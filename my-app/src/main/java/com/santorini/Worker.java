package com.santorini;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private Cell position;
    private final Board board;
    private static final int SIDE = 5;

    public Worker (Cell position, Board board){
        this.position = position;
        this.board = board;
    }
    
    public Cell getPosition() {
        return position;
    }

    public void setPosition(Cell cell) {
        this.position = cell; 
    }

    public List<Cell> getValidNeighbors(){
        // System.out.print("worker");
        // System.out.print(board == null);
        List<Cell> validNeighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newX = position.getX() + i;
                int newY = position.getY() + j;
                if (newX >= 0 && newX < SIDE && newY >= 0 && newY < SIDE){
                    Cell neighbor = board.getCell(newX, newY); 
                            // Check if the new coordinates are within board bounds (0 to 4)
                        if (!neighbor.isOccupied()
                        && (neighbor.getBlock().getHeight() - this.position.getBlock().getHeight()  <=1)){
                        validNeighbors.add(neighbor);
                    }
                }
            }
        }
        // System.out.println("valid Neighbors: " + validNeighbors);
        return validNeighbors;
    }

    public boolean canMoveWorker(){
        return this.getValidNeighbors().isEmpty();
    }

    public boolean canMoveToCell(Cell newCell) {
        return !newCell.isOccupied() && this.getValidNeighbors().contains(newCell) && !newCell.getBlock().hasDome();
    }


    public void doWorkerMove(Cell newCell) {
        if (canMoveToCell(newCell)){
            if (position != null) {
                position.setOccupiedWorker(null);  
            }
            newCell.setOccupiedWorker(this);  
            this.position = newCell;  
            System.out.println("\n"+ this + " Moved to" + this.position + ", height = " + this.position.getBlock().getHeight());
        }
    }

    public boolean canBuildToCell(Cell cell) {
        return this.getValidNeighbors().contains(cell) && !cell.getBlock().hasDome();
    }

    public void buildBlock(Cell cell) {
        if (canBuildToCell(cell)){
            cell.getBlock().buildBlock();
        }
        else{
            System.out.println("Invalid Build because" + cell + " is not a neighbor. Rechoose a cell!\n");
        }
       
    }
}
