import lombok.Getter;
import lombok.extern.java.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Server {

    @Getter
    private final static String HOST = "localhost"; // TODO: ograniczyć ilość wątków, egzekutor, pula wątków
    @Getter
    private final static int PORT = 10000; // do przemyślenia, czy nie wrzucić w config

    private final ServerSocket serverSocket;
    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem
    private final ClientHandlers clientHandlers; // moja klasa do zarządzania pokojami, mam tam mapę wszelkich pokoi identyfikowanych po nazwie

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clientHandlers = new ClientHandlers();
    }

    public void startServer() {
        logger.log(Level.INFO, "Server is listening on port: " + getPORT());
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "New Client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server failed to start: " + e.getMessage());
            e.printStackTrace();
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



