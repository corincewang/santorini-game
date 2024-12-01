package com.santorini;

/**
 * The Board class represents the 5x5 game board for the Santorini game. 
 * It contains a grid of cells that make up the playable area, where workers can be placed and blocks can be built.
 */
public class Board {
    private final Cell[][] cells;
    private static final int SIDE = 5;

     /**
     * Constructs a 5x5 game board by initializing each cell with its respective coordinates.
     */
    public Board() {
        cells = new Cell[SIDE][SIDE]; 
        for (int i = 0; i < SIDE ; i++) {
            for (int j = 0; j < SIDE; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Retrieves the Cell object located at the specified coordinates on the board.
     *
     * @param x the x-coordinate of the desired cell
     * @param y the y-coordinate of the desired cell
     * @return the Cell at the specified (x, y) position on the board
     */
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    // Board.java
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < SIDE && y >= 0 && y < SIDE;
    }


}


