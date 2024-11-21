package com.santorini;

/**
 * The Game class manages the overall game state for a Santorini match, 
 * including the board, players, and the current player's turn.
 * It provides methods to control gameplay flow, check for game end conditions, 
 * and switch between players' turns.
 */
public class Game {
    private final Board board;
    private final Player[] players;
    private Player turn;
    private boolean endState;
    private String currentAction; 

    /**
     * Constructs a new Game instance with two players and initializes the board.
     * Sets player1 as the starting player.
     *
     * @param player1 the first player
     * @param player2 the second player
     */
    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.players = new Player[]{player1, player2};
        this.turn = player1; 
        this.endState = false; 
        this.currentAction = "place";
    }

    public Player[] getPlayers(){
        return players;
    }

    /**
     * Retrieves the player whose turn it currently is.
     *
     * @return the Player whose turn it is to play
     */
    public Player getTurn() {
        return turn;
    }

    /**
     * Returns the game board on which the game is being played.
     *
     * @return the Board object representing the game's playing area
     */
    public Board getBoard(){
        return board;
    }

    public String getCurrentAction() {
        return this.currentAction;
    }

    public void setCurrentAction(String action) {
        this.currentAction = action;
    }

    /**
     * Checks if the game has reached an end condition. Determines a winner if either 
     * player has achieved a win or if the opposing player has met a lose condition.
     *
     * @param player1 the first player
     * @param player2 the second player
     * @return the Player who has won the game, or null if the game has not ended
     */
    public Player checkGameEnd(Player player1, Player player2){
        // player1 wins
        if (player1.checkWinStatus() || player2.checkLoseStatus()){
            endState = true;
            System.out.println("player1 wins");
            return player1;
        }
        // player2 wins
        else if (player2.checkWinStatus() || player1.checkLoseStatus()){
            endState = true;
            System.out.println("player2 wins");
            return player2;
        }
        return null;
    }

    public void setEndState(){
        endState = true;
    }

    /**
     * Switches the current turn to the next player.
     */
    public void switchTurn() {
        this.turn = (turn == players[0]) ? players[1] : players[0];
    }
    
      /**
     * Moves a specified worker to a target cell, if the move is valid.
     *
     * @param worker the Worker to move
     * @param cell the target Cell to move the worker to
     */
    public void moveWorker(Worker worker, Cell cell) {
        if (worker.canMoveToCell(cell)) {
           worker.doWorkerMove(cell);
        }
        else{
            System.out.println("Cannot Make this Move!");
        }
    }

    
/**
 * Executes a move action for the current player at the specified coordinates (x, y).
 * If the move is valid, updates the game state and switches the turn to the next player.
 *
 * @param x the x-coordinate of the target cell
 * @param y the y-coordinate of the target cell
 * @return the updated Game instance after the move
 */
public Game play(int x, int y) {
    // Retrieve the target cell on the board
    Cell targetCell = this.board.getCell(x, y);

    // Check if the target cell is valid and not occupied
    if (targetCell == null || targetCell.isOccupied()) {
        System.out.println("Invalid move: Cell (" + x + ", " + y + ") is either out of bounds or already occupied.");
        return this;
    }

    // Check if the game has already ended
    if (this.endState) {
        System.out.println("Game has already ended. No further moves are allowed.");
        return this;
    }

    // Select the current player's worker (assume worker 0 is the default worker for simplicity)
    Worker worker = this.turn.getWorkers()[0];

    // Check if the worker can move to the target cell
    if (!worker.canMoveToCell(targetCell)) {
        System.out.println("Invalid move: Worker cannot move to cell (" + x + ", " + y + ").");
        return this;
    }

    // Perform the worker's move
    worker.doWorkerMove(targetCell);

    // Check for a win condition after the move
    if (this.turn.checkWinStatus()) {
        this.endState = true;
        System.out.println("Player " + this.turn + " wins!");
        return this;
    }

    // Switch the turn to the next player
    switchTurn();

    // Return the updated game state
    return this;
}


}
