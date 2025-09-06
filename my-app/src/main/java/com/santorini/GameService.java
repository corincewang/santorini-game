package com.santorini;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {
    private Game game;
    private final Map<Player, Integer> playerWorkerCount = new HashMap<>();
    private Map<Player, Worker> selectedWorkers = new HashMap<>();

    public String handleNewGame() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        this.game = new Game(player1, player2);

        playerWorkerCount.put(player1, 0);
        playerWorkerCount.put(player2, 0);

        GameState gameState = GameState.forGame(this.game);
        return String.format("""
            {
                "message": "New game started with an empty board.",
                "gameState": %s
            }
            """, gameState.toString());
    }

    public String handlePlayAction(Map<String, String> params) {
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
            case "selectGodCard":
                return handleSelectGodCard(params); 
            case "pass":
                return handlePassAction();
            default:
                return String.format("""
                    {
                        "error": "Invalid action: '%s'. Supported actions: place, chooseWorker, move, build."
                    }
                    """, action);
        }
    }

    // Copy all the private methods from App.java here
    // I'll add a few key ones as examples:

    private String handleSelectGodCard(Map<String, String> params) {
        if (!params.containsKey("player") || !params.containsKey("godCard")) {
            return """
                {
                    "error": "Missing parameters. Both 'player' and 'godCard' are required."
                }
                """;
        }

        String playerName = params.get("player");
        String godCardName = params.get("godCard");

        Player player = null;
        for (Player p : this.game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                player = p;
                break;
            }
        }

        if (player == null) {
            return """
                {
                    "error": "Player not found."
                }
                """;
        }

        try {
            GodCard godCard = GodCardFactory.getGodCard(godCardName);
            player.setGodCard(godCard);
        } catch (IllegalArgumentException e) {
            return String.format("""
                {
                    "error": "Invalid god card selection: '%s'"
                }
                """, godCardName);
        }

        this.game.setGodCards(playerName, godCardName);

        return String.format("""
            {
                "message": "God card '%s' selected successfully for player '%s'.",
                "gameState": %s
            }
            """, godCardName, playerName, GameState.forGame(this.game).toString());
    }

    private String handlePlaceAction(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        int workerCount = playerWorkerCount.getOrDefault(currentPlayer, 0);

        if (workerCount < 2) {
            Cell targetCell = this.game.getBoard().getCell(x, y);
            if (targetCell != null && !targetCell.isOccupied()) {
                Worker worker = new Worker(targetCell, this.game.getBoard(), currentPlayer);
                targetCell.setOccupiedWorker(worker);
                
                playerWorkerCount.put(currentPlayer, workerCount + 1);
                if (workerCount + 1 == 2) {
                    this.game.switchTurn();
                    if (currentPlayer.equals(this.game.getPlayers()[0])) {
                        return String.format("""
                            {
                                "message": "Player 1 has placed all workers. Now it's Player 2's turn.",
                                "gameState": %s
                            }
                            """, GameState.forGame(this.game).toString());
                    } else {
                        this.game.setCurrentAction("chooseWorker");
                        return String.format("""
                            {
                                "message": "All workers placed by both players. Moving to 'chooseWorker' phase.",
                                "gameState": %s
                            }
                            """, GameState.forGame(this.game).toString());
                    }
                } else {
                    return String.format("""
                        {
                            "message": "Worker placed at (%d, %d) by %s.",
                            "gameState": %s
                        }
                        """, x, y, currentPlayer.getName(), GameState.forGame(this.game).toString());
                }
            } else {
                return """
                    {
                        "error": "Cannot place worker at this location."
                    }
                    """;
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
        Worker worker = this.game.getBoard().getCell(x, y).getOccupiedWorker();
        
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
                this.game.setCurrentAction("build");
                
                if (currentPlayer.checkWinStatus()) {
                    this.game.setEndState();
                    System.out.println("game ends, endState set!");
                    return String.format("""
                        {
                            "message": "Worker moved to (%d, %d).",
                            "gameState": %s
                        }
                        """, x, y, GameState.forGame(this.game).toString());
                } else {
                    return String.format("""
                        {
                            "message": "Worker moved to (%d, %d).",
                            "gameState": %s
                        }
                        """, x, y, GameState.forGame(this.game).toString());
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
                    "error": "In Move, No worker selected. Please select a worker first."
                }
                """;
        }
    }

    private String handleBuildAction(int x, int y) {
        Player currentPlayer = this.game.getTurn();
        Worker selectedWorker = selectedWorkers.get(currentPlayer);
        
        if (selectedWorker == null) {
            return """
                {
                    "error": "In Build, No worker selected. Please select a worker first."
                }
                """;
        }
    
        Cell targetCell = this.game.getBoard().getCell(x, y);
        if (selectedWorker.canBuildToCell(targetCell)) {
            selectedWorker.buildBlock(targetCell);
            
            GodCard godCard = currentPlayer.getGodCard();
            if (godCard != null && godCard.allowsExtraBuild() && !this.game.isAwaitingExtraBuild()) {
                this.game.setAwaitingExtraBuild(true);
                return String.format("""
                    {
                        "message": "First build complete. You may perform an extra build.",
                        "gameState": %s
                    }
                    """, GameState.forGame(this.game).toString());
            }
    
            selectedWorkers.remove(currentPlayer);
            this.game.setCurrentAction("chooseWorker");
            this.game.switchTurn();
            this.game.setAwaitingExtraBuild(false);

            return String.format("""
                {
                    "message": "Worker built on cell (%d, %d). Switch Turn to next player!",
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

    private String handlePassAction() {
        if (!this.game.isAwaitingExtraBuild()) {
            return """
                {
                    "error": "No extra build to skip."
                }
                """;
        }
    
        Player currentPlayer = this.game.getTurn();
        selectedWorkers.remove(currentPlayer);
        this.game.setAwaitingExtraBuild(false);
        this.game.switchTurn();
        this.game.setCurrentAction("chooseWorker");
    
        return String.format("""
            {
                "message": "Extra build skipped. Turn switched to the next player.",
                "gameState": %s
            }
            """, GameState.forGame(this.game).toString());
    }
}
