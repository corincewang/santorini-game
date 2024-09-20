package com.santorini;


public class SantoriniGame {
    public static void main(String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();
        Game game = new Game(player1, player2);

        // 示例游戏流程
        Player currentPlayer = game.getTurn();
        Worker worker = currentPlayer.getWorkers()[0];
        Board board = game.getBoard();

        // 玩家1将worker移动到(1, 1)处
        currentPlayer.moveWorker(worker, board.getCell(1, 1));

        // 玩家1在(1, 1)处建造一个Tower
        currentPlayer.buildTower(worker, board.getCell(1, 1));

        // 检查游戏是否获胜
        if (game.checkWin(currentPlayer)) {
            System.out.println("Player 1 wins!");
            game.endGame();
        }

        // 切换到玩家2的回合
        game.switchTurn();
    }
}

