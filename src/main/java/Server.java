import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Server {

    @Getter
    private final static String HOST = "localhost";
    @Getter
    private final static int PORT = 10000;
    private ServerSocket serverSocket;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }

    public void startServer(){
        logger.log(Level.INFO, "Server is listening on port: " + getPORT());
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "New Client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server failed to start: " + e.getMessage());
            e.printStackTrace();
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



