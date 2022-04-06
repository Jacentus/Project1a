package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import jmotyka.requests.HandleableRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandler implements Runnable {

    @Getter
    private final ClientHandlersManager clientHandlersManager;
    @Getter
    private Socket socket;
    @Getter
    private ObjectInputStream objectInputStream;
    @Getter
    private ObjectOutputStream objectOutputStream;
    @Getter
    @Setter
    private String clientUsername; //TODO: zastanowić sie, czy tego nie ugenerycznić w jakiś sposób
/*  @Getter
    @Setter
    private String channelName;
    @Getter
    @Setter
    private PrivateChannel privateChannel;*/
    private RequestHandler requestHandler;
    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem

    public ClientHandler(Socket socket, ClientHandlersManager clientHandlersManager) throws IOException {
        // todo: atomic integer id dla usera?
        this.clientHandlersManager = clientHandlersManager;
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.requestHandler = new RequestHandler(this.clientHandlersManager, this);
        } catch (IOException e) {
            logger.log(Level.INFO, "ClientHandler failed to start...");
            //clientHandlersManager.remove(this); //TODO: usuń z listy kanałów gdy padnie
            Server.closeSocket(socket);
            closeStreams(objectInputStream, objectOutputStream); // TODO: zastanowic się, czy nie dac tego na np. shutdownhook
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                logger.log(Level.INFO, "client handler waiting for requests...");
                HandleableRequest request = ((HandleableRequest) objectInputStream.readObject());
                requestHandler.handleRequest(request);
                logger.log(Level.INFO, "client handler processed request.");
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.INFO, "Error when handling Client's requests. Closing...");
                //ClientHandlersManager.getMapOfAllChannels().remove(this); // todo: usunąć z listy kanału gdy padnie
                e.printStackTrace();
                closeStreams(objectInputStream, objectOutputStream);
                Server.closeSocket(socket);
                break;
            }
        }
    }

    public static <E extends InputStream, T extends OutputStream> void closeStreams(E inputStream, T outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



