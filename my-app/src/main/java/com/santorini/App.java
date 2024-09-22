package com.santorini;



public class App {
    public static void main(String[] args) {
        System.out.println("start");
        Player player1 = new Player();
        Player player2 = new Player();
        Game game = new Game(player1, player2);

        Player currentPlayer = game.getTurn(); 
        Board board = game.getBoard();
        currentPlayer.placeWorker(board.getCell(1, 2), board.getCell(1, 1), board);

        Worker worker = currentPlayer.selectWorker(0);
        // System.out.println("null in APP");
        // System.out.println(worker == null);

        currentPlayer.moveWorker(worker, board.getCell(1, 0));

        worker.buildBlock(board.getCell(1, 1));


        if (currentPlayer.checkWinStatus()) {
            System.out.println(currentPlayer + " wins!");
        }

        game.switchTurn();
    }
}

