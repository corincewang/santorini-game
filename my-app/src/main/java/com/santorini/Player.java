package com.santorini;


public class Player {
    private final Worker[] workers;
    private boolean winStatus;
    private boolean loseStatus;
    private static final int MAX_HEIGHT = 3;

    public Player() {
        this.workers = new Worker[2];
        this.winStatus = false;
        this.loseStatus = false;
    }


    public void placeWorker(Cell cell1, Cell cell2, Board board){
        this.workers[0] = new Worker(cell1, board);
        this.workers[1] = new Worker(cell2, board);
        cell1.setOccupiedWorker(this.workers[0]); 
        cell2.setOccupiedWorker(this.workers[1]); 
    }

    public boolean checkWinStatus() {
        for (Worker worker : this.getWorkers()) {
            if (worker.getPosition().getBlock().getHeight() == MAX_HEIGHT) {
                this.winStatus = true;
                return winStatus;
            }
        }
        return winStatus;
    }

    public boolean checkLoseStatus() {
        for (Worker worker : this.getWorkers()) {
            if (worker.canMoveWorker()) {
                return loseStatus;
            }
        }
        this.loseStatus = true;
        return loseStatus;
    }


    public Worker[] getWorkers() {
        return workers;
    }

    public Worker selectWorker(int workerNum){
        return this.workers[workerNum];
    }


    public void moveWorker(Worker worker, Cell cell) {
        if (worker.canMoveToCell(cell)) {
           worker.doWorkerMove(cell);
        }
        else{
            System.out.println("Cannot Make this Move!");
        }
    }

}
