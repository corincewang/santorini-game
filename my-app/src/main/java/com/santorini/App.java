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
                        if (placedWorkers >= 2) {
                            responseText = """
                                {
                                    "error": "Player %s has already placed 2 workers."
                                }
                                """.formatted(currentPlayer);
                        } else {
                            // Place the worker
                            this.game.getTurn().placeWorker(this.game.getBoard().getCell(x, y), null, this.game.getBoard());
                            playerWorkerCount.put(currentPlayer, placedWorkers + 1);
                        
                        

                            responseText = """
                                {
                                    "message": "Worker placed at (%d, %d).",
                                    "gameState": %s
                                }
                                """.formatted(x, y, GameState.forGame(this.game).toString());
                        }
                         // If the player placed 2 workers, switch turn
                         if (playerWorkerCount.get(currentPlayer) == 2) {
                            this.game.switchTurn();
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

    

    private void handlePlaceWorker(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Board board = this.game.getBoard();
        currentPlayer.placeWorker(board.getCell(x, y), null, board); 
    }

    private void handleMoveWorker(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Worker worker = currentPlayer.selectWorker(0); // Select the first worker
        Cell targetCell = this.game.getBoard().getCell(x, y);

        if (worker.canMoveToCell(targetCell)) {
            this.game.moveWorker(worker, targetCell);
        } else {
            throw new IllegalArgumentException("Move not allowed to the target cell.");
        }
    }

    private void handleBuildBlock(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Worker worker = currentPlayer.selectWorker(0); // Select the first worker
        Cell targetCell = this.game.getBoard().getCell(x, y);

        if (worker.canBuildToCell(targetCell)) {
            worker.buildBlock(targetCell);
        } else {
            throw new IllegalArgumentException("Cannot build at the target cell.");
        }
    }

    private String buildGameStateResponse(String message, int x, int y) {
        return """
            {
                "message": "%s",
                "gameState": %s
            }
            """.formatted(String.format(message, x, y), GameState.forGame(this.game).toString());
    }
}
