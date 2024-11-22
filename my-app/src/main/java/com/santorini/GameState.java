package com.santorini;

import java.util.Arrays;

public final class GameState {
    private final CellState[] cells;
    private final String currentPlayer;
    private static final int SIDE = 5;
    private final String action;

    private GameState(CellState[] cells, String currentPlayer, String action) {
        this.cells = cells;
        this.currentPlayer = currentPlayer;
        this.action = action;
    }

    /**
     * Constructs a GameState from a Game instance.
     *
     * @param game the current Game instance
     * @return a new GameState object representing the current game state
     */
    public static GameState forGame(Game game) {
        CellState[] cellStates = getCellStates(game);
        String currentPlayer = game.getTurn().getName();
        String action = game.getCurrentAction();
        return new GameState(cellStates, currentPlayer, action);
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
                    "currentPlayer": "%s",
                    "action": "%s"
                }
                """.formatted(Arrays.toString(this.cells), this.currentPlayer, this.action);
    }

    /**
     * Converts the Board's cells into an array of CellState objects.
     *
     * @param game the Game instance
     * @return an array of CellState objects representing the board's state
     */
    private static CellState[] getCellStates(Game game) {
        Board board = game.getBoard();
        Player[] players = game.getPlayers();
        int boardSize = SIDE;
        CellState[] cellStates = new CellState[boardSize * boardSize];

        int index = 0;
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Cell cell = board.getCell(x, y);

                String text = "";
                String player = ""; // Identify which player owns the worker
                if (cell.isOccupied()) {
                    Worker occupiedWorker = cell.getOccupiedWorker();


                    if (occupiedWorker.getPlayer().equals(players[0])) {
                        player = "Player1";
                    } else if (occupiedWorker.getPlayer().equals(players[1])) {
                        player = "Player2";
                    }
                }

                boolean playable = !cell.getBlock().hasDome();
                cellStates[index++] = new CellState(x, y, text, playable, cell.getBlock().getHeight(), player);
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
        private final String player; // New field for identifying the player

        public CellState(int x, int y, String text, boolean playable, int height, String player) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.playable = playable;
            this.height = height;
            this.player = player;
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

        public String getPlayer() {
            return this.player;
        }

        @Override
        public String toString() {
            return """
                    {
                        "text": "%s",
                        "playable": %b,
                        "x": %d,
                        "y": %d,
                        "height": %d,
                        "player": "%s"
                    }
                    """.formatted(this.text, this.playable, this.x, this.y, this.height, this.player);
        }
    }
}
