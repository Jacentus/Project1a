package jmotyka;

import jmotyka.chatHistoryReaderAndWriter.ShutDownHookSavingHistoryToFile;
import jmotyka.entities.ChatHistory;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Server {

    @Getter
    private final static String HOST = "localhost"; // TODO: ograniczyć ilość wątków, egzekutor, pula wątków
    @Getter
    private final static int PORT = 10000; // TODO: do przemyślenia, czy nie wrzucić w config
    private final ServerSocket serverSocket;
    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem
    private final ClientHandlersManager clientHandlersManager;
    private final ExecutorService executor = Executors.newCachedThreadPool(); //newFixedThreadPool(50);

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clientHandlersManager = new ClientHandlersManager(new ChatHistory());
    }

    public void startServer() {
        logger.log(Level.INFO, "Server is listening on port: " + getPORT());
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHookSavingHistoryToFile(clientHandlersManager)));
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "New Client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlersManager);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server failed to start: " + e.getMessage());
            e.printStackTrace();
            closeServerSocket();
            logger.log(Level.SEVERE, "Thread executor shutting down...");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException exception) {
                executor.shutdownNow();
            }
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                logger.log(Level.INFO, "Closing server socket...");
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Socket> void closeSocket(T socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}