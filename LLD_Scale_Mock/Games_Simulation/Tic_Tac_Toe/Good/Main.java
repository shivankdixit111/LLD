package Low_Level_Design.LLD_Scale_Mock.Games_Simulation.Tic_Tac_Toe.Good;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;


class Position {
    private int row, col;
    Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
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

    public void printBoard() {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(board[i][j]!=null) System.out.print(board[i][j].getName() + " ");
                else System.out.print(  " NULL ");
            }
            System.out.println();
        }
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





class GameService { 
    private GameBoard board;
    private Queue<Player> turnQueue;
    private List<Player> leaderboard; 
    private final Map<Player,int[]> rowCount = new HashMap<>();
    private final Map<Player,int[]> colCount = new HashMap<>();
    private final Map<Player,Integer> diagonalCount = new HashMap<>(); 
    private final Map<Player,Integer> revDiagonalCount = new HashMap<>();

    GameService(GameBoard board, List<Player> turnQueue) { 
        this.board = board;
        this.turnQueue = new LinkedList<>();
        leaderboard = new ArrayList<>(); 

        int n = board.getSize();  

        for(Player player: turnQueue) {
            this.turnQueue.add(player);
            rowCount.put(player, new int[n]);
            colCount.put(player, new int[n]);
            diagonalCount.put(player, 0);
            revDiagonalCount.put(player, 0);
        }
    }
    public Optional<Position> getValidMove(Player player) {
        Optional<Position> result = player.makeMove();
        int attempsLeft = 10;
            
        while(result.isPresent() && !board.isValidMove(result.get().getRow(), result.get().getCol()) && attempsLeft>0) {
            System.out.println(player.getName() + " [ " + player.getSymbol() + " ] Your move for " + " (" + result.get().getRow() + " , " + result.get().getCol() + ") " + " is invalild.. Kindly try different move.. ");
            result = player.makeMove(); 
            attempsLeft--;
        }
            
        if(result.isPresent() && board.isValidMove(result.get().getRow(), result.get().getCol())) {
            return result;
        }
        return  Optional.empty();
    }

    void play() {
        while(turnQueue.size() > 1) { 
            Player player = turnQueue.poll();
            Optional<Position> result = getValidMove(player);
            if(result.isEmpty()) { // skip the turn
                turnQueue.add(player);
                System.out.println(player.getName() + " [ " + player.getSymbol() + " ] Turn skipped ! Your maximum allowed number of invalid moves are reached.. ");
                continue; 
            }

            Position move = result.get();
            int row = move.getRow(), col = move.getCol();
            board.setPlayer(row, col, player);  
            board.printBoard();

            if(winCondition(row, col, player)) {
                leaderboard.add(player);
                System.out.println("Player " + player.getName() + " got the rank " + leaderboard.size());
            } else if(draw()) {
                System.out.println("Game is DRAW ! ");
                return;
            }  else {
                turnQueue.add(player);
            }
        }
        
        
        if(turnQueue.size() == 1) {
            Player player = turnQueue.poll();
            System.out.println("Game ended ! ");
            leaderboard.add(player);
            System.out.println("Player " + player.getName() + " got the rank " + leaderboard.size());
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
}


public class Main {
    public static void main(String[] args) {
        GameBoard board = new GameBoard(3);

        Player player1 = new Player("1", "John", "X", new PlayerInputMove(new Scanner(System.in)));
        Player player2 = new Player("2", "Bot", "O", new ComputerMove(board));

        try {
            GameService gameService = new GameService(board, List.of(player1, player2));
            gameService.play();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
