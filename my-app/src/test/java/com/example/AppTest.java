package com.example;

import com.santorini.Board;
import com.santorini.Game;
import com.santorini.Player;
import com.santorini.Worker;

import junit.framework.TestCase;

public class AppTest extends TestCase {
    private Game game;
    private Player player1;
    private Player player2;
    private Board board;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        player1 = new Player();
        player2 = new Player();
        game = new Game(player1, player2);
        board = game.getBoard();
        
        // Place workers for player1
        player1.placeWorker(board.getCell(1, 1), board.getCell(1, 2), board);
    }

    public void testSwitchTurn() {
        assertEquals(player1, game.getTurn());

        game.switchTurn();
        assertEquals(player2, game.getTurn());

        game.switchTurn();
        assertEquals(player1, game.getTurn());
    }

    public void testPlayer1WorkerPositionAfterPlacement() {
        Worker worker1 = player1.selectWorker(0);
        assertEquals(board.getCell(1, 1), worker1.getPosition());
        
        Worker worker2 = player1.selectWorker(1);
        assertEquals(board.getCell(1, 2), worker2.getPosition());
    }

    public void testMoveWorker() {
        Worker worker1 = player1.selectWorker(0);
        game.moveWorker(worker1, board.getCell(2, 2));
        assertEquals(board.getCell(2, 2), worker1.getPosition());
    }

    public void testBuildBlock() {
        Worker worker1 = player1.selectWorker(0);
        game.moveWorker(worker1, board.getCell(2, 2)); // Move to (2, 2)
        
        // Attempting a legal build
        worker1.buildBlock(board.getCell(2, 3));
        assertEquals(1, board.getCell(2, 3).getBlock().getHeight());
        
        // Attempting an illegal build
        worker1.buildBlock(board.getCell(2, 4));
        assertEquals(0, board.getCell(2, 4).getBlock().getHeight());
    }

    
    public void testPlayer2PlaceWorkers() {
        player2.placeWorker(board.getCell(3, 1), board.getCell(4, 1), board);
        
        Worker worker3 = player2.selectWorker(0);
        assertEquals(board.getCell(3, 1), worker3.getPosition());
        
        Worker worker4 = player2.selectWorker(1);
        assertEquals(board.getCell(4, 1), worker4.getPosition());
    }
    
    public void testPlayer2MoveWorker() {
        // Setup workers for player2
        player2.placeWorker(board.getCell(3, 1), board.getCell(4, 1), board);
        Worker worker3 = player2.selectWorker(0);
        
        // Move worker
        game.moveWorker(worker3, board.getCell(3, 2));
        assertEquals(board.getCell(3, 2), worker3.getPosition());
    }
    
    public void testPlayer2BuildBlock() {
        // Setup workers for player2
        player2.placeWorker(board.getCell(3, 1), board.getCell(4, 1), board);
        Worker worker3 = player2.selectWorker(0);
        game.moveWorker(worker3, board.getCell(3, 2)); // Move to (3, 2)
    
        // Build block
        worker3.buildBlock(board.getCell(3, 3));
        assertEquals(1, board.getCell(3, 3).getBlock().getHeight());
    }
    
    public void testWinCondition() {
        int x1 = 1, y1 = 2, x2 = 1, y2 = 3; 
        player1.placeWorker(board.getCell(x1, y1), board.getCell(x2, y2), board);
        Worker worker1 = player1.selectWorker(0);
        Worker worker2 = player1.selectWorker(1);

        System.out.println("round 1: " );
        game.moveWorker(worker1, board.getCell(2, 1));
        worker1.buildBlock(board.getCell(2, 2));
        game.moveWorker(worker2, board.getCell(1, 2));
        worker2.buildBlock(board.getCell(2, 3));

        System.out.println("round 2: " );
        game.moveWorker(worker1, board.getCell(2, 2));
        worker1.buildBlock(board.getCell(2, 3));
        game.moveWorker(worker2, board.getCell(2, 1));
        worker2.buildBlock(board.getCell(3, 2));

        System.out.println("round 3: " );
        game.moveWorker(worker1, board.getCell(2, 3));
        worker1.buildBlock(board.getCell(2, 2));
        game.moveWorker(worker2, board.getCell(3, 2));
        worker2.buildBlock(board.getCell(2, 2));

        System.out.println("round 4: " );
        game.moveWorker(worker1, board.getCell(2, 2));
        

        assertTrue(player1.checkWinStatus());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
