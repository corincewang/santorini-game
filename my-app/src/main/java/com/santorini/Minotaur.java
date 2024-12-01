package com.santorini;

public class Minotaur implements GodCard {
    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        // Check if the cell is occupied by an opponent worker and if the push is possible
        if (targetCell.isOccupied() && targetCell.getOccupiedWorker().getPlayer() != worker.getPlayer()) {
            Cell pushToCell = worker.getBoard().getCell(targetCell.getX() + (targetCell.getX() - worker.getPosition().getX()), 
                                             targetCell.getY() + (targetCell.getY() - worker.getPosition().getY()));
            // Can move if the push to cell is valid (in bounds, not occupied)
            return pushToCell != null && !pushToCell.isOccupied();
        }
        return worker.canMoveToCell(targetCell);
    }

    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        return worker.canBuildToCell(targetCell);
    }

    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell destination) {
        if (destination.isOccupied() && destination.getOccupiedWorker().getPlayer() != worker.getPlayer()) {
            Cell pushToCell = worker.getBoard().getCell(destination.getX() + (destination.getX() - origin.getX()), 
                                                        destination.getY() + (destination.getY() - origin.getY()));
            // Move opponent worker
            Worker pushedWorker = destination.getOccupiedWorker();
            pushToCell.setOccupiedWorker(pushedWorker);
            pushedWorker.setPosition(pushToCell);
        }
        // Move the Minotaur's worker
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
        return worker.checkWin(worker);
    }
}
