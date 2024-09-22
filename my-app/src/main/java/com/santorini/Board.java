package com.santorini;


public class Board {
    private final Cell[][] cells;
    private static final int SIDE = 5;

    public Board() {
        cells = new Cell[SIDE][SIDE]; 
        for (int i = 0; i < SIDE ; i++) {
            for (int j = 0; j < SIDE; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}


