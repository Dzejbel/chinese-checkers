package client.chinesecheckers;

import java.net.*;
import java.io.*;

/**
 * Główna klasa aplikacji klienta
 */
public class ClientApp {

    private static final int DEFAULT_PORT = 4444;

    public String playerNickname;
    public int serverPort;

    public BufferedReader consoleBufferReader;
    public Socket socket;
    public ServerOutputHandlerThread serverOutputHandlerThread;
    public ServerInputHandlerThread serverInputHandlerThread;

    public static void main(String[] args) {
        new ClientApp(args);
    }

    public ClientApp(String[] args) {
        clientInit(args);
    }

    public void clientInit(String[] args) {
        // Wstępne ustawienie nieprawidłowej wartości na serverPort
        serverPort = -1;

        // Ustawienie nicku i pierwszego portu
        if (args.length > 1) {
            for (String arg: args) {
                if (arg.startsWith("nickname:") && arg.length() > 8) {
                    playerNickname = arg.substring(9);

                } else if (arg.equals("port:default")) {
                    serverPort = DEFAULT_PORT;

                } else if (arg.startsWith("port:") && arg.length() > 5) {
                    try {
                        serverPort = Integer.parseInt(arg.substring(5));
                    } catch(NumberFormatException e) {
                        continue;
                    }
                }
            }
        }

        // Utworzenie bufora na tekst wpisywany w konsoli
        consoleBufferReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Jeśli gracz nie wpisał nickname, ustal go teraz
            if (playerNickname == null) {
                System.out.print("Please enter nickname: ");
                playerNickname = consoleBufferReader.readLine();
            }

            // Jeśli gracz nie wpisał (poprawnego) portu
            if (serverPort == -1) {
                while (serverPort == -1) {
                    try {
                        System.out.print("Please enter server port: ");
                        serverPort = Integer.parseInt(consoleBufferReader.readLine());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        } catch (IOException IOError) {
            System.out.println("ERROR: " + IOError);
        }

        startServerIOThreads();
    }

    private void startServerIOThreads() {
        try  {
            socket = new Socket("localhost", serverPort);

            // Stwórz i uruchom wątek odpowiedzialny za odbieranie informacji od serwera
            serverInputHandlerThread = new ServerInputHandlerThread(this);
            serverInputHandlerThread.start();

            // Stwórz i uruchom wątek odpowiedzialny za wysyłanie informacji do serwera
            serverOutputHandlerThread = new ServerOutputHandlerThread(this);
            serverOutputHandlerThread.start();

        } catch (UnknownHostException severNotFoundException) {
            System.out.println("ERROR: " + severNotFoundException);

        } catch (IOException IOError) {
            System.out.println("ERROR: " + IOError);
        }
    }
}
