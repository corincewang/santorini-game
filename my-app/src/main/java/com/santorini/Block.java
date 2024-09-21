package com.santorini;


public class Block {
    private int height;
    private boolean hasDome;

    public Block() {
        this.height = 0;
        this.hasDome = false;
    }

    @Override
    public String toString() {
        return "Block(height=" + height + ", hasDome=" + hasDome + ")";
    }

    public int getHeight() {
        return height;
    }

    public boolean hasDome() {
        return hasDome;
    }

    public boolean isValidHeight(){
        return height < 3;
    }

    public void buildBlock() {
        if (!hasDome && isValidHeight()) {
            height++;
            System.out.println("Builded Block to: " + this);
        } 
        else if (height == 3) {
            addDome();
        }
    }

    public void addDome() {
        hasDome = true;
        System.out.println("Add Dome to" + this);
    }
}
