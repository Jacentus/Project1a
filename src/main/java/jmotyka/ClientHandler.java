package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import jmotyka.entities.Channel;
import jmotyka.requests.HandleableRequest;
import jmotyka.requests.RemoveFromChannelRequest;
import jmotyka.requests.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.util.Map;
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
                killConnectionWithClient(ClientHandlersManager.getMapOfAllChannels(), this);
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

    public void killConnectionWithClient(Map<String, Channel> allChannels, ClientHandler clientHandler){
        for (Channel channel: allChannels.values()) {
                channel.getUsersInChannel().remove(clientHandler);
        }
    }

}



