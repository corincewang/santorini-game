package com.santorini;
import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {
    private static final int SITE_NUM = 8080;
    private Game game;

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

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nSantorini Game Server Running on port 8080!\n");
    }

 @Override
public Response serve(IHTTPSession session) {
    String uri = session.getUri();
    Map<String, String> params = session.getParms();
    String responseText = "";

    if (uri.equals("/newgame")) {
        // e.g., /newgame?x1=0&y1=0&x2=1&y2=0
        if (params.containsKey("x1") && params.containsKey("y1") &&
            params.containsKey("x2") && params.containsKey("y2")) {
            try {
                int x1 = Integer.parseInt(params.get("x1"));
                int y1 = Integer.parseInt(params.get("y1"));
                int x2 = Integer.parseInt(params.get("x2"));
                int y2 = Integer.parseInt(params.get("y2"));

                // Initialize a new game with two players
                Player player1 = new Player();
                Player player2 = new Player();
                this.game = new Game(player1, player2);

                // Place workers for the first player
                this.game.getTurn().placeWorker(
                    this.game.getBoard().getCell(x1, y1),
                    this.game.getBoard().getCell(x2, y2),
                    this.game.getBoard()
                );

                responseText = "New game started with workers placed at (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ")";
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                responseText = "Error: Invalid coordinates provided for new game!";
            }
        } else {
            responseText = "Missing parameters! Example: /newgame?x1=0&y1=0&x2=1&y2=0";
        }
    } else if (uri.equals("/play")) {
        // e.g., /play?x=1&y=1
        if (params.containsKey("x") && params.containsKey("y")) {
            try {
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y"));

                // Execute the play action
                this.game = this.game.play(x, y);
                responseText = "Move played at (" + x + ", " + y + ")";
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                responseText = "Error: Invalid coordinates provided for play!";
            }
        } else {
            responseText = "Missing parameters! Example: /play?x=1&y=1";
        }
    }

    // Extract the view-specific data from the game and apply it to the template
    GameState gameplay = GameState.forGame(this.game);
    responseText += "\n" + gameplay.toString();
    return newFixedLengthResponse(responseText);
}

}