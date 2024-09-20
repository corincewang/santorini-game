package com.santorini;


public class Tower {
    private int height;
    private boolean hasDome;

    public Tower() {
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
        } else if (height == 3) {
            addDome();
        }
    }

    public void addDome() {
        hasDome = true;
    }
}
