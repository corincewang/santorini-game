package com.santorini;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {
    private static final int SITE_NUM = 8080;
    private Game game;
    private final Map<Player, Integer> playerWorkerCount = new HashMap<>();

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    /**
     * Constructor: Initializes the server and starts it on port 8080.
     *
     * @throws IOException if the server fails to start
     */
    public App() throws IOException {
        super(SITE_NUM);

        Player player1 = new Player();
        Player player2 = new Player();
        this.game = new Game(player1, player2);

        // Initialize worker counts for both players
        playerWorkerCount.put(player1, 0);
        playerWorkerCount.put(player2, 0);

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nSantorini Game Server Running on port 8080!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        String responseText = "";

     

        if (uri.equals("/newgame")) {
            // Initialize a new game
            Player player1 = new Player();
            Player player2 = new Player();
            this.game = new Game(player1, player2);

            // Reset worker counts
            playerWorkerCount.put(player1, 0);
            playerWorkerCount.put(player2, 0);

            // Generate the game state
            GameState gameState = GameState.forGame(this.game);
            responseText = """
                {
                    "message": "New game started with an empty board.",
                    "gameState": %s
                }
                """.formatted(gameState.toString());
        } 
        else if (uri.equals("/play")) {
              
           
            if (params.containsKey("action")) {
                String action = params.get("action");
                try {
                    int x = Integer.parseInt(params.getOrDefault("x", "-1"));
                    int y = Integer.parseInt(params.getOrDefault("y", "-1"));
                    
                   
                    if ("place".equals(action)) {
                        

                        Player currentPlayer = this.game.getTurn();
                        // Check if the current player has placed less than 2 workers
                        int placedWorkers = playerWorkerCount.getOrDefault(currentPlayer, 0);
                      
                        // Place the worker
                        this.game.getTurn().placeWorker(this.game.getBoard().getCell(x, y), null, this.game.getBoard());
                        playerWorkerCount.put(currentPlayer, placedWorkers + 1);

                        responseText = """
                            {
                                "message": "Worker placed at (%d, %d).",
                                "gameState": %s
                            }
                            """.formatted(x, y, GameState.forGame(this.game).toString());
                    
                         // If the player placed 2 workers, switch turn
                        if (playerWorkerCount.get(currentPlayer) == 2) {
                            this.game.switchTurn();
                        }
                        if (playerWorkerCount.get(this.game.getPlayers()[0]) >= 2 && playerWorkerCount.get(this.game.getPlayers()[1]) >= 2) {
                            this.game.setCurrentAction("move");
                            System.out.println("move");
                            responseText = """
                            {
                                "message": "All workers placed. Moving to 'move' phase.",
                                "gameState": %s
                            }
                            """.formatted(GameState.forGame(this.game).toString());
                        }   
                      
                    } 
                    else if ("move".equals(action)) {
                        try {
                            Player currentPlayer = this.game.getTurn();
                            Worker worker = currentPlayer.selectWorker(0); // 假设始终选择第一个工人
                            Cell targetCell = this.game.getBoard().getCell(x, y);
                    
                            if (worker.canMoveToCell(targetCell)) {
                                this.game.moveWorker(worker, targetCell);

                                if (currentPlayer.checkWinStatus()) {
                                    responseText = """
                                        {
                                            "message": "Worker moved to (%d, %d). Player %s wins!",
                                            "gameState": %s
                                        }
                                        """.formatted(x, y, currentPlayer.toString(), GameState.forGame(this.game).toString());
                                    this.game.setEndState(); // 结束游戏
                                } else {
                                    // 如果没有胜利，切换到下一个玩家的回合
                                    this.game.switchTurn();
                                    responseText = """
                                        {
                                            "message": "Worker moved to (%d, %d).",
                                            "gameState": %s
                                        }
                                        """.formatted(x, y, GameState.forGame(this.game).toString());
                                }
                            } else {
                                responseText = """
                                    {
                                        "error": "Invalid move. Worker cannot move to the target cell."
                                    }
                                    """;
                            }
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            responseText = """
                                {
                                    "error": "Invalid or missing coordinates for move action."
                                }
                                """;
                        }
                    }
                    
                    else {
                        responseText = """
                            {
                                "error": "Invalid action: '%s'. Supported action: place."
                            }
                            """.formatted(action);
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    responseText = """
                        {
                            "error": "Invalid or missing coordinates."
                        }
                        """;
                }
            } else {
                responseText = """
                    {
                        "error": "Missing action parameter. Supported action: place."
                    }
                    """;
            }
        } else {
            responseText = """
                {
                    "error": "Invalid endpoint. Available endpoints: /newgame, /play."
                }
                """;
        }

        return newFixedLengthResponse(Response.Status.OK, "application/json", responseText);
    }


}
