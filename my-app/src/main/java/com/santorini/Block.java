package com.santorini;

/**
 * The Block class represents a single building block in the Santorini game.
 * Each block has a specific height and may contain a dome when it reaches maximum height.
 */
public class Block {
    private int height;
    private boolean hasDome;
    private Cell cell;
    private final int x;
    private final int y;
    private static final int MAX_HEIGHT = 3;

     /**
     * Constructor for creating a Block at specified coordinates (x, y).
     * Initializes the block with height 0 and no dome.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     */
    public Block(int x, int y) {
        this.height = 0;
        this.hasDome = false;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the Block.
     *
     * @return a formatted string describing the block's position, height, and dome status
     */
    @Override
    public String toString() {
        return "Block(x=" + x + " y=" + y + ", height=" + height + ", hasDome=" + hasDome + ")";
    }

    /**
     * Retrieves the current height of the block.
     *
     * @return the current height of the block
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks whether the block has a dome.
     *
     * @return true if the block has a dome, false otherwise
     */
    public boolean hasDome() {
        return hasDome;
    }

    /**
    * Retrieves the coordinates of the block's position.
    *
    * @return an array of integers containing the block's x and y coordinates
    */
    public int[] getPosition(){
        return this.cell.getCoordinates();
    }
    
     /**
     * Checks if the current height of the block is below the maximum height.
     *
     * @return true if the block's height is less than the maximum height, false otherwise
     */
    public boolean isValidHeight(){
        return height < MAX_HEIGHT;
    }

    /**
     * Increases the block's height by 1, provided the block has not reached maximum height and does not have a dome.
     * If the block reaches maximum height, a dome is added instead.
     * Prints a message to the console indicating the result of the build attempt.
     */
    public void buildBlock() {
        if (!hasDome && isValidHeight()) {
            height++;
            System.out.println("Builded Block to: " + this + "\n");
        } 
        else if (height == MAX_HEIGHT) {
            addDome();
        }
        else{
            System.out.println("Invalid Build because block " + this + " has Dome or reaches max height. Rechoose a cell!");
        }
    }

    /**
     * Adds a dome to the block, indicating that it has reached maximum height and cannot be built further.
     * Prints a message to the console when a dome is added.
     */
    public void addDome() {
        hasDome = true;
        System.out.println("Add Dome to" + this);
    }
}
