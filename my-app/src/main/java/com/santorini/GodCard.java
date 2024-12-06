package com.santorini;

/**
 * The GodCard interface defines the rules and special abilities associated with
 * a specific god card in the Santorini game. Implementations of this interface
 * can modify the default game behavior for movement, building, and win conditions
 * to reflect the unique powers of each god card.
 */
public interface GodCard {

    /**
     * Determines if a worker can move to a specific cell based on the rules
     * of the god card and the general movement rules of the game.
     *
     * @param worker    The worker attempting to move.
     * @param targetCell The cell the worker is attempting to move to.
     * @return true if the move is valid according to the god card's rules, false otherwise.
     */
    boolean canMoveToCell(Worker worker, Cell targetCell);

    /**
     * Determines if a worker can build on a specific cell based on the rules
     * of the god card and the general building rules of the game.
     *
     * @param worker    The worker attempting to build.
     * @param targetCell The cell the worker is attempting to build on.
     * @return true if the build is valid according to the god card's rules, false otherwise.
     */
    boolean canBuildOnCell(Worker worker, Cell targetCell);

    /**
     * Checks if the worker satisfies the win condition as per the god card's
     * special rules and the default win condition of the game.
     *
     * @param worker The worker whose win condition is being checked.
     * @return true if the worker fulfills the win condition, false otherwise.
     */
    boolean checkWinCondition(Worker worker);

    /**
     * Indicates whether the god card allows an extra build during the same turn.
     * This is used to determine if the player can perform additional build actions.
     *
     * @return true if the god card permits an extra build, false otherwise.
     */
    boolean allowsExtraBuild();

    /**
     * Applies the god card's special build rules when a worker performs a build action.
     * This method can modify the building behavior of the worker according to the god card's abilities.
     *
     * @param worker    The worker performing the build.
     * @param targetCell The cell where the build action is performed.
     */
    void applyBuildRule(Worker worker, Cell targetCell);

    /**
     * Applies the god card's special movement rules when a worker performs a move action.
     * This method can modify the movement behavior of the worker according to the god card's abilities.
     *
     * @param worker The worker performing the move.
     * @param origin The starting cell of the worker before the move.
     * @param target The target cell of the worker after the move.
     */
    void applyMoveRule(Worker worker, Cell origin, Cell target);

    /**
     * Resets the state of the god card at the end of a player's turn.
     * This method is useful for clearing any temporary state or restrictions
     * imposed by the god card during a single turn (e.g., resetting lastBuiltCell).
     */
    void resetAll();
}
