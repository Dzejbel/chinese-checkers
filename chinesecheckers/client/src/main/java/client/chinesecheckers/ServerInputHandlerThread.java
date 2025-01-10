package client.chinesecheckers;

import java.net.*;
import java.io.*;

/**
 * Klasa wątku obsługującego input z serwera
 * Dzięki niej gracz cały czas otrzymuje wiadomości od serwera
 */
public class ServerInputHandlerThread extends Thread {

    private ClientApp clientApp;

    ServerInputHandlerThread(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    @Override
    public void run() {
        try {
            // Inicjalizacja inputu przez strumień z serwera
            BufferedReader in = new BufferedReader(new InputStreamReader(clientApp.socket.getInputStream()));

            String serverResponse;
            do {
                if (in.ready()) {
                    serverResponse = in.readLine();

                    // Jeśli response jest null to znaczy że serwer został zamknięty wówczas zamykamy aplikację
                    if (serverResponse == null) {
                        System.out.println(" >> Server has been shut down");
                        clientApp.socket.close();
                        System.exit(1);
                    }
                    
                    System.out.println(" >> " + serverResponse);
                } else {
                    Thread.sleep(30);
                }
            } while (true);

        } catch (Exception IOError) { 
            System.out.println("ERROR (and program didn't close the socket): " + IOError);
        }
    }
}
