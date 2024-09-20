package com.santorini;

public class Game {
    private Board board;
    private Player[] players;
    private Player turn;
    private boolean endState;

    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.players = new Player[]{player1, player2};
        this.turn = player1; 
        this.endState = false; 
    }


    public Player getTurn() {
        return turn;
    }

    public Board getBoard(){
        return board;
    }

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

    public void switchTurn() {
        this.turn = (turn == players[0]) ? players[1] : players[0];
    }


}
