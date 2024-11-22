package com.santorini;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {
    private static final int SITE_NUM = 8080;
    private Game game;
    private final Map<Player, Integer> playerWorkerCount = new HashMap<>();
    private Map<Player, Worker> selectedWorkers = new HashMap<>();


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

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
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
            Player player1 = new Player("Player1");
            Player player2 = new Player("Player2");
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
                        int placedWorkers = playerWorkerCount.getOrDefault(currentPlayer, 0);
        
                        if (placedWorkers < 2) {
                            this.game.getTurn().placeWorker(this.game.getBoard().getCell(x, y), null,  this.game.getBoard());
                            placedWorkers++;
                            playerWorkerCount.put(currentPlayer, placedWorkers);
        
                            if (placedWorkers == 2 && currentPlayer.equals(this.game.getPlayers()[0])) {
                                this.game.switchTurn(); // Switch to Player 2 after Player 1 places two workers
                                responseText = String.format("""
                                    {
                                        "message": "Player 1 has placed all workers. Now it's Player 2's turn.",
                                        "gameState": %s
                                    }
                                    """, GameState.forGame(this.game).toString());
                            } else if (placedWorkers == 2 && currentPlayer.equals(this.game.getPlayers()[1])) {
                                this.game.switchTurn();
                                this.game.setCurrentAction("chooseWorker"); // Move to the next phase after Player 2 places their workers
                                responseText = String.format("""
                                    {
                                        "message": "All workers placed by both players. Moving to 'move' phase.",
                                        "gameState": %s
                                    }
                                    """, GameState.forGame(this.game).toString());
                            } else {
                                responseText = String.format("""
                                    {
                                        "message": "Worker placed at (%d, %d) by %s.",
                                        "gameState": %s
                                    }
                                    """, x, y, currentPlayer, GameState.forGame(this.game).toString());
                            }
                        } else {
                            responseText = """
                                {
                                    "error": "No more workers to place for this player."
                                }
                                """;
                        }
                    } 
                    
                    else if ("chooseWorker".equals(action)) {
                        try {
                            Player currentPlayer = this.game.getTurn();                      
                            Cell selectedCell = this.game.getBoard().getCell(x, y);
                            Worker worker = selectedCell.getOccupiedWorker();
                            if (worker != null && worker.getPlayer().equals(currentPlayer)) {
                                selectedWorkers.put(currentPlayer, worker);
                                System.out.println(selectedWorkers);
                                responseText = String.format("""
                                    {
                                        "message": "Worker at (%d, %d) selected successfully.",
                                        "gameState": %s
                                    }
                                    """, x, y, GameState.forGame(this.game).toString());
                            } else {
                                responseText = String.format("""
                                    {
                                        "error": "No worker at (%d, %d) belongs to you or no worker present."
                                    }
                                    """, x, y);
                            }
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            responseText = """
                                {
                                    "error": "Invalid or missing coordinates for choosing worker."
                                }
                                """;
                        }
                    }
                    
                
                    else if ("move".equals(action)) {
                        try {
                            Worker selectedWorker = selectedWorkers.get(this.game.getTurn());
                            Player currentPlayer = this.game.getTurn();
                            // System.out.println("selectedWorker");
                            // System.out.println(selectedWorker);
                            
                            if (selectedWorker != null) {
                                Cell targetCell = this.game.getBoard().getCell(x, y);
                                System.out.println("canGet Target Cell");
                                if (selectedWorker.canMoveToCell(targetCell)) {
                                    System.out.println("canMove");
                                    this.game.moveWorker(selectedWorker, targetCell);
                                    selectedWorkers.remove(this.game.getTurn()); 
                                    if (currentPlayer.checkWinStatus()) {
                                        responseText = """
                                            {
                                                "message": "Worker moved to (%d, %d). Player %s wins!",
                                                "gameState": %s
                                            }
                                            """.formatted(x, y, currentPlayer.toString(), GameState.forGame(this.game).toString());
                                        this.game.setEndState();
                                    } else {
                                        this.game.switchTurn();
                                        responseText = """
                                            {
                                                "message": "Worker moved to (%d, %d).",
                                                "gameState": %s
                                            }
                                            """.formatted(x, y, GameState.forGame(this.game).toString());
                                    }
                                } 
                                else {
                                    responseText = """
                                        {
                                            "error": "Invalid move. Worker cannot move to the target cell."
                                        }
                                        """;
                                }
                        }
                        else {
                            responseText = """
                                {
                                    "error": "No worker selected. Please select a worker first."
                                }
                                """;
                        }
                     } 
                     catch (NumberFormatException | IndexOutOfBoundsException e) {
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
