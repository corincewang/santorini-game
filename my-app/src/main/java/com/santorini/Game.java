package com.santorini;

public class Game {
    private Board board;
    private Player[] players;
    private Player turn;
    private boolean state;

    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.players = new Player[]{player1, player2};
        this.turn = player1; // 游戏开始时默认玩家1先手
        this.state = true;  // 游戏进行中
    }

    public boolean checkWin(Player player) {
        for (Worker worker : player.getWorkers()) {
            if (worker.getPosition().getTower().getHeight() == 3) {
                return true;
            }
        }
        return false;
    }

    public void switchTurn() {
        this.turn = (turn == players[0]) ? players[1] : players[0];
    }

    public Player getTurn() {
        return turn;
    }

    public Board getBoard(){
        return board;
    }

    public boolean isGameOver() {
        return !state;
    }

    public void endGame() {
        state = false;
    }
}
