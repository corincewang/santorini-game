package com.santorini;

public class Hephaestus implements GodCard {
    private boolean hasBuiltOnce = false;

    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        // Hephaestus does not affect moving
        return worker.canMoveToCell(targetCell);
    }

    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        // Normal building rules apply
        return worker.canBuildToCell(targetCell);
    }

    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell destination) {
        // No specific move rules for Hephaestus
    }

    @Override
    public void applyBuildRule(Worker worker, Cell targetCell) {
        // Allows a second build on the same cell if it's not a dome yet
        targetCell.getBlock().buildBlock();  // First build
        if (!targetCell.getBlock().hasDome() && !hasBuiltOnce) {
            targetCell.getBlock().buildBlock();  // Second build (only block, not dome)
            hasBuiltOnce = true;
        }
        hasBuiltOnce = false; // Reset for the next build turn
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        // Normal win condition
        return worker.checkWin(worker);
    }
}
