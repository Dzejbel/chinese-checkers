package server.chinesecheckers;

import java.net.*;
import java.util.ArrayList;

/**
 * Główna klasa aplikacji serwera
 */
public class ServerApp {

    public static final int DEFAULT_PORT = 4444;

    public int maxPlayers;
    public ArrayList<ClientThread> players;
    public GameEngine game;

    public static void main(String[] args) {
        try {
            int maxPlayers = Integer.parseInt(args[0]);
            int port = Integer.parseInt(args[1]);
            new ServerApp(maxPlayers, port);
        } catch(Exception e) {
            System.out.println("ERROR: Invalid argument. Try [maxPlayers: int, port: int]");
        }
    }

    private ServerApp(int maxPlayers, int port) {
        this.maxPlayers = maxPlayers;
        runServer(port);
    }

    private void runServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Info: Server listening on port " + port);

            players = new ArrayList<ClientThread>();
            game = new GameEngine(this);

            int id = 0;
            while (true) {
                Socket socket = serverSocket.accept();

                if (players.size() < maxPlayers) {
                    players.add(new ClientThread(socket, id, this));
                    players.get(players.size() - 1).start();
                    id++;
                    if(players.size() == maxPlayers) {
                        System.out.println("[Server] Starting game.");
                        game.start('c', maxPlayers);
                    }
                }
                else {
                    socket.close();
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    public void updatePlayers() {
        for(ClientThread client : players) {
            if(client.getState() == Thread.State.TERMINATED) {
                players.remove(client);
            }
        }
    }

    /**
     * Metoda do printowania wszystkim klientom tego co wysłał jeden z nich
     * Pierwotny wysyłacz nie otrzymuje tutaj wiadomości ponieważ dla niego jest generowana inna wiadomość
     */
    public void printForAllExcept(String message, ClientThread excludedSender) {
        for(ClientThread client : players) {
            if(client != null && client.getState() != Thread.State.TERMINATED && client != excludedSender) {
                client.printMessage(message);
            }
        }
    }

    public void printForAll(String message) {
        for(ClientThread client : players) {
            if(client != null && client.getState() != Thread.State.TERMINATED) {
                client.printMessage(message);
            }
        }
    }
}
