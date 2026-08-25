package Low_Level_Design.LLD_Scale_Mock.Games_Simulation.Snake_And_Ladder.Bad; 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom; 


class Dice {
    public int roll() {
        int diceNumber = ThreadLocalRandom.current().nextInt(1,7);
        return diceNumber;
    }
}

class Pair {
    private final int start,end;
    Pair(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public int getStart() {
        return start;
    }
    public int getEnd() {
        return end;
    }
}

class Player {
    private int pos; 
    private String name;
    Player(String name) {
        this.name = name;
        pos = 1; 
    }

    public int getPos() {
        return pos;
    }
    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int move(int pos, Dice dice) { 
        int nextPos = pos + dice.roll();
        System.out.println("[Player move] " + " moved from " + pos + " to " + nextPos);
        this.pos = nextPos;
        return nextPos;
    }
}


interface BoardElement {
    public boolean isPresent(int pos);
    public int move(int pos);
}

class Snake implements BoardElement {
    private Map<Integer,Integer> snakeTail;

    Snake(List<Pair> snakeList) {
        snakeTail = new HashMap<>();
        for(Pair p: snakeList) {
            snakeTail.put(p.getStart(), p.getEnd());
        }
    }

    public boolean isPresent(int pos) {
        if(snakeTail.getOrDefault(pos, -1) != -1) return true;
        return false;
    }

    public int move(int pos) {
        int nextMove = snakeTail.get(pos).intValue();
        System.out.println("[Snake bite] " + " pos down from " + pos + " to " + nextMove);
        return nextMove;
    }
}

class Ladder implements BoardElement {
    private Map<Integer,Integer> ladderTop;

    Ladder(List<Pair> ladderList) {
        ladderTop = new HashMap<>();
        for(Pair p: ladderList) {
            ladderTop.put(p.getStart(), p.getEnd());
        }
    }

    public boolean isPresent(int pos) {
        if(ladderTop.getOrDefault(pos, -1) != -1) return true;
        return false;
    }

    public int move(int pos) {
        int nextMove = ladderTop.get(pos).intValue();
        System.out.println("[Ladder climb] " + " climbing from " + pos + " to " + nextMove);
        return nextMove;
    }
}


class Board {
    private final List<Integer> board;
    private final BoardElement snake;
    private final BoardElement ladder;
    int n;

    Board(int n, BoardElement snake, BoardElement ladder) {
        board = new ArrayList<>();
        for(int i=1; i<=n; i++) board.add(i);

        this.n = n;
        this.snake = snake;
        this.ladder = ladder;
    }
    
    boolean isSnake(int pos) {
        return snake.isPresent(pos);
    }
    boolean isLadder(int pos) {
        return ladder.isPresent(pos);
    }

    int snakeMove(int pos) {
        if(!snake.isPresent(pos)) throw new IllegalArgumentException("No snake at this position");
        return snake.move(pos);
    }
    int ladderMove(int pos) {
        if(!ladder.isPresent(pos)) throw new IllegalArgumentException("No ladder at this position");
        return ladder.move(pos);
    }
}


class GameService {
    public List<Player> players;
    public List<String> winners;
    public Board board; 
    public Dice dice;

    GameService(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        winners = new ArrayList<>();
        dice = new Dice();
    }

    public void start() {
        Iterator<Player> it = players.iterator();

        while(it.hasNext()) { 
            Player p = it.next();
            int pos = p.getPos();
            System.out.println(p.getName() + " move : "); 
            pos = p.move(pos,dice);
            while(true) {
                if(board.isSnake(pos)) pos = board.snakeMove(pos);
                else if(board.isLadder(pos)) pos = board.ladderMove(pos); 
                else break;
            }

            p.setPos(pos);
            if(p.getPos() >= board.n) {
                winners.add(p.getName());
                System.out.println("\n ------- Player " + p.getName() + " completed this game and got rank - " + winners.size()); 
                it.remove(); 
            }
        }
        System.out.println();
        

        if(players.size() == 0) {
            System.out.println("Game finished ! No player remainig now");
        } else {
            start();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BoardElement snake = new Snake(
            List.of(
                new Pair(99,3), new Pair(45, 23), new Pair(87, 43),
                new Pair(98, 4)
            )
        );
        BoardElement ladder = new Ladder(
            List.of(
                new Pair(15,80), new Pair(29, 98), new Pair(52, 89),
                new Pair(4, 13)
            )
        );

        Board board = new Board(100, snake, ladder);
        Player shivank = new Player("Shivank");
        Player vishu = new Player("Vishu");
        List<Player> players = new ArrayList<>(List.of(shivank, vishu));

        GameService gameService = new GameService(players, board);
        gameService.start();
    }
}
