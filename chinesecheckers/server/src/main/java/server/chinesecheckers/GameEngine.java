package server.chinesecheckers;

import java.util.Random;

public class GameEngine {
    private ServerApp server;
    private Game game;
    private int currentPlayer;

    public GameEngine(ServerApp server) {
        this.server = server;
    }

    public void start(char variant, int numberOfPlayers) throws IllegalArgumentException {
        switch(variant) {
            case 'c': 
                try {
                    game = new ClassicGame(numberOfPlayers);
                    for(ClientThread client : server.players) {
                        client.changeStatus(1);
                    }
                    currentPlayer = new Random().nextInt(numberOfPlayers);
                    server.players.get(currentPlayer).changeStatus(2);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
                break;
            default: 
                throw new IllegalArgumentException("Unknown game type.");
        }
    }

    public boolean state() {
        if (game == null) {
            return false;
        } else {
            return true;
        }
    }

    public String draw() {
        return game.draw();
    }

    public void move(int playerID, String args) throws IllegalArgumentException {
        try {
            int xS = Integer.parseInt(args.substring(0, args.indexOf(',')));
            int yS = Integer.parseInt(args.substring(args.indexOf(',') + 1, args.indexOf('-')));
            int xF = Integer.parseInt(args.substring(args.indexOf('-') + 1, args.lastIndexOf(',')));
            int yF = Integer.parseInt(args.substring(args.lastIndexOf(',') + 1));
            game.move(currentPlayer, xS, yS, xF, yF);
            nextPlayer();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid fields.");
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public void nextPlayer() {
        server.players.get(currentPlayer).changeStatus(1);
        currentPlayer = (currentPlayer + 1) % server.players.size();
        server.players.get(currentPlayer).changeStatus(2);
    }
}
