package jmotyka;

import jmotyka.entities.PrivateChannel;
import jmotyka.requests.Request;
import jmotyka.responses.Response;
import jmotyka.serverResponseHandlers.ResponseHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class Client {

    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem
    @Getter
    private final ResponseHandler responseHandler = new ResponseHandler(this);
    @Getter
    private final ClientLockForServerResponse lock = new ClientLockForServerResponse();
    @Getter
    private Socket socket;
    @Getter
    private ObjectInputStream inputStreamReader;
    @Getter
    private ObjectOutputStream outputStreamWriter;
    @Getter
    private String username;
    @Getter
    @Setter
    private String channelName; //TODO: przemyśl lepszy i łatwiejszy sposób na identyfikowanie kanałów
    @Getter
    @Setter
    private PrivateChannel privateChannel;

    public Client(Socket socket, String username) throws IOException {
        try {
            this.socket = socket;
            this.username = username;
            this.inputStreamReader = new ObjectInputStream(socket.getInputStream());
            this.outputStreamWriter = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, outputStreamWriter, inputStreamReader);
        }
    }

    public void sendRequest(Request request) {
        try {
            logger.log(Level.INFO, "Sending request: " + request);
            outputStreamWriter.writeObject(request);
            outputStreamWriter.flush();
            logger.log(Level.INFO, "request sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    logger.log(Level.INFO, "Client is listening...");
                    responseHandler.handleResponse((Response) inputStreamReader.readObject());
                    logger.log(Level.INFO, "Response has been handled by the Client");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    close(socket, outputStreamWriter, inputStreamReader);
                }
            }
        }).start();
    }

    public void close(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





