package com.santorini;

public interface GameEventListener {
    void onMove(Player player, Worker worker, Cell from, Cell to);
    void onBuild(Player player, Worker worker, Cell cell);
    void onCheckWin(Player player);
}
