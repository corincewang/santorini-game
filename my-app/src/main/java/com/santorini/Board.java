package com.santorini;


public class Board {
    private Cell[][] cells;

    public Board() {
        cells = new Cell[5][5];  // Santorini 是 5x5 的游戏板
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}
