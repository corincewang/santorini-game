package com.santorini;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private Cell position;
    private Board board;

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
                Cell neighbor = board.getCell(newX, newY); 
                // Check if the new coordinates are within board bounds (0 to 4)
                if (newX >= 0 && newX < 5 && newY >= 0 && newY < 5 && !neighbor.isOccupied()
                    && (neighbor.getBlock().getHeight() - this.position.getBlock().getHeight()  <=1)){
                    validNeighbors.add(neighbor);
                }
            }
        }
        System.out.println("valid Neighbors: " + validNeighbors);
        return validNeighbors;
    }

    public boolean canMoveWorker(){
        return this.getValidNeighbors().isEmpty();
    }

    public boolean canMoveToCell(Cell cell) {
        // System.out.println("canMoveToCell");
        return this.getValidNeighbors().contains(cell) && !cell.getBlock().hasDome();
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
