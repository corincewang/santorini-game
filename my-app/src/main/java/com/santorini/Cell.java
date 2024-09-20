package com.santorini;


public class Cell {
    private Worker occupied;
    private Tower tower;

    public Cell() {
        this.occupied = null;
        this.tower = new Tower();
    }

    public boolean isOccupied() {
        return occupied != null;
    }

    public Worker getOccupiedWorker() {
        return occupied;
    }

    public void setOccupiedWorker(Worker worker) {
        this.occupied = worker;
    }

    public Tower getTower() {
        return tower;
    }
}
