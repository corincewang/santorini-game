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
    @SuppressWarnings("unused")
    private boolean endState;

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

    /**
     * Switches the current turn to the next player.
     */
    public void switchTurn() {
        this.turn = (turn == players[0]) ? players[1] : players[0];
    }


}
