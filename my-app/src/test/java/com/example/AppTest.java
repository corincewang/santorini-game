package com.example;

import org.junit.Test;

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
        
        game = new Game(player1, player2);
        board = game.getBoard();
        // Place workers for both players
        player1.placeWorker(board.getCell(1, 1), board.getCell(1, 2), board);
        player2.placeWorker(board.getCell(4, 4), board.getCell(4, 3), board);
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
        player2.setGodCard(null);
        // Setup workers for player2
        player2.placeWorker(board.getCell(3, 1), board.getCell(4, 1), board);
        Worker worker3 = player2.selectWorker(0);
        game.moveWorker(worker3, board.getCell(3, 2)); // Move to (3, 2)
    
        // Build block
        worker3.buildBlock(board.getCell(2, 1));
        assertEquals(1, board.getCell(2, 1).getBlock().getHeight());
    }
    
    @Test
    public void testWinConditionWithoutGodCards() {
        // No god cards assigned
        player1.setGodCard(null);
        player2.setGodCard(null);
    
        // Place workers on the board
        player1.placeWorker(board.getCell(1, 1), board.getCell(1, 2), board);
        player2.placeWorker(board.getCell(4, 4), board.getCell(4, 3), board);
    
        Worker player1Worker = player1.selectWorker(0);
        Worker player2Worker = player2.selectWorker(0);
    
        // Player1's turn: Move and build
        game.moveWorker(player1Worker, board.getCell(2, 1));
        player1Worker.buildBlock(board.getCell(2, 2));
    
        // Player2's turn: Move and build
        game.moveWorker(player2Worker, board.getCell(3, 4));
        player2Worker.buildBlock(board.getCell(3, 3));
    
        // Player1's turn: Move and build
        game.moveWorker(player1Worker, board.getCell(2, 2));
        player1Worker.buildBlock(board.getCell(2, 3));
    
        // Player2's turn: Move and build
        game.moveWorker(player2Worker, board.getCell(3, 3));
        player2Worker.buildBlock(board.getCell(2, 3));
    
        // Player1's turn: Prepare for the win
        game.moveWorker(player1Worker, board.getCell(2, 3));
        board.getCell(2, 2).getBlock().buildBlock(); // Level 2
        board.getCell(2, 2).getBlock().buildBlock(); // Level 3
    
        // Player2's turn: Normal move and build
        Worker player2Worker2 = player2.selectWorker(1);
        game.moveWorker(player2Worker2, board.getCell(4, 3));
        player2Worker2.buildBlock(board.getCell(4, 2));
    
        // Player1's turn: Move to level 3 and win
        game.moveWorker(player1Worker, board.getCell(2, 2));
    
        // Check win condition
        assertTrue(player1.checkWinStatus());
        assertEquals(player1, game.getWinner());
    }
    


    @Test
    public void testDemeterDoubleBuild() {
        player1.setGodCard(new Demeter());
        Worker worker = player1.selectWorker(0);
        game.moveWorker(worker, board.getCell(2, 2)); // Move to an adjacent position.
        worker.buildBlock(board.getCell(2, 3)); // First build.
        assertEquals(1, board.getCell(2, 3).getBlock().getHeight());

        worker.buildBlock(board.getCell(2, 3)); // Second build at a different cell.
        assertEquals(1, board.getCell(2, 3).getBlock().getHeight());

        worker.buildBlock(board.getCell(3, 3)); // Second build at a different cell.
        assertEquals(1, board.getCell(3, 3).getBlock().getHeight());
    }
    
    @Test
    public void testHephaestusDoubleBuild() {
        player2.setGodCard(new Hephaestus());
        Worker worker = player2.selectWorker(0);
        game.moveWorker(worker, board.getCell(3, 3)); // Assume moving to an adjacent position is valid.
        worker.buildBlock(board.getCell(3, 4)); // First build.
        worker.buildBlock(board.getCell(3, 4)); // Second build by Hephaestus.
    
        assertEquals(2, board.getCell(3, 4).getBlock().getHeight());
    }
    
    @Test
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

    @Test
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
    
    @Test
    public void testHephaestusDemeterWinCondition() {
        player1.setGodCard(new Hephaestus());
        player2.setGodCard(new Demeter());

        player1.placeWorker(board.getCell(1, 1), board.getCell(1, 2), board);
        player2.placeWorker(board.getCell(4, 4), board.getCell(4, 3), board);

        Worker player1Worker = player1.selectWorker(0);
        Worker player2Worker = player2.selectWorker(0);

        game.moveWorker(player1Worker, board.getCell(2, 2));
        player1Worker.buildBlock(board.getCell(2, 3));
        player1Worker.buildBlock(board.getCell(2, 3));

        game.moveWorker(player2Worker, board.getCell(3, 3));
        player2Worker.buildBlock(board.getCell(3, 2));
        player2Worker.buildBlock(board.getCell(3, 4));

        game.moveWorker(player1Worker, board.getCell(3, 2));
        board.getCell(2, 2).getBlock().buildBlock();
        board.getCell(2, 2).getBlock().buildBlock();

        Worker player2Worker2 = player2.selectWorker(1);
        game.moveWorker(player2Worker2, board.getCell(4, 4));
        player2Worker2.buildBlock(board.getCell(4, 3));
        player2Worker2.buildBlock(board.getCell(4, 3));

        game.moveWorker(player1Worker, board.getCell(2, 2));
        player1Worker.buildBlock(board.getCell(2, 3));

        game.moveWorker(player2Worker2, board.getCell(3, 4));
        player2Worker2.buildBlock(board.getCell(3, 3));
        player2Worker2.buildBlock(board.getCell(3, 3));

        game.moveWorker(player1Worker, board.getCell(2, 3));

        assertTrue(player1.checkWinStatus());
        assertEquals(player1, game.getWinner());
    }

    @Test
    public void testMinotaurPanWinCondition() {
        player1.setGodCard(new Minotaur());
        player2.setGodCard(new Pan());
    
        player1.placeWorker(board.getCell(1, 1), board.getCell(0, 0), board);
        player2.placeWorker(board.getCell(2, 2), board.getCell(4, 3), board);
    
        Worker player1Worker = player1.selectWorker(0);
        Worker player2Worker = player2.selectWorker(0);
    
        System.out.println("godcard: " + player2.getGodCard());
    
        // Player1's turn: Move and push with Minotaur
        game.moveWorker(player1Worker, board.getCell(2, 2));
        player1Worker.buildBlock(board.getCell(2, 3));
    
        // Player2's turn: Move and build
        game.moveWorker(player2Worker, board.getCell(2, 3));
        player2Worker.buildBlock(board.getCell(1, 3));
    
        // Player1's turn: Move and build
        game.moveWorker(player1Worker, board.getCell(1, 2));
        player1Worker.buildBlock(board.getCell(1, 3));
    
        // Player2's turn: Move and build
        game.moveWorker(player2Worker, board.getCell(1, 3));
        player2Worker.buildBlock(board.getCell(1, 4));
    
        game.moveWorker(player1Worker, board.getCell(0, 2));
        player1Worker.buildBlock(board.getCell(0,3));
    
        game.moveWorker(player2Worker, board.getCell(0, 4));

        assertTrue(player2.checkWinStatus());
        assertEquals(player2, game.getWinner());
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
}
