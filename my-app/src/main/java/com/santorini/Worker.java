package com.santorini;

public class Worker {
    private Cell position;


    public Worker() {
        this.position = null;
    }

    public Cell getPosition() {
        return position;
    }

    public boolean canMoveWorker(){
        return true;
    }

    public boolean canMoveToCell(Cell newCell) {
        return canMoveWorker() && !newCell.isOccupied() && newCell.getBlock().getHeight() <= position.getBlock().getHeight() + 1;
    }


    public void doWorkerMove(Cell newCell) {
        if (canMoveToCell(newCell)){
            if (position != null) {
                position.setOccupiedWorker(null);  
            }
            newCell.setOccupiedWorker(this);  
            this.position = newCell;  
            System.out.println(this + "Moved to" + this.position + ", height = " + this.position.getHeight());
        }

    }

    public void buildBlock(Cell cell) {
        cell.getBlock().buildBlock();
    }
}
