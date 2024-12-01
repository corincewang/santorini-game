package com.example;

import com.santorini.Board;
import com.santorini.Cell;
import com.santorini.Demeter;
import com.santorini.Game;
import com.santorini.Hephaestus;
import com.santorini.Minotaur;
import com.santorini.Pan;
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
        player1 = new Player("PlayerA");
        player2 = new Player("PlayerB");
        player1.setGodCard(new Demeter());
        player2.setGodCard(new Hephaestus()); // You can switch this to test different cards.
        game = new Game(player1, player2);
        board = game.getBoard();
        
        // Place workers for both players
        player1.placeWorker(board.getCell(1, 1), board.getCell(1, 2), board);
        player2.placeWorker(board.getCell(4, 4), board.getCell(4, 3), board);
    }


    public void testDemeterDoubleBuild() {
        Worker worker = player1.selectWorker(0);
        game.moveWorker(worker, board.getCell(2, 2)); // Move to an adjacent position.
        worker.buildBlock(board.getCell(2, 3)); // First build.
        assertEquals(1, board.getCell(2, 3).getBlock().getHeight());

        worker.buildBlock(board.getCell(2, 3)); // Second build at a different cell.
        assertEquals(1, board.getCell(2, 3).getBlock().getHeight());

        worker.buildBlock(board.getCell(3, 3)); // Second build at a different cell.
        assertEquals(1, board.getCell(3, 3).getBlock().getHeight());
    }
    

    public void testHephaestusDoubleBuild() {
        Worker worker = player2.selectWorker(0);
        game.moveWorker(worker, board.getCell(3, 3)); // Assume moving to an adjacent position is valid.
        worker.buildBlock(board.getCell(3, 4)); // First build.
        worker.buildBlock(board.getCell(3, 4)); // Second build by Hephaestus.
    
        assertEquals(2, board.getCell(3, 4).getBlock().getHeight());
    }
    

    public void testPanSpecialWinCondition() {
        player1.setGodCard(new Pan());
        Worker worker = player1.selectWorker(0);
        
        // Simulating a situation where the worker moves down from a higher to a lower level.
        board.getCell(1, 1).getBlock().buildBlock(); // Level 1
        board.getCell(1, 1).getBlock().buildBlock(); // Level 2
        worker.setPosition(board.getCell(1, 1));

        game.moveWorker(worker, board.getCell(2, 1)); // Assume (2, 1) is at level 0.
        
        assertTrue(player1.checkWinStatus()); // Pan wins by moving down two levels.
    }


    public void testMinotaurPushWorker() {
        player2.setGodCard(new Minotaur());
    
        // Position Player1's worker at (3, 4), directly in front of Player2's worker at (4, 4)
        player1.placeWorker(board.getCell(3, 4), board.getCell(3, 3), board);
        Worker player2Worker = player2.selectWorker(0);
    
        // The target cell where the push will happen
        Cell pushTarget = board.getCell(3, 4);
        // Expected destination for Player1's worker after being pushed (into the next cell in line)
        Cell pushedDestination = board.getCell(2, 4);  // Cell directly ahead when pushing from (3, 4) to (2, 4)
    
        // Perform the move using Minotaur's special ability
        game.moveWorker(player2Worker, pushTarget);
    
        // Check positions after the move
        assertEquals("Player2's worker should now be at the pushed cell (3, 4)", pushTarget, player2Worker.getPosition());
        assertSame("Player2's worker should occupy the push target (3, 4)", player2Worker, pushTarget.getOccupiedWorker());
        assertNull("Original position of Player2's worker (4, 4) should be empty", board.getCell(4, 4).getOccupiedWorker());
        assertEquals("Player1's worker should be in the pushed position (2, 4)", player1.getWorkers()[0], pushedDestination.getOccupiedWorker());
       
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
