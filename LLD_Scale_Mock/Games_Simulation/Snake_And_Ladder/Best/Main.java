package Low_Level_Design.LLD_Scale_Mock.Games_Simulation.Snake_And_Ladder.Best;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// ==========================================
// 1. OBSERVER PATTERN: EVENT LISTENER UI DECOUPLING
// ==========================================
interface GameEventListener {
    void onGameStarted(int boardSize, int playerCount);
    void onTurnPlayed(Player player, int rollValue, int initialPos, int targetPos);
    void onEntityEncountered(Player player, BoardEntity entity, int fromPos, int toPos);
    void onTurnOvershot(Player player, int overshotPos, int boardSize);
    void onPlayerFinished(Player player, int rank);
    void onGameOver(Player lastPlayer, int totalRank, List<Player> leaderboard);
    void onWarning(String message);
}

class ConsoleGameEventListener implements GameEventListener {
    @Override
    public void onGameStarted(int boardSize, int playerCount) {
        System.out.printf("=== GAME STARTED (Board Size: %d, Players: %d) ===%n", boardSize, playerCount);
    }

    @Override
    public void onTurnPlayed(Player player, int rollValue, int initialPos, int targetPos) {
        System.out.printf("%n[Turn] %s rolled a %d. Moving from %d -> %d%n",
                player.getName(), rollValue, initialPos, targetPos);
    }

    @Override
    public void onEntityEncountered(Player player, BoardEntity entity, int fromPos, int toPos) {
        System.out.printf("  Encountered %s at %d! Moving to %d%n",
                entity.getEntityName(), fromPos, toPos);
    }

    @Override
    public void onTurnOvershot(Player player, int overshotPos, int boardSize) {
        System.out.printf("  %s overshot finish line (reached %d, max %d). Turn skipped.%n",
                player.getName(), overshotPos, boardSize);
    }

    @Override
    public void onPlayerFinished(Player player, int rank) {
        System.out.printf("  🎉 %s REACHED THE FINISH LINE! Rank #%d%n", player.getName(), rank);
    }

    @Override
    public void onGameOver(Player lastPlayer, int totalRank, List<Player> leaderboard) {
        System.out.printf("%n[Game Over] %s placed last (Rank #%d)%n", lastPlayer.getName(), totalRank);
        System.out.println("\n=== FINAL LEADERBOARD ===");
        for (int i = 0; i < leaderboard.size(); i++) {
            System.out.printf("Rank #%d: %s%n", i + 1, leaderboard.get(i).getName());
        }
    }

    @Override
    public void onWarning(String message) {
        System.out.printf("  ⚠️ Warning: %s%n", message);
    }
}

// ==========================================
// 2. DICE STRATEGY
// ==========================================
interface DiceStrategy {
    int roll();
}

class StandardDice implements DiceStrategy {
    private final int numberOfDice;

    public StandardDice(int numberOfDice) {
        if (numberOfDice < 1) {
            throw new IllegalArgumentException("Must have at least 1 dice.");
        }
        this.numberOfDice = numberOfDice;
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
// 3. BOARD ENTITIES (STRATEGY / POLYMORPHISM)
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
            throw new IllegalArgumentException("Snake tail (" + tail + ") must be strictly below head (" + head + ").");
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
            throw new IllegalArgumentException("Ladder end (" + end + ") must be strictly above start (" + start + ").");
        }
        this.start = start;
        this.end = end;
    }

    @Override public int getStart() { return start; }
    @Override public int getEnd() { return end; }
    @Override public String getEntityName() { return "Ladder 🪜"; }
}

// ==========================================
// 4. DOMAIN MODELS & BUILDER PATTERN
// ==========================================
class Player {
    private final String id;
    private final String name;
    private int position;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.position = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}

class Board {
    private final int size;
    private final Map<Integer, BoardEntity> specialEntities;

    Board(int size, Map<Integer, BoardEntity> specialEntities) {
        this.size = size;
        this.specialEntities = Collections.unmodifiableMap(new HashMap<>(specialEntities));
    }

    public int getSize() { return size; }
    public boolean hasEntity(int position) { return specialEntities.containsKey(position); }
    public BoardEntity getEntity(int position) { return specialEntities.get(position); }
}

class BoardBuilder {
    private int size = 100;
    private final Map<Integer, BoardEntity> entities = new HashMap<>();

    public BoardBuilder setSize(int size) {
        if (size < 10) {
            throw new IllegalArgumentException("Board size must be at least 10.");
        }
        this.size = size;
        return this;
    }

    public BoardBuilder addSnake(int head, int tail) {
        return addEntity(new Snake(head, tail));
    }

    public BoardBuilder addLadder(int start, int end) {
        return addEntity(new Ladder(start, end));
    }

    public BoardBuilder addEntity(BoardEntity entity) {
        if (entities.containsKey(entity.getStart())) {
            throw new IllegalStateException("Position " + entity.getStart() + " already contains an entity.");
        }
        entities.put(entity.getStart(), entity);
        return this;
    }

    public Board build() {
        validateEntities();
        validateNoCycles();
        return new Board(size, entities);
    }

    private void validateEntities() {
        for (BoardEntity entity : entities.values()) {
            if (entity.getStart() <= 0 || entity.getStart() >= size) {
                throw new IllegalStateException("Entity start position " + entity.getStart() + " out of board bounds (1 to " + (size - 1) + ").");
            }
            if (entity.getEnd() <= 0 || entity.getEnd() > size) {
                throw new IllegalStateException("Entity end position " + entity.getEnd() + " out of board bounds (1 to " + size + ").");
            }
        }
    }

    private void validateNoCycles() {
        for (Integer startPos : entities.keySet()) {
            Set<Integer> visited = new HashSet<>();
            int curr = startPos;
            while (entities.containsKey(curr)) {
                if (visited.contains(curr)) {
                    throw new IllegalStateException("Infinite cycle detected in board layout starting at square " + curr);
                }
                visited.add(curr);
                curr = entities.get(curr).getEnd();
            }
        }
    }
}

// ==========================================
// 5. GAME ENGINE (ORCHESTRATION)
// ==========================================
class SnakeAndLadderGame {
    private final Board board;
    private final DiceStrategy dice;
    private final Queue<Player> playerQueue;
    private final List<Player> leaderboard;
    private final GameEventListener listener;

    public SnakeAndLadderGame(Board board, DiceStrategy dice, List<Player> players, GameEventListener listener) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("At least 2 players are required.");
        }
        this.board = Objects.requireNonNull(board, "Board cannot be null.");
        this.dice = Objects.requireNonNull(dice, "DiceStrategy cannot be null.");
        this.listener = listener != null ? listener : new ConsoleGameEventListener();
        this.playerQueue = new LinkedList<>(players);
        this.leaderboard = new ArrayList<>();
    }

    public void play() {
        listener.onGameStarted(board.getSize(), playerQueue.size());

        while (playerQueue.size() > 1) {
            Player currentPlayer = playerQueue.poll();
            int rollValue = dice.roll();
            int initialPos = currentPlayer.getPosition();
            int targetPos = initialPos + rollValue;

            listener.onTurnPlayed(currentPlayer, rollValue, initialPos, targetPos);

            if (targetPos > board.getSize()) {
                listener.onTurnOvershot(currentPlayer, targetPos, board.getSize());
                playerQueue.add(currentPlayer);
                continue;
            }

            int finalPos = resolvePosition(currentPlayer, targetPos);
            currentPlayer.setPosition(finalPos);

            if (finalPos == board.getSize()) {
                leaderboard.add(currentPlayer);
                listener.onPlayerFinished(currentPlayer, leaderboard.size());
            } else {
                playerQueue.add(currentPlayer);
            }
        }

        if (!playerQueue.isEmpty()) {
            Player lastPlayer = playerQueue.poll();
            leaderboard.add(lastPlayer);
            listener.onGameOver(lastPlayer, leaderboard.size(), Collections.unmodifiableList(leaderboard));
        }
    }

    private int resolvePosition(Player player, int currentPos) {
        int visitedCount = 0;
        int maxTransitionsAllowed = 10;

        while (board.hasEntity(currentPos)) {
            if (visitedCount >= maxTransitionsAllowed) {
                listener.onWarning("Max entity transitions reached at position " + currentPos + ". Halting extra jumps.");
                break;
            }
            BoardEntity entity = board.getEntity(currentPos);
            listener.onEntityEncountered(player, entity, entity.getStart(), entity.getEnd());
            currentPos = entity.getEnd();
            visitedCount++;
        }
        return currentPos;
    }
}

// ==========================================
// 6. DRIVER / MAIN EXECUTION
// ==========================================
public class Main {
    public static void main(String[] args) {
        // Construct board safely using BoardBuilder
        Board board = new BoardBuilder()
                .setSize(100)
                .addSnake(99, 3)
                .addSnake(45, 23)
                .addSnake(87, 43)
                .addSnake(98, 4)
                .addLadder(4, 13)
                .addLadder(15, 80)
                .addLadder(29, 98)
                .addLadder(52, 89)
                .build();

        DiceStrategy dice = new StandardDice(1);
        GameEventListener listener = new ConsoleGameEventListener();

        List<Player> players = List.of(
                new Player("P1", "Shivank"),
                new Player("P2", "Vishu"),
                new Player("P3", "Amit")
        );

        SnakeAndLadderGame game = new SnakeAndLadderGame(board, dice, players, listener);
        game.play();
    }
}