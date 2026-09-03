package Low_Level_Design.LLD_Scale_Mock.Games_Simulation.Tic_Tac_Toe.Better; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;

enum GameState {
    STARTED,
    IN_PROGRESS, 
    FINISHED
} 

class Position {
    private final int row, col;
    Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    } 
    public int getCol() {
        return col;
    } 
}
/* Player holds domain attributes */
class Player {
    private final String id;
    private String name;
    private String symbol;
    private PlayerMoveStrategy moveStrategy;

    public Player(String id, String name, String symbol, PlayerMoveStrategy moveStrategy) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.moveStrategy = moveStrategy;
    }
    public String getId() {
        return id;
    } 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Optional<Position> makeMove() {
        return moveStrategy.makeMove(this);
    }
}


// Observer pattern to decouple the UI logic from the Game Board
interface GameEventListener {
    void onPlayerMove(Player player, Position position);
    void onInvalidMoveAttempt(Player player, Position position);
    void onMoveAttemptExhausted(Player player);
    void onBoardUpdated(GameBoard board);
    void onGameWon(Player player, int rank);
    void onGameDraw();
    void onGameFinished();
}

/* ConsoleView manages presentation. */
class ConsoleView implements GameEventListener {

    @Override
    public void onGameDraw() {
        System.out.println("Game is Draw ! ");
    }

    @Override
    public void onGameFinished() {
        System.out.println("Game is finished ! Thank u for participating.. ");
    }

    @Override
    public void onGameWon(Player player, int rank) {
        System.out.println(player.getName() + " [ " + player.getSymbol() + " ] won! and rank is : " + rank);
    }

    @Override
    public void onPlayerMove(Player player, Position position) {
        System.out.println(player.getName() + " [ " + player.getSymbol() + " ] moved at position (" + position.getRow() + " , " + position.getCol() + ") ");
    }

    @Override
    public void onInvalidMoveAttempt(Player player, Position position) {
        System.out.println(player.getName() + " [ " + player.getSymbol() + " ] Your move for " + " (" + position.getRow() + " , " + position.getCol() + ") " + " is invalild.. Kindly try different move.. ");
    }
    @Override
    public void onMoveAttemptExhausted(Player player) {
        System.out.println(player.getName() + " [ " + player.getSymbol() + " ] Turn skipped ! Your maximum allowed number of invalid moves are reached.. ");
    }

    @Override
    public void onBoardUpdated(GameBoard board) {
        for(int i=0; i<board.getSize(); i++) {
            for(int j=0; j<board.getSize(); j++) {
                if(board.getPlayer(i, j)!=null) System.out.print(board.getPlayer(i,j).getSymbol() + " ");
                else System.out.print("NULL");
            }
            System.out.println();
        }
    }
}

// factory method to create players (to extend in future for players such as EASY_BOT, HARD_BOT, REMOTE_PLAYER)
// factory interface
abstract class PlayerFactory {
    public Player createPlayer(String id, String name, String symbol) {
        PlayerMoveStrategy moveStrategy = createStrategy();
        return new Player(id, name, symbol, moveStrategy);
    }
    abstract protected PlayerMoveStrategy createStrategy();
}

//concret factory 
class HumanPlayerFactory extends PlayerFactory { 
    private final Scanner scanner;
    HumanPlayerFactory(Scanner scanner) { 
        this.scanner = scanner;
    }
    @Override
    protected PlayerMoveStrategy createStrategy() {
        return new PlayerInputMove(scanner);
    }
}

class BotPlayerFactory extends PlayerFactory { 
    private final GameBoard board;
    BotPlayerFactory(GameBoard board) { 
        this.board = board;
    }
    @Override
    protected PlayerMoveStrategy createStrategy() {
        return new ComputerMove(board);
    }
}

/* GameBoard manages board state */
class GameBoard {
    private Player[][] board;
    private int size;
    private int numberOfCellsOccupied = 0;

    GameBoard(int size) {
        this.size = size;
        board = new Player[size][size];
    }

    public int getSize() {
        return size;
    } 
    public boolean isValidMove(int row, int col) {
        if(row>=0 && col>=0 && row<size && col<size && isEmpty(row, col)) return true;
        return false;
    }
    public boolean isEmpty(int row, int col) {
        return board[row][col] == null;
    }
    public boolean isFull() {
        return numberOfCellsOccupied == size*size;
    }
    public int getNumberOfCellsOccupied() {
        return numberOfCellsOccupied;
    }

    public void setPlayer(int row, int col, Player player) {
        if(!isValidMove(row, col)) {
            throw new IllegalArgumentException("Invalid row OR col points ");
        }
        board[row][col] = player; 
        this.numberOfCellsOccupied += 1;
    }
    public Player getPlayer(int row, int col) {
        return board[row][col];
    }
    public boolean hasPlayer(int row, int col) {
        return board[row][col]!=null;
    }
}

//strategy pattern
interface PlayerMoveStrategy {
    Optional<Position> makeMove(Player p);
}
class ComputerMove implements PlayerMoveStrategy {
    private GameBoard board;
    ComputerMove(GameBoard board) {
        this.board = board;
    }

    @Override
    public Optional<Position> makeMove(Player p) { //making the first empty move
        for(int i=0; i<board.getSize(); i++) {
            for(int j=0; j<board.getSize(); j++){
                if(board.isEmpty(i,j)) {
                    System.out.println("[ " + p.getSymbol() + " ] selecting row & col values  : " + i + " " + j);
                    return Optional.of(new Position(i, j));
                }
            }
        }
        return Optional.empty();
    }
}

class PlayerInputMove implements PlayerMoveStrategy {
    private final Scanner scanner;
    PlayerInputMove(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Position> makeMove(Player p) { 
        System.out.println("[ " + p.getSymbol() + " ] select your row & col values  : ");
        int i = scanner.nextInt();
        int j = scanner.nextInt(); 
        return Optional.of(new Position(i,j));
    }
}


/* GameService handles orchestration */
class GameService { 
    private GameBoard board;
    private Queue<Player> turnQueue;
    private List<Player> leaderboard; 
    private List<GameEventListener> listeners; 
    private GameState gameState;
    private final Map<Player,int[]> rowCount = new HashMap<>();
    private final Map<Player,int[]> colCount = new HashMap<>();
    private final Map<Player,Integer> diagonalCount = new HashMap<>(); 
    private final Map<Player,Integer> revDiagonalCount = new HashMap<>();

    GameService(GameBoard board, List<Player> turnQueue) { 
        this.board = board;
        this.turnQueue = new LinkedList<>();
        this.leaderboard = new ArrayList<>(); 
        this.listeners = new ArrayList<>();
        this.gameState = GameState.STARTED;

        for(Player player: turnQueue) {
            this.turnQueue.add(player);
            rowCount.put(player, new int[this.board.getSize()]);
            colCount.put(player, new int[this.board.getSize()]);
            diagonalCount.put(player, 0);
            revDiagonalCount.put(player, 0);
        }
    }
    public void addEventListeners(GameEventListener listener) {
        this.listeners.add(listener);
    }

    public Optional<Position> getValidMove(Player player) {
        Optional<Position> result = player.makeMove();
        int attempsLeft = 10;
            
        while(result.isPresent() && !board.isValidMove(result.get().getRow(), result.get().getCol()) && attempsLeft>0) {
            notifyOnInvalidMoveAttempt(player, result.get());
            result = player.makeMove(); 
            attempsLeft--;
        }
            
        if(result.isPresent() && board.isValidMove(result.get().getRow(), result.get().getCol())) {
            return result;
        }
        return  Optional.empty();
    }

    void play() {
        this.gameState = GameState.IN_PROGRESS;
        while(turnQueue.size() > 1 && this.gameState.equals(GameState.IN_PROGRESS)) {
            Player player = turnQueue.poll();
            Optional<Position> result = getValidMove(player);
            if(result.isEmpty()) { // skip the turn
                turnQueue.add(player);
                notifyOnMoveAttemptExhausted(player);
                continue; 
            }

            Position move = result.get();
            int row = move.getRow(), col = move.getCol();

            board.setPlayer(row, col, player);  
            notifyOnPlayerMove(player, move);
            notifyOnBoardUpdated(board);

            if(winCondition(row, col, player)) {
                leaderboard.add(player);
                notifyOnGameWon(player, leaderboard.size()); 
            } else if(draw()) { 
                notifyOnGameDraw();
                notifyOnGameFinished();
                this.gameState = GameState.FINISHED;
                return; 
            } else  {
                turnQueue.add(player);
            }
        } 
        
        if(turnQueue.size() == 1) {
            Player player = turnQueue.poll();
            leaderboard.add(player); 
            notifyOnGameFinished();
        }
    }

    public boolean winCondition(int row, int col, Player player) {
        int n = board.getSize(); 
        int rows[] = rowCount.get(player);
        int cols[] = colCount.get(player);
        rows[row]++;
        cols[col]++;

        if(row==col) diagonalCount.put(player, diagonalCount.get(player)+1);
        if(row+col==n-1) revDiagonalCount.put(player, revDiagonalCount.get(player)+1);

        if(rows[row]==n || 
            cols[col]==n || 
                diagonalCount.get(player)==n ||
                revDiagonalCount.get(player)==n
        ) {
            return true;
        }
        return false;
    } 
    public boolean draw() {
        return board.isFull();
    }
    
    // passing the update to all listeners
    void notifyOnPlayerMove(Player player, Position position) { 
        for(GameEventListener listener: listeners) { 
            listener.onPlayerMove(player, position); 
        } 
    }
    void notifyOnInvalidMoveAttempt(Player player, Position position) { 
        for(GameEventListener listener: listeners) { 
            listener.onInvalidMoveAttempt(player, position); 
        } 
    }
    void notifyOnMoveAttemptExhausted(Player player) { 
        for(GameEventListener listener: listeners) { 
            listener.onMoveAttemptExhausted(player); 
        } 
    }
    void notifyOnBoardUpdated(GameBoard board) { 
        for(GameEventListener listener: listeners) { 
            listener.onBoardUpdated(board);
        } 
    }
    void notifyOnGameWon(Player player, int rank) { 
        for(GameEventListener listener: listeners) { 
            listener.onGameWon(player, rank); 
        } 
    }
    void notifyOnGameDraw() { 
        for(GameEventListener listener: listeners) { 
            listener.onGameDraw(); 
        } 
    }
    void notifyOnGameFinished() { 
        for(GameEventListener listener: listeners) { 
            listener.onGameFinished(); 
        } 
    }
}


public class Main {
    public static void main(String[] args) {
        GameBoard board = new GameBoard(3);
        Scanner scanner = new Scanner(System.in);
        PlayerFactory humanFactory = new HumanPlayerFactory(scanner);
        PlayerFactory botFactory = new BotPlayerFactory(board);

        Player player1 = humanFactory.createPlayer("1", "John", "X");
        Player player2 = botFactory.createPlayer("2", "Bot", "O");

        try {
            GameService gameService = new GameService(board, List.of(player1, player2));
            gameService.addEventListeners(new ConsoleView());
            gameService.play();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    } 
}
