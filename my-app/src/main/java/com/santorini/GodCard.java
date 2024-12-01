package com.santorini;

public interface GodCard {
    boolean canMoveToCell(Worker worker, Cell targetCell);
    boolean canBuildOnCell(Worker worker, Cell targetCell);
    
    void applyBuildRule(Worker worker, Cell targetCell);
    void applyMoveRule(Worker worker, Cell origin, Cell destination);
    boolean checkWinCondition(Worker worker);
}
