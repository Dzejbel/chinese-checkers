package server.chinesecheckers;

import java.util.Random;

public class GameEngine {
    private ServerApp server;
    private Game game;
    private ClientThread[] players;
    private int[] playersId;
    private int currentPlayer;

    public GameEngine(ServerApp server) {
        this.server = server;
    }

    public String start(int type, int numberOfPlayers) {
        if (numberOfPlayers != 2 && numberOfPlayers != 3 && numberOfPlayers != 4 && numberOfPlayers != 6) {
            return "Unable to start new game for " + numberOfPlayers + " - try for 2, 3, 4 or 6.";
        }

        players = new ClientThread[numberOfPlayers];
        playersId = new int[numberOfPlayers];

        int index = 0;
        int availablePlayers = 0;
        for (int i = 1; i <= server.clientCount; i++) {
            if ((server.players[i] != null || server.players[i].getState() == Thread.State.TERMINATED) && server.players[i].status == 0) {
                server.players[i].status = 1;
                players[index] = server.players[i];
                playersId[index] = i;
                
                availablePlayers++;
                index++;
            }
        }

        if (availablePlayers == numberOfPlayers) {
            game = new Game(type, numberOfPlayers);
            currentPlayer = new Random().nextInt(numberOfPlayers);
            players[currentPlayer].status = 2;
            return "Started new game for " + numberOfPlayers;
        } else {
            for (int i = 0; i < numberOfPlayers; i++) {
                server.players[i].status = 0;
            }
            players = null;
            return "Unable to start new game for " + numberOfPlayers + " - not enough players!";
        }
    }

    public boolean state() {
        if (game == null) {
            return false;
        } else {
            return true;
        }
    }

    public String execute(int player, String command, String arguments) {
        String result;
        switch (command) {
            case "move":
                int xS = Integer.parseInt(arguments.substring(0, arguments.indexOf(',')));
                int yS = Integer.parseInt(arguments.substring(arguments.indexOf(',') + 1, arguments.indexOf('-')));
                int xF = Integer.parseInt(arguments.substring(arguments.indexOf('-') + 1, arguments.lastIndexOf(',')));
                int yF = Integer.parseInt(arguments.substring(arguments.lastIndexOf(',') + 1));
                result = game.move(player, xS, yS, xF, yF) + ". " + nextPlayer();
                break;
            case "draw":
                result = game.draw();
                break;
            default:
                result = "Unknown command.";
                break;
        }
        return result;
    }

    public String nextPlayer() {
        players[currentPlayer].status = 1;
        currentPlayer = (currentPlayer + 1) % players.length;
        players[currentPlayer].status = 2;
        return "Next player: " + players[currentPlayer].playerNumber;
    }
}
