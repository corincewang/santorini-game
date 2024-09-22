package com.example;

import com.santorini.Board;
import com.santorini.Game;
import com.santorini.Player;
import com.santorini.Worker;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

public class AppTest extends TestCase {
    private Game game;
    private Player player1;
    private Player player2;

    protected void setUp() throws Exception {
        super.setUp();
        player1 = new Player();
        player2 = new Player();
        game = new Game(player1, player2);
    }

    public void testSwitchTurn() {
        assertEquals(player1, game.getTurn());

        game.switchTurn();
        assertEquals(player2, game.getTurn());

        game.switchTurn();
        assertEquals(player1, game.getTurn());
    }

    public void testMoveAndBuild() {
        Board board = game.getBoard();

        int x1 = 1, y1 = 2, x2 = 1, y2 = 3; 
        player1.placeWorker(board.getCell(x1, y1), board.getCell(x2, y2), board);
        Worker worker1 = player1.selectWorker(0);
        Worker worker2 = player1.selectWorker(1);
        assertNotNull(worker1);
        assertNotNull(worker2);

        assertEquals(board.getCell(x1, y1), worker1.getPosition());
        assertEquals(board.getCell(x2, y2), worker2.getPosition());

        int moveX = 2;
        int moveY = 2; 

        System.out.println("moved worker1" );
        player1.moveWorker(worker1, board.getCell(moveX, moveY));
        
        assertEquals(board.getCell(moveX, moveY), worker1.getPosition());

        int buildXBad = 2;
        int buildYBad = 4;
        int buildXGood = 2;
        int buildYGood = 3;
        // check illegal build
        worker1.buildBlock(board.getCell(buildXBad, buildYBad));
        System.out.println("worker1 built block: " );
        worker1.buildBlock(board.getCell(buildXGood, buildYGood));
       
        assertEquals(0, board.getCell(buildXBad, buildYBad).getBlock().getHeight());
        assertEquals(1, board.getCell(buildXGood, buildYGood).getBlock().getHeight());

        game.switchTurn();
        // swithch turn to player2 nos
        assertEquals(player2, game.getTurn());


        int x3 = 3, y3 = 1, x4 = 4, y4 = 1; 
        player2.placeWorker(board.getCell(x3, y3), board.getCell(x4, y4), board);
        Worker worker3 = player2.selectWorker(0);
        Worker worker4 = player2.selectWorker(1);
        assertNotNull(worker3);
        assertNotNull(worker4);

        assertEquals(board.getCell(x3, y3), worker3.getPosition());
        assertEquals(board.getCell(x4, y4), worker4.getPosition());
        // System.out.println("Finished placing worker3 and worker4" );

        int moveX2Bad = 4;
        int moveY2Bad = 1; 
        int moveX2Good = 3;
        int moveY2Good = 2;
        // Test a bad move: move on the other worker, should fail
        
        player2.moveWorker(worker3, board.getCell(moveX2Bad, moveY2Bad));
        System.out.println("Worker3 finished bad move" );
        player2.moveWorker(worker3, board.getCell(moveX2Good, moveY2Good));
        System.out.println("Worker3 finished good move" );
        assertEquals(board.getCell(moveX2Good, moveY2Good).getOccupiedWorker(), worker3);
        assertEquals(board.getCell(moveX2Good, moveY2Good), worker3.getPosition());

        int buildXBad2 = 2;
        int buildYBad2 = 2;
        int buildXGood2 = 2;
        int buildYGood2 = 3;
        // check illegal build
        worker3.buildBlock(board.getCell(buildXBad2, buildYBad2));
        worker3.buildBlock(board.getCell(buildXGood2, buildYGood2));
       
        assertEquals(0, board.getCell(buildXBad2, buildYBad2).getBlock().getHeight());
        assertEquals(2, board.getCell(buildXGood2, buildYGood2).getBlock().getHeight());

        game.switchTurn();
        // swithch turn to player1 
        assertEquals(player1, game.getTurn());
    
    }

    public void testWinCondition() {
        Board board = game.getBoard();

        int x1 = 1, y1 = 2, x2 = 1, y2 = 3; 
        player1.placeWorker(board.getCell(x1, y1), board.getCell(x2, y2), board);
        Worker worker1 = player1.selectWorker(0);
        Worker worker2 = player1.selectWorker(1);

        System.out.println("round 1: " );
        player1.moveWorker(worker1, board.getCell(2, 1));
        worker1.buildBlock(board.getCell(2, 2));
        player1.moveWorker(worker2, board.getCell(1, 2));
        worker2.buildBlock(board.getCell(2, 3));

        System.out.println("round 2: " );
        player1.moveWorker(worker1, board.getCell(2, 2));
        worker1.buildBlock(board.getCell(2, 3));
        player1.moveWorker(worker2, board.getCell(2, 1));
        worker2.buildBlock(board.getCell(3, 2));

        System.out.println("round 3: " );
        player1.moveWorker(worker1, board.getCell(2, 3));
        worker1.buildBlock(board.getCell(2, 2));
        player1.moveWorker(worker2, board.getCell(3, 2));
        worker2.buildBlock(board.getCell(2, 2));

        System.out.println("round 4: " );
        player1.moveWorker(worker1, board.getCell(2, 2));
        

        assertTrue(player1.checkWinStatus());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
