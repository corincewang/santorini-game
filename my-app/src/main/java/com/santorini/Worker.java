package com.santorini;

import java.util.ArrayList;
import java.util.List;

/**
 * The Worker class represents a worker in the Santorini game. 
 * It manages the worker's position on the board, determines valid movements, 
 * and handles building actions in neighboring cells.
 */
public class Worker {
    private Cell position;
    private final Board board;
    private static final int SIDE = 5;
    private static final int MAX_HEIGHT = 3;
    private final Player player;

     /**
     * Constructs a Worker with an initial position and reference to the game board.
     *
     * @param position the initial cell where the worker is placed
     * @param board the board on which the worker is located
     * @param player the player on which the worker is located
     */
    public Worker (Cell position, Board board, Player player){
        this.position = position;
        this.board = board;
        this.player = player;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Cell getPosition() {
        return position;
    }

    public Player getPlayer() {
        return player;
    }
    /**
     * Sets a new position for the worker.
     *
     * @param cell the new cell where the worker will be placed
     */
    public void setPosition(Cell cell) {
        this.position = cell; 
    }



    public boolean checkWin(Worker worker){
        Cell position = worker.getPosition();
        Block block = position.getBlock();
        if (player.getGodCard() != null) {
            return player.getGodCard().checkWinCondition(worker);
        }
        return block.getHeight() == MAX_HEIGHT;
    }


    /**
     * Retrieves a list of valid neighboring cells the worker can move to.
     * A valid neighbor is an adjacent cell that is not occupied, 
     * is within the board boundaries, and has an acceptable height difference.
     *
     * @return a list of valid neighboring cells for movement
     */
    public List<Cell> getValidNeighbors(){
        // System.out.print("worker");
        // System.out.print(board == null);
        List<Cell> validNeighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = position.getX() + i;
                int newY = position.getY() + j;
                if (newX >= 0 && newX < SIDE && newY >= 0 && newY < SIDE){
                    Cell neighbor = board.getCell(newX, newY); 
                            // Check if the new coordinates are within board bounds (0 to 4)
                        if (!neighbor.isOccupied()
                        && (neighbor.getBlock().getHeight() - this.position.getBlock().getHeight()  <=1)){
                        validNeighbors.add(neighbor);
                    }
                }
            }
        }
        // System.out.println("valid Neighbors: " + validNeighbors);
        return validNeighbors;
    }

    /**
     * Checks if there are any valid neighboring cells the worker can move to.
     *
     * @return true if there are no valid cells to move to, false otherwise
     */
    public boolean canMoveWorker(){
        return this.getValidNeighbors().isEmpty();
    }

    /**
     * Determines if the worker can move to a specified cell.
     * A move is valid if the cell is unoccupied, within reach, and does not have a dome.
     *
     * @param newCell the cell to which the worker intends to move
     * @return true if the worker can move to the specified cell, false otherwise
     */
    public boolean canMoveToCell(Cell newCell) {
        if (player.getGodCard() != null) {
            return player.getGodCard().canMoveToCell(this, newCell);
        }
        return !newCell.isOccupied() && this.getValidNeighbors().contains(newCell) && !newCell.getBlock().hasDome();
    }

    /**
     * Executes a move of the worker to a specified cell if the move is valid.
     *
     * @param newCell the target cell for the worker's move
     */
    public void doWorkerMove(Cell newCell) {
        if (canMoveToCell(newCell)){
            if (position != null) {
                position.setOccupiedWorker(null);  
            }
            newCell.setOccupiedWorker(this);  
            this.position = newCell;  
            System.out.println("\n"+ this + " Moved to" + this.position + ", height = " + this.position.getBlock().getHeight());

            if (checkWin(this)){
                System.out.println(this.player.getName() + " wins!");
            }
        }
        else {
            System.out.println("Invalid Move: Cannot move to " + newCell);
        }
    }

    /**
     * Checks if the worker can build a block in a specified target cell.
     * The build is valid if the cell is a neighboring cell within reach and does not have a dome.
     *
     * @param cell the cell where the worker intends to build
     * @return true if building in the cell is allowed, false otherwise
     */
    public boolean canBuildToCell(Cell cell) {
        if (player.getGodCard() != null) {
            return player.getGodCard().canBuildOnCell(this, cell);
        }
        return this.getValidNeighbors().contains(cell) && !cell.getBlock().hasDome();
    }

    /**
     * Builds a block in the specified cell if the action is valid.
     * If the build is invalid, an error message is printed.
     *
     * @param cell the cell where the worker intends to build
     */
    public void buildBlock(Cell cell) {
        if (canBuildToCell(cell)) {
            if (player.getGodCard() != null) {
                player.getGodCard().applyBuildRule(this, cell);
            } else {
                cell.getBlock().buildBlock();
            }
        } else {
            System.out.println("Invalid Build: Cannot build on " + cell);
        }
    }
}
