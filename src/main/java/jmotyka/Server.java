package jmotyka;

import jmotyka.chathistoryreaderandwriter.ShutDownHookSavingHistoryToFile;
import jmotyka.entities.ChatHistory;
import lombok.Getter;
import lombok.extern.java.Log;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public Server(ServerSocket serverSocket) { // TODO: thread safe!
        this.serverSocket = serverSocket;
        this.clientHandlers = new ClientHandlers(new ChatHistory());
    }

    public void startServer() {
        logger.log(Level.INFO, "Server is listening on port: " + getPORT());

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHookSavingHistoryToFile(clientHandlers)));

        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "New Client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers);
                executor.execute(clientHandler);
                //Thread thread = new Thread(clientHandler);
                //thread.start();
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
                logger.log(Level.INFO, "Closing server socket");
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



