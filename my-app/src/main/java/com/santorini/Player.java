package com.santorini;

/**
 * The Player class represents a player in the Santorini game. 
 * Each player has two workers, and the class manages player actions such as placing, moving workers, 
 * and checking win or lose conditions.
 */
public class Player {
    private final Worker[] workers;
    private boolean winStatus;
    private boolean loseStatus;
    private static final int MAX_HEIGHT = 3;

     /**
     * Initializes a new player with an array of two workers and initial win and lose status set to false.
     */
    public Player() {
        this.workers = new Worker[2];
        this.winStatus = false;
        this.loseStatus = false;
    }

    /**
     * Places the player's workers on two specified cells on the board.
     *
     * @param cell1 the cell for the first worker
     * @param cell2 the cell for the second worker
     * @param board the game board
     */
    public void placeWorker(Cell cell1, Cell cell2, Board board) {
        if (cell1 != null) {
            this.workers[0] = new Worker(cell1, board);
            cell1.setOccupiedWorker(this.workers[0]); 
        }
    
        if (cell2 != null) {
            this.workers[1] = new Worker(cell2, board);
            cell2.setOccupiedWorker(this.workers[1]); 
        }
    }
    

    /**
     * Checks if the player has met the winning condition (worker reaches maximum height).
     *
     * @return true if the player has won, false otherwise
     */
    public boolean checkWinStatus() {
        for (Worker worker : this.getWorkers()) {
            if (worker.getPosition().getBlock().getHeight() == MAX_HEIGHT) {
                this.winStatus = true;
                return winStatus;
            }
        }
        return winStatus;
    }

    /**
     * Checks if the player has met the losing condition (no valid moves for any worker).
     *
     * @return true if the player has lost, false otherwise
     */
    public boolean checkLoseStatus() {
        for (Worker worker : this.getWorkers()) {
            if (worker.canMoveWorker()) {
                return loseStatus;
            }
        }
        this.loseStatus = true;
        return loseStatus;
    }


    /**
     * Returns an array of the player's workers.
     *
     * @return an array of Worker objects belonging to the player
     */
    public Worker[] getWorkers() {
        return workers;
    }

     /**
     * Selects one of the player's workers based on the specified index.
     *
     * @param workerNum the index of the worker (0 or 1)
     * @return the selected Worker object
     */
    public Worker selectWorker(int workerNum){
        return this.workers[workerNum];
    }


}
