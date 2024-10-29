package com.santorini;

/**
 * The Cell class represents a single cell on the game board. 
 * Each cell has coordinates, a block (for building structures), and may be occupied by a worker.
 */
public class Cell {
    private Worker occupied;
    private final Block block;
    private final int height;
    private final int x;
    private final int y;
    private Board board;

    /**
     * Constructs a Cell at the specified coordinates (x, y) with an empty state (no worker).
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    public Cell(int x, int y) {
        this.occupied = null;
        this.block = new Block(x, y);
        this.height = 0;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the cell's coordinates.
     *
     * @return a string in the format "(x, y)" for the cell's position
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Checks if the cell is currently occupied by a worker.
     *
     * @return true if the cell is occupied by a worker, false otherwise
     */
    public boolean isOccupied() {
        return occupied != null;
    }
    
    /**
     * Sets the worker occupying this cell.
     *
     * @param worker the Worker to place in this cell
     */
    public void setOccupiedWorker(Worker worker) {
        this.occupied = worker;
    }

    /**
     * Retrieves the board that contains this cell.
     *
     * @return the Board object this cell belongs to
     */
    public Board getBoard(){
        return this.board;
    }

    /**
     * Gets the x-coordinate of this cell.
     *
     * @return the x-coordinate of the cell
     */
    public int getX(){
        return x;
    }

     /**
     * Gets the y-coordinate of this cell.
     *
     * @return the y-coordinate of the cell
     */
    public int getY(){
        return y;
    }

     /**
     * Returns the coordinates of this cell as an array.
     *
     * @return an integer array with the x and y coordinates of the cell
     */
    public int[] getCoordinates() {
        return new int[]{x, y};
    }

    /**
     * Gets the worker currently occupying this cell, if any.
     *
     * @return the Worker occupying this cell, or null if unoccupied
     */
    public Worker getOccupiedWorker() {
        return occupied;
    }

    /**
     * Gets the block associated with this cell, used for building structures.
     *
     * @return the Block object for this cell
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Retrieves the current height level of the block in this cell.
     *
     * @return the height of the block in the cell
     */
    public int getHeight(){
        return height;
    }
}
