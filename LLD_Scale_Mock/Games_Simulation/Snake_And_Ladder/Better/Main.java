package Low_Level_Design.LLD_Scale_Mock.Games_Simulation.Snake_And_Ladder.Better;
// package Low_Level_Design.LLD_Scale_Mock.Games_Simulation.Snake_And_Ladder.Good;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// ==========================================
// 1. DICE ABSTRACTION (DIP & Extensibility)
// ==========================================
interface DiceStrategy {
    int roll();
}

class StandardDice implements DiceStrategy {
    private final int numberOfDice;

    public StandardDice(int numberOfDice) {
        this.numberOfDice = Math.max(1, numberOfDice);
    }

    @Override
    public int roll() {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += ThreadLocalRandom.current().nextInt(1, 7);
        }
        return total;
    }
}

// ==========================================
// 2. BOARD ENTITY STRATEGY (OCP & Polymorphic Jumps)
// ==========================================
interface BoardEntity {
    int getStart();
    int getEnd();
    String getEntityName();
}

class Snake implements BoardEntity {
    private final int head;
    private final int tail;

    public Snake(int head, int tail) {
        if (tail >= head) {
            throw new IllegalArgumentException("Snake tail must be lower than head.");
        }
        this.head = head;
        this.tail = tail;
    }

    @Override public int getStart() { return head; }
    @Override public int getEnd() { return tail; }
    @Override public String getEntityName() { return "Snake 🐍"; }
}

class Ladder implements BoardEntity {
    private final int start;
    private final int end;

    public Ladder(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("Ladder end must be higher than start.");
        }
        this.start = start;
        this.end = end;
    }

    @Override public int getStart() { return start; }
    @Override public int getEnd() { return end; }
    @Override public String getEntityName() { return "Ladder 🪜"; }
}

// ==========================================
// 3. DOMAIN MODELS (SRP)
// ==========================================
class Player {
    private final String id;
    private final String name;
    private int position;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.position = 0; // Standard start before square 1
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}

class Board {
    private final int size;
    private final Map<Integer, BoardEntity> specialEntities;

    public Board(int size) {
        this.size = size;
        this.specialEntities = new HashMap<>();
    }

    public void addEntity(BoardEntity entity) {
        specialEntities.put(entity.getStart(), entity);
    }

    public int getSize() {
        return size;
    }

    public boolean hasEntity(int position) {
        return specialEntities.containsKey(position);
    }

    public BoardEntity getEntity(int position) {
        return specialEntities.get(position);
    }
}

// ==========================================
// 4. GAME SERVICE (Iterative Game Loop & Queue Turn Management)
// ==========================================
class SnakeAndLadderGame {
    private final Board board;
    private final DiceStrategy dice;
    private final Queue<Player> playerQueue;
    private final List<Player> leaderboard;

    public SnakeAndLadderGame(Board board, DiceStrategy dice, List<Player> players) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("At least 2 players required to start game.");
        }
        this.board = board;
        this.dice = dice;
        this.playerQueue = new LinkedList<>(players);
        this.leaderboard = new ArrayList<>();
    }

    public void play() {
        System.out.println("=== GAME STARTED ===");

        while (playerQueue.size() > 1) {
            Player currentPlayer = playerQueue.poll();
            int rollValue = dice.roll();
            int initialPos = currentPlayer.getPosition();
            int targetPos = initialPos + rollValue;

            System.out.printf("%n[Turn] %s rolled a %d. Moving from %d -> %d%n",
                    currentPlayer.getName(), rollValue, initialPos, targetPos);

            if (targetPos > board.getSize()) {
                System.out.printf("  %s overshot position %d. Turn skipped.%n", 
                        currentPlayer.getName(), board.getSize());
                playerQueue.add(currentPlayer);
                continue;
            }

            // Resolve entities (Snakes, Ladders, or future elements)
            int finalPos = resolvePosition(targetPos);
            currentPlayer.setPosition(finalPos);

            if (finalPos == board.getSize()) {
                leaderboard.add(currentPlayer);
                System.out.printf("  🎉 %s REACHED THE FINISH LINE! Rank #%d%n",
                        currentPlayer.getName(), leaderboard.size());
            } else {
                playerQueue.add(currentPlayer);
            }
        }

        if (!playerQueue.isEmpty()) {
            Player lastPlayer = playerQueue.poll();
            leaderboard.add(lastPlayer);
            System.out.printf("%n[Game Over] %s placed last (Rank #%d)%n", 
                    lastPlayer.getName(), leaderboard.size());
        }

        printLeaderboard();
    }

    private int resolvePosition(int currentPos) {
        int visitedCount = 0;
        int maxTransitionsAllowed = 10; // Protection against infinite jump loops

        while (board.hasEntity(currentPos)) {
            if (visitedCount >= maxTransitionsAllowed) {
                System.out.println("  ⚠️ Warning: Infinite loop detected in board layout. Halting movement.");
                break;
            }
            BoardEntity entity = board.getEntity(currentPos);
            System.out.printf("  Encountered %s at %d! Moving to %d%n",
                    entity.getEntityName(), entity.getStart(), entity.getEnd());
            currentPos = entity.getEnd();
            visitedCount++;
        }
        return currentPos;
    }

    private void printLeaderboard() {
        System.out.println("\n=== FINAL LEADERBOARD ===");
        for (int i = 0; i < leaderboard.size(); i++) {
            System.out.printf("Rank #%d: %s%n", i + 1, leaderboard.get(i).getName());
        }
    }
}

// ==========================================
// 5. DRIVER CODE
// ==========================================
public class Main {
    public static void main(String[] args) {
        Board board = new Board(100);

        // Add Snakes
        board.addEntity(new Snake(99, 3));
        board.addEntity(new Snake(45, 23));
        board.addEntity(new Snake(87, 43));
        board.addEntity(new Snake(98, 4));

        // Add Ladders
        board.addEntity(new Ladder(4, 13));
        board.addEntity(new Ladder(15, 80));
        board.addEntity(new Ladder(29, 98));
        board.addEntity(new Ladder(52, 89));

        DiceStrategy dice = new StandardDice(1);

        List<Player> players = List.of(
                new Player("P1", "Shivank"),
                new Player("P2", "Vishu"),
                new Player("P3", "Amit")
        );

        SnakeAndLadderGame game = new SnakeAndLadderGame(board, dice, players);
        game.play();
    }
}
