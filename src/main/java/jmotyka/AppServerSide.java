package jmotyka;

import java.io.IOException;
import java.net.ServerSocket;

public class AppServerSide {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Server.getPORT());
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
