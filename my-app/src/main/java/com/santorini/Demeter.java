package com.santorini;

public class Demeter implements GodCard {
    private Cell lastBuiltCell = null;
    private static final int MAX_HEIGHT = 3;

    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        return !targetCell.isOccupied() && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }

    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        return !targetCell.equals(lastBuiltCell) && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }

    @Override
    public boolean allowsExtraBuild(){
        return true;
    }


    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell destination) {
       // Demeter does not affect moving, so no special rules
        worker.setPosition(destination);
        origin.setOccupiedWorker(null);
        destination.setOccupiedWorker(worker);
    }

    @Override
    public void applyBuildRule(Worker worker, Cell targetCell) {
        if (canBuildOnCell(worker, targetCell)) {
            targetCell.getBlock().buildBlock();
            lastBuiltCell = targetCell;  // Remember last built cell
        }
    }


    @Override
    public boolean checkWinCondition(Worker worker) {
        // Normal win condition
        return worker.getPosition().getBlock().getHeight() == MAX_HEIGHT;
    }

    public void resetAll() {
        lastBuiltCell = null;
    }


}
