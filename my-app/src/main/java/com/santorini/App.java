package com.santorini;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

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
        super(8080);

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

        switch (uri) {
            case "/newgame" -> {
                // Start a new game
                Player player1 = new Player();
                Player player2 = new Player();
                this.game = new Game(player1, player2);
                responseText = "New game started!";
            }

            case "/placeworker" -> {
                // Example: /placeworker?x1=1&y1=2&x2=1&y2=1
                if (params.containsKey("x1") && params.containsKey("y1") &&
                        params.containsKey("x2") && params.containsKey("y2")) {
                    int x1 = Integer.parseInt(params.get("x1"));
                    int y1 = Integer.parseInt(params.get("y1"));
                    int x2 = Integer.parseInt(params.get("x2"));
                    int y2 = Integer.parseInt(params.get("y2"));

                    Player currentPlayer = this.game.getTurn();
                    Board board = this.game.getBoard();
                    currentPlayer.placeWorker(board.getCell(x1, y1), board.getCell(x2, y2), board);

                    responseText = "Workers placed at (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ")";
                } else {
                    responseText = "Missing parameters! Example: /placeworker?x1=1&y1=2&x2=1&y2=1";
                }
            }

            case "/play" -> {
                // Example: /play?x=1&y=2
                if (params.containsKey("x") && params.containsKey("y")) {
                    int x = Integer.parseInt(params.get("x"));
                    int y = Integer.parseInt(params.get("y"));

                    this.game = this.game.play(x, y);
                    responseText = "Move played at (" + x + ", " + y + ")";
                } else {
                    responseText = "Missing parameters! Example: /play?x=1&y=2";
                }
            }

            case "/status" -> // Return the current game state
                responseText = this.game.getBoard().toString();

            case "/checkwin" -> {
                // Check if the current player has won
                Player currentPlayer = this.game.getTurn();
                if (currentPlayer.checkWinStatus()) {
                    responseText = currentPlayer + " wins!";
                } else {
                    responseText = "No winner yet.";
                }
            }

            default -> responseText = "Santorini Game API: Available endpoints:\n" +
                               "/newgame - Start a new game\n" +
                               "/placeworker?x1={x1}&y1={y1}&x2={x2}&y2={y2} - Place workers for the current player\n" +
                               "/play?x={x}&y={y} - Play a move\n" +
                               "/status - Get the current game board\n" +
                               "/checkwin - Check if the current player has won\n";
        }

        return newFixedLengthResponse(responseText);
    }
}
