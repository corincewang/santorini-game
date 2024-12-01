package com.santorini;

public class Pan implements GodCard {
    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        return worker.canMoveToCell(targetCell);
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
        if (worker.getPosition().getBlock().getHeight() - worker.getPosition().getBlock().getHeight() >= 2) {
            return true;
        }
        return worker.checkWin(worker);
    }
}
