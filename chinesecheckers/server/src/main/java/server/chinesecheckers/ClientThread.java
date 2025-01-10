package server.chinesecheckers;

import java.net.*;
import java.io.*;

/**
 * Klasa wątku obsługującego komunikację z klientem (graczem)
 */
public class ClientThread extends Thread {

    private Socket socket;
    private ServerApp server;

    public int playerID;
    private String playerNickname;
    private int status; // 0 - czeka na gre, 1 - w grze, czeka na ruch, 2 - wykonuje ruch

    private PrintWriter out;

    /**
     * Konstruktor do ustawienia socketu komunikacji z klientem oraz przypisanego wstępnie numeru gracza
     * @param socket
     * @param playerID
     */
    public ClientThread(Socket socket, int playerID, ServerApp server) {
        this.socket = socket;
        this.playerID = playerID;
        this.server = server;
        status = 0;
    }

    @Override
    public void run() {
        try  {
            // Inicjalizacja komunikacji przez strumienie z klientem
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMessage = in.readLine();

            int spaceIndex = clientMessage.indexOf(' ');
            try {
                playerNickname = clientMessage.substring(spaceIndex + 1);
            } catch (Exception e) {
                playerNickname = "Player " + playerID;
            }

            System.out.println("player with nickname '" + playerNickname + "' connected to server");
            out.println("Hello " + playerNickname + "! I am server!");

            do {
                clientMessage = in.readLine();
                // Jeśli response jest null to znaczy że serwer został zamknięty
                if (clientMessage == null) {
                    System.out.println("player:" + playerID + " DISCONNECTED ");
                    break;
                }
                System.out.println("player:" + playerID + " >> " + clientMessage);
                
                String response = response(clientMessage);
                if(response.startsWith("[CLI]")) {
                    printMessage(response.substring(5));
                } else {
                    server.printForAll(response.substring(5));
                }
            } while (!clientMessage.equals("exit"));

            server.updatePlayers();
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
            case "draw":
                response = "[ALL] " + server.game.draw();
                break;
            case "skip":
                if(status == 2) {
                    response = "[ALL] " + playerNickname + " skipped his turn.";
                    server.game.nextPlayer();
                } else {
                    response = "[CLI] It's not your turn.";
                }
                break;
            case "move":
                if(status == 2) {
                    try {
                        server.game.move(playerID, argument);
                        response = "[ALL]" + playerNickname + " moved " + argument + ".";
                    } catch (IllegalArgumentException e) {
                        response = "[CLI] " + e.getMessage();
                    }
                } else {
                    response = "[CLI] It's not your turn.";
                }
                break;
            default:
                response = "[CLI] Unknown command.";
                break;
        }

        return response;
    }

    public int getStatus() {
        return status;
    }

    public void changeStatus(int status) {
        this.status = status;
    }

    public void printMessage(String message) {
        out.println(message);
    }
}
