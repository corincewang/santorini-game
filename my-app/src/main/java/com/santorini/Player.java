package com.santorini;


public class Player {
    private final Worker[] workers;
    private boolean win;
    private boolean gameOver;

    public Player() {
        this.workers = new Worker[]{new Worker(), new Worker()};
        this.win = false;
        this.gameOver = false;
    }

    public Worker[] getWorkers() {
        return workers;
    }

    public boolean hasWon() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void moveWorker(Worker worker, Cell cell) {
        if (worker.canMove(cell)) {
            worker.move(cell);
        }
    }

    public void buildTower(Worker worker, Cell cell) {
        cell.getTower().buildBlock();
    }
}
