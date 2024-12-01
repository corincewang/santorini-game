package com.santorini;

public class Pan implements GodCard {
    private static final int MAX_HEIGHT = 3;

    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        return !targetCell.isOccupied() && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }

    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        return worker.canBuildToCell(targetCell);
    }

    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell destination) {
        // Regular move
        worker.setPosition(destination);
        origin.setOccupiedWorker(null);
        destination.setOccupiedWorker(worker);
    }

    @Override
    public void applyBuildRule(Worker worker, Cell targetCell) {
        targetCell.getBlock().buildBlock();
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        // Check if moving down two or more levels
        if (worker.getPreviousHeight() - worker.getPosition().getBlock().getHeight() >= 2) {
            return true;
        }
        return worker.getPosition().getBlock().getHeight() == MAX_HEIGHT;
    }
}
