package com.santorini;

public class Hephaestus implements GodCard {
    private boolean hasBuiltOnce = false;
    private static final int MAX_HEIGHT = 3;
    private Cell lastBuiltCell = null;

    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        // Hephaestus does not affect moving
        return !targetCell.isOccupied() && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }

    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        if (hasBuiltOnce){
            return targetCell.equals(lastBuiltCell) && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
        }
        // Normal building rules apply
        return worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }
    
    @Override
    public boolean allowsExtraBuild() {
        return true;
    }

    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell destination) {
        worker.setPosition(destination);
        origin.setOccupiedWorker(null);
        destination.setOccupiedWorker(worker);
    }

    @Override
    public void applyBuildRule(Worker worker, Cell targetCell) {
        if (canBuildOnCell(worker, targetCell)) {
            targetCell.getBlock().buildBlock();
            lastBuiltCell = targetCell;  // Remember last built cell
            hasBuiltOnce = true;
        }
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        // Normal win condition
        return worker.getPosition().getBlock().getHeight() == MAX_HEIGHT;
    }

    @Override
    public void resetAll() {
        lastBuiltCell = null;
        hasBuiltOnce = false;
    }
}
