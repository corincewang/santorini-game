package com.santorini;

import java.util.Arrays;

public final class GameState {
    private final CellState[] cells;
    private final String currentPlayer;
    private static final int SIDE = 5;

    private GameState(CellState[] cells, String currentPlayer) {
        this.cells = cells;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Constructs a GameState from a Game instance.
     *
     * @param game the current Game instance
     * @return a new GameState object representing the current game state
     */
    public static GameState forGame(Game game) {
        CellState[] cellStates = getCellStates(game.getBoard());
        String currentPlayer = game.getTurn().toString();
        return new GameState(cellStates, currentPlayer);
    }

    public CellState[] getCells() {
        return this.cells;
    }

    public String getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Returns the JSON representation of the GameState.
     *
     * @return a JSON string representing the game state
     */
    @Override
    public String toString() {
        return """
                {
                    "cells": %s,
                    "currentPlayer": "%s"
                }
                """.formatted(Arrays.toString(this.cells), this.currentPlayer);
    }

    /**
     * Converts the Board's cells into an array of CellState objects.
     *
     * @param board the Board instance
     * @return an array of CellState objects representing the board's state
     */
    private static CellState[] getCellStates(Board board) {
        int boardSize = SIDE; // Assuming a 5x5 board for Santorini
        CellState[] cellStates = new CellState[boardSize * boardSize];

        int index = 0;
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Cell cell = board.getCell(x, y);

                String text = "";
                if (cell.isOccupied()) {
                    text = "W"; // Worker present
                }

                boolean playable = !cell.getBlock().hasDome();
                cellStates[index++] = new CellState(x, y, text, playable, cell.getBlock().getHeight());
            }
        }

        return cellStates;
    }

    /**
     * Inner CellState class for representing individual cell state in GameState.
     */
    public static class CellState {
        private final int x;
        private final int y;
        private final String text;
        private final boolean playable;
        private final int height;

        public CellState(int x, int y, String text, boolean playable, int height) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.playable = playable;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getText() {
            return this.text;
        }

        public boolean isPlayable() {
            return this.playable;
        }

        public int getHeight() {
            return this.height;
        }

        @Override
        public String toString() {
            return """
                    {
                        "text": "%s",
                        "playable": %b,
                        "x": %d,
                        "y": %d,
                        "height": %d
                    }
                    """.formatted(this.text, this.playable, this.x, this.y, this.height);
        }
    }
}
