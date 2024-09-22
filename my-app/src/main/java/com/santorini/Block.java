package com.santorini;


public class Block {
    private int height;
    private boolean hasDome;
    private Cell cell;
    private final int x;
    private final int y;
    private static final int MAX_HEIGHT = 3;

    public Block(int x, int y) {
        this.height = 0;
        this.hasDome = false;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Block(x=" + x + " y=" + y + ", height=" + height + ", hasDome=" + hasDome + ")";
    }

    public int getHeight() {
        return height;
    }

    public boolean hasDome() {
        return hasDome;
    }

    public int[] getPosition(){
        return this.cell.getCoordinates();
    }
    
    public boolean isValidHeight(){
        return height < MAX_HEIGHT;
    }

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

    public void addDome() {
        hasDome = true;
        System.out.println("Add Dome to" + this);
    }
}
