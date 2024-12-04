package com.santorini;


public class Minotaur implements GodCard {
    private static final int MAX_HEIGHT = 3;

    // MinotaurGodCard.java
    @Override
    public boolean canMoveToCell(Worker worker, Cell targetCell) {
        // Check if the cell is occupied by an opponent's worker and if the push is possible
        if (targetCell.isOccupied() && targetCell.getOccupiedWorker().getPlayer() != worker.getPlayer()) {
            int pushX = targetCell.getX() + (targetCell.getX() - worker.getPosition().getX());
            int pushY = targetCell.getY() + (targetCell.getY() - worker.getPosition().getY());
     
            // Check if the push to cell is within bounds and not occupied
            if (worker.getBoard().isValidPosition(pushX, pushY)) {
                Cell pushToCell = worker.getBoard().getCell(pushX, pushY);
                return !pushToCell.isOccupied();
            }
        }
        return !targetCell.isOccupied() && worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();

    }


    @Override
    public boolean canBuildOnCell(Worker worker, Cell targetCell) {
        return worker.getValidNeighbors().contains(targetCell) && !targetCell.getBlock().hasDome();
    }


    @Override
    public boolean allowsExtraBuild() {
        return false;
    }
    
    @Override
    public void applyMoveRule(Worker worker, Cell origin, Cell target) {
        System.out.println("Minotaur applyMove");
        if (target.isOccupied() && target.getOccupiedWorker().getPlayer() != worker.getPlayer()) {
            int dx = target.getX() - origin.getX();  // Delta X
            int dy = target.getY() - origin.getY();  // Delta Y
            int destinationX = target.getX() + dx;  // Calculate push X coordinate
            int destinationY = target.getY() + dy;  // Calculate push Y coordinate
    
            // Ensure the push to cell is within bounds and not occupied
            if (worker.getBoard().isValidPosition(destinationX, destinationY)) {
                Cell destination = worker.getBoard().getCell(destinationX, destinationY);
                if (!destination.isOccupied()) {
                    // Move the opponent worker to the new cell
                    Worker pushedWorker = target.getOccupiedWorker();
                    pushedWorker.setPosition(destination);  // Update the position of the pushed worker
                    destination.setOccupiedWorker(pushedWorker);  // Assign the pushed worker to the new cell
                    target.setOccupiedWorker(null);  // Clear the original cell

                }
            }
        }
    
        // Move the Minotaur worker to the target
        origin.setOccupiedWorker(null);  // Clear the original cell
        target.setOccupiedWorker(worker);  // Set the worker to the new cell
        worker.setPosition(target);  // Update the worker's position
    }
    


    @Override
    public void applyBuildRule(Worker worker, Cell targetCell) {
        targetCell.getBlock().buildBlock();
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        return worker.getPosition().getBlock().getHeight() == MAX_HEIGHT;
    }
}
