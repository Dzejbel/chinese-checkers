package server.chinesecheckers;

import java.net.*;
import java.io.*;

/**
 * Klasa wątku obsługującego komunikację z klientem (graczem)
 */
public class ClientThread extends Thread {

    public Socket socket;
    private ServerApp server;
    public int playerNumber;
    public int status; // 0 - czeka na gre, 1 w grze, czeka na ruch, 2 - wykonuje ruch

    private PrintWriter out;

    /**
     * Konstruktor do ustawienia socketu komunikacji z klientem oraz przypisanego wstępnie numeru gracza
     * @param socket
     * @param playerNumber
     */
    public ClientThread(Socket socket, int playerNumber, ServerApp server) {
        this.socket = socket;
        this.playerNumber = playerNumber;
        this.server = server;
        status = 0;
    }

    @Override
    public void run() {
        try  {
            // Inicjalizacja komunikacji przez strumienie z klientem
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMessage, playerNickname;
            clientMessage = in.readLine();

            int spaceIndex = clientMessage.indexOf(' ');
            try {
                playerNickname = clientMessage.substring(spaceIndex + 1);
            } catch (Exception e) {
                playerNickname = "Unknown";
            }

            System.out.println("player with nickname '" + playerNickname + " and number " + playerNumber + "' connected to server");
            out.println("Hello " + playerNickname + "! I am server!");

            do {
                clientMessage = in.readLine();
                // Jeśli response jest null to znaczy że serwer został zamknięty
                if (clientMessage == null) {
                    System.out.println("player:" + playerNumber + " DISCONNECTED ");
                    break;
                }
                System.out.println("player:" + playerNumber + " >> " + clientMessage);
                server.printForAllExcept("player:" + playerNumber + " >> " + clientMessage, this);
                
                String response = response(clientMessage);
                out.println(response);

            } while (!clientMessage.equals("exit"));

            server.updatePlayers(socket, false);
            server.updateClientCount();
            socket.close();

        } catch (UnknownHostException severNotFoundException) {
            System.out.println("ERROR: " + severNotFoundException);
        }
        
        catch (IOException IOError) {
            System.out.println("ERROR: " + IOError);
        }
    }

    private String response(String message) {
        String command;
        String argument;

        int spaceIndex = message.indexOf(' ');
        if (spaceIndex == -1) {
            command = message;
            argument = "";
        } else {
            command = message.substring(0, spaceIndex);
            argument = message.substring(spaceIndex + 1);
        }

        String response;
        switch (command) {
            case "exit":
                response = command;
                break;
            case "start":
                int arg = Integer.parseInt(argument);
                if (server.game.state()) {
                    response = "The game is already being played";
                }
                else if (arg == server.clientCount) {
                    response = server.game.start(1, arg);
                } else {
                    response = "Invalid number of players (currently " + server.clientCount + ")";
                }
                break;
            case "draw":
                if (server.game.state()) {
                    response = server.game.execute(playerNumber, command, argument);
                } else {
                    response = "There is no game currently being played";
                }
                break;
            case "skip":
            if (server.game.state()) {
                response = "Skipped your turn. " + server.game.nextPlayer();
                } else {
                    response = "There is no game currently being played";
                }
                break;
            case "move":
                if (server.game.state()) {
                    response = server.game.execute(playerNumber, command, argument);
                } else {
                    response = "There is no game currently being played";
                }
                break;
            default:
                response = "Unknown command.";
                break;
        }

        return response;
    }

    /**
     * Metoda aktualizująca numer gracza
     * @param newPlayerNumber
     */
    public void changePlayerNumber(int newPlayerNumber) {
        playerNumber = newPlayerNumber;
    }

    public void printMessage(String message) {
        out.println(message);
    }
}
