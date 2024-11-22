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

    public App() throws IOException {
        super(SITE_NUM);

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        this.game = new Game(player1, player2);

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
            responseText = handleNewGame();
        } else if (uri.equals("/play")) {
            responseText = handlePlayAction(params);
        } else {
            responseText = """
                {
                    "error": "Invalid endpoint. Available endpoints: /newgame, /play."
                }
                """;
        }

        return newFixedLengthResponse(Response.Status.OK, "application/json", responseText);
    }

    private String handleNewGame() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        this.game = new Game(player1, player2);

        playerWorkerCount.put(player1, 0);
        playerWorkerCount.put(player2, 0);

        GameState gameState = GameState.forGame(this.game);
        return """
            {
                "message": "New game started with an empty board.",
                "gameState": %s
            }
            """.formatted(gameState.toString());
    }

    private String handlePlayAction(Map<String, String> params) {
        if (!params.containsKey("action")) {
            return """
                {
                    "error": "Missing action parameter. Supported actions: place, chooseWorker, move, build."
                }
                """;
        }
    
        String action = params.get("action");
        int x = Integer.parseInt(params.getOrDefault("x", "-1"));
        int y = Integer.parseInt(params.getOrDefault("y", "-1"));
    
        switch (action) {
            case "place":
                return handlePlaceAction(x, y);
            case "chooseWorker":
                return handleChooseWorkerAction(x, y);
            case "move":
                return handleMoveAction(x, y);
            case "build":
                return handleBuildAction(x, y);
            default:
                return String.format("""
                    {
                        "error": "Invalid action: '%s'. Supported actions: place, chooseWorker, move, build."
                    }
                    """, action);
        }
    }
    

    private String handlePlaceAction(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        int placedWorkers = playerWorkerCount.getOrDefault(currentPlayer, 0);

        if (placedWorkers < 2) {
            this.game.getTurn().placeWorker(this.game.getBoard().getCell(x, y), null, this.game.getBoard());
            placedWorkers++;
            playerWorkerCount.put(currentPlayer, placedWorkers);

            if (placedWorkers == 2 && currentPlayer.equals(this.game.getPlayers()[0])) {
                this.game.switchTurn();
                return """
                    {
                        "message": "Player 1 has placed all workers. Now it's Player 2's turn.",
                        "gameState": %s
                    }
                    """.formatted(GameState.forGame(this.game).toString());
            } else if (placedWorkers == 2 && currentPlayer.equals(this.game.getPlayers()[1])) {
                this.game.switchTurn();
                this.game.setCurrentAction("chooseWorker");
                return """
                    {
                        "message": "All workers placed by both players. Moving to 'chooseWorker' phase.",
                        "gameState": %s
                    }
                    """.formatted(GameState.forGame(this.game).toString());
            } else {
                return """
                    {
                        "message": "Worker placed at (%d, %d) by %s.",
                        "gameState": %s
                    }
                    """.formatted(x, y, currentPlayer, GameState.forGame(this.game).toString());
            }
        } else {
            return """
                {
                    "error": "No more workers to place for this player."
                }
                """;
        }
    }

    private String handleChooseWorkerAction(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Cell selectedCell = this.game.getBoard().getCell(x, y);
        Worker worker = selectedCell.getOccupiedWorker();
    
        if (worker != null && worker.getPlayer().equals(currentPlayer)) {
            selectedWorkers.put(currentPlayer, worker);
            return String.format("""
                {
                    "message": "Worker at (%d, %d) selected successfully.",
                    "gameState": %s
                }
                """, x, y, GameState.forGame(this.game).toString());
        } else {
            return String.format("""
                {
                    "error": "No worker at (%d, %d) belongs to you or no worker present."
                }
                """, x, y);
        }
    }
    

    private String handleMoveAction(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Worker selectedWorker = selectedWorkers.get(currentPlayer);

        if (selectedWorker != null) {
            Cell targetCell = this.game.getBoard().getCell(x, y);
            if (selectedWorker.canMoveToCell(targetCell)) {
                this.game.moveWorker(selectedWorker, targetCell);
                    // System.out.println("Worker selected in move: " + selectedWorkers);

                if (currentPlayer.checkWinStatus()) {
                    return """
                        {
                            "message": "Worker moved to (%d, %d). Player %s wins!",
                            "gameState": %s
                        }
                        """.formatted(x, y, currentPlayer, GameState.forGame(this.game).toString());
                } else {
       
                    return """
                        {
                            "message": "Worker moved to (%d, %d).",
                            "gameState": %s
                        }
                        """.formatted(x, y, GameState.forGame(this.game).toString());
                        
                }
            } else {
                return """
                    {
                        "error": "Invalid move. Worker cannot move to the target cell."
                    }
                    """;
            }
        } else {
            return """
                {
                    "error": "No worker selected. Please select a worker first."
                }
                """;
        }
    }

    private String handleBuildAction(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Worker selectedWorker = selectedWorkers.get(currentPlayer);
        // System.out.println("Worker selected in build: " + selectedWorkers);  
    
        if (selectedWorker == null) {
            return """
                {
                    "error": "No worker selected. Please select a worker first."
                }
                """;
        }
    
        Cell targetCell = this.game.getBoard().getCell(x, y);
    
        if (selectedWorker.canBuildToCell(targetCell)) {
            selectedWorker.buildBlock(targetCell);
            return String.format("""
                {
                    "message": "Worker built on cell (%d, %d).",
                    "gameState": %s
                }
                """, x, y, GameState.forGame(this.game).toString());
        } else {
            return """
                {
                    "error": "Invalid build. Cannot build on the target cell."
                }
                """;
        }
    }
    
}
