package com.santorini;


public class Cell {
    private Worker occupied;
    private Block block;
    private int height;
    private int x;
    private int y;


    public Cell(int x, int y) {
        this.occupied = null;
        this.block = new Block();
        this.height = 0;
        this.x = x;
        this.y = y;
    }

    public boolean isOccupied() {
        return occupied != null;
    }
    
    public void setOccupiedWorker(Worker worker) {
        this.occupied = worker;
    }

    public int[] getCoordinates() {
        return new int[]{x, y};
    }

    public Worker getOccupiedWorker() {
        return occupied;
    }

    public Block getBlock() {
        return block;
    }

    public int getHeight(){
        return height;
    }
}
