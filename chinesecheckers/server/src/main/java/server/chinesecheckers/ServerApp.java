package server.chinesecheckers;

import java.net.*;

/**
 * Główna klasa aplikacji serwera
 */
public class ServerApp {

    public static final int DEFAULT_PORT = 4444;

    public int clientCount;
    public int maxPlayers;
    public ClientThread[] players;
    public GameEngine game;

    public static void main( String[] args ) {
        try {
            int maxPlayers = Integer.parseInt(args[0]);
            new ServerApp(maxPlayers);
        } catch(NumberFormatException e) {
            System.out.println("ERROR: Invalid number of players. Use 2, 3, 4 or 6 as an argument.");
        }
    }

    private ServerApp(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        runServer();
    }

    private void runServer() {
        int port = DEFAULT_PORT;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Info: Server listening on port " + port);

            players = new ClientThread[maxPlayers + 1];
            clientCount = 0;
            game = new GameEngine(this);

            while (true) {
                Socket socket = serverSocket.accept();

                if (clientCount < maxPlayers) {
                    players = updatePlayers(socket, true);
                    clientCount = updateClientCount();
                }
                else {
                    //komunikat do klienta o rozlaczeniu z powodu limitu
                    socket.close();
                    //zakonczenie programu klienta
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            players = updatePlayers(null, false);
            clientCount = updateClientCount();
        }
    }

    public ClientThread[] updatePlayers(Socket socket, boolean add) {
        ClientThread[] result = new ClientThread[maxPlayers + 1];
        int index = 1;

        for (int i = 1; i <= maxPlayers; i++) {
            if (players[i] != null && players[i].getState() != Thread.State.TERMINATED) {
                result[index] = players[i];
                result[index].changePlayerNumber(index);
                index++;
            }
        }

        if (add) {
            result[index] = new ClientThread(socket, index, this);
            result[index].start();
            System.out.println("Info: New player joined with number: " + index);
        }

        return result;
    }

    public int updateClientCount() {
        int result = 0;
        
        for (int i = 1; i <= maxPlayers; i++) {
            if (players[i] != null) {
                result++;
            }
        }

        return result;
    }

    /**
     * Metoda do printowania wszystkim klientom tego co wysłał jeden z nich
     * Pierwotny wysyłacz nie otrzymuje tutaj wiadomości ponieważ dla niego jest generowana inna wiadomość
     */
    public void printForAllExcept(String message, ClientThread excludedSender) {
        for (int i = 1; i <= clientCount; i++) {
            if (players[i] != null && players[i].getState() != Thread.State.TERMINATED && players[i] != excludedSender) {
                players[i].printMessage(message);
            }
        }
    }
}
