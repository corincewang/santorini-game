package com.santorini;


public class Block {
    private int height;
    private boolean hasDome;

    public Block() {
        this.height = 0;
        this.hasDome = false;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasDome() {
        return hasDome;
    }

    public void buildBlock() {
        if (!hasDome && height < 3) {
            height++;
            System.out.println("Builded Block to" + this + "Current height = " + this.height);
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
