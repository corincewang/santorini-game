package com.santorini;


public class Player {
    private Worker[] workers;
    private boolean winStatus;
    private boolean loseStatus;

    public Player() {
        this.workers = new Worker[2];
        this.winStatus = false;
        this.loseStatus = false;
    }

    public void placeWorker(Cell cell1, Cell cell2, Board board){
        this.workers[0] = new Worker(cell1, board);
        this.workers[1] = new Worker(cell2, board);
    }

    public boolean checkWinStatus() {
        for (Worker worker : this.getWorkers()) {
            if (worker.getPosition().getBlock().getHeight() == 3) {
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
           this.selectWorker(1).doWorkerMove(cell);
        }
        else{
            System.out.println("Cannot Make this Move!");
        }
    }

}
