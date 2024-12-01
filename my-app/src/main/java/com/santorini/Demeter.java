package com.santorini;

public class Demeter implements GodCard {
    private Cell lastBuiltCell = null;
    private static final int MAX_HEIGHT = 3;

    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        // Demeter does not affect moving, so defer to default logic
        return !targetCell.isOccupied() && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }

    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        // Can build if not the last built cell
        return !targetCell.equals(lastBuiltCell) && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }

    @Override
    public void applyBuildRule(Worker worker, Cell targetCell) {
        if (canBuildOnCell(worker, targetCell)) {
            targetCell.getBlock().buildBlock();
            lastBuiltCell = targetCell;  // Remember last built cell
        }
    }

    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell destination) {
        // TODO Auto-generated method stub
       // Demeter does not affect moving, so no special rules
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        // Normal win condition
        return worker.getPosition().getBlock().getHeight() == MAX_HEIGHT;
    }


}
