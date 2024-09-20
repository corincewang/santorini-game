package com.santorini;


public class Player {
    private final Worker[] workers;
    private boolean winStatus;
    private boolean loseStatus;

    public Player() {
        this.workers = new Worker[]{new Worker(), new Worker()};
        this.winStatus = false;
        this.loseStatus = false;
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

    public Worker selectWorker(Worker worker){
        return worker;
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
