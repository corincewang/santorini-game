package com.santorini;

public interface GodCard {
    boolean canMoveToCell(Worker worker, Cell targetCell);
    boolean canBuildOnCell(Worker worker, Cell targetCell);
    boolean checkWinCondition(Worker worker);
    boolean allowsExtraBuild();

    void applyBuildRule(Worker worker, Cell targetCell);
    void applyMoveRule(Worker worker, Cell origin, Cell target);
    void resetAll();
   
}
