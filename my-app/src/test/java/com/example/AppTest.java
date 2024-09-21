// package com.example;

// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// /**
//  * Unit test for simple App.
//  */
// public class AppTest 
//     extends TestCase
// {
//     /**
//      * Create the test case
//      *
//      * @param testName name of the test case
//      */
//     public AppTest( String testName )
//     {
//         super( testName );
//     }

//     /**
//      * @return the suite of tests being tested
//      */
//     public static Test suite()
//     {
//         return new TestSuite( AppTest.class );
//     }

//     /**
//      * Rigourous Test :-)
//      */
//     public void testApp()
//     {
//         assertTrue( true );
//     }
// }
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

    public void testPlaceAndMoveWorker() {
        Board board = game.getBoard();

        // 玩家1放置工人
        int x1 = 1, y1 = 2, x2 = 1, y2 = 3; // 硬编码的坐标
        player1.placeWorker(board.getCell(x1, y1), board.getCell(x2, y2), board);
        Worker worker = player1.selectWorker(0);
        assertNotNull(worker);

        // 玩家1移动工人
        int moveX = 2, moveY = 2; // 硬编码的坐标
        player1.moveWorker(worker, board.getCell(moveX, moveY));
        assertEquals(board.getCell(moveX, moveY), worker.getPosition());

        // 玩家1建造Block
        int buildX = 2, buildY = 2; // 硬编码的坐标
        worker.buildBlock(board.getCell(buildX, buildY));
        assertEquals(1, board.getCell(buildX, buildY).getBlock().getHeight());

        // 切换到玩家2
        game.switchTurn();
        assertEquals(player2, game.getTurn());
    }

    public void testWinCondition() {
        Board board = game.getBoard();

        // 玩家1放置工人
        int x1 = 1, y1 = 2, x2 = 1, y2 = 3; // 硬编码的坐标
        player1.placeWorker(board.getCell(x1, y1), board.getCell(x2, y2), board);
        Worker worker = player1.selectWorker(0);

        // 玩家1移动工人
        int moveX = 2, moveY = 2; // 硬编码的坐标
        player1.moveWorker(worker, board.getCell(moveX, moveY));

        // 建造到达胜利的块
        for (int i = 0; i < 3; i++) { // 硬编码建造三层
            worker.buildBlock(board.getCell(moveX, moveY));
        }

        assertTrue(player1.checkWinStatus());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
