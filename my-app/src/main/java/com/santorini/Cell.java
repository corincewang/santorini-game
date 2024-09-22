package com.santorini;


public class Cell {
    private Worker occupied;
    private Block block;
    private int height;
    private int x;
    private int y;
    private Board board;


    public Cell(int x, int y) {
        this.occupied = null;
        this.block = new Block(x, y);
        this.height = 0;
        this.x = x;
        this.y = y;
        this.board = board;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean isOccupied() {
        return occupied != null;
    }
    
    public void setOccupiedWorker(Worker worker) {
        this.occupied = worker;
    }

    public Board getBoard(){
        return this.board;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
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
