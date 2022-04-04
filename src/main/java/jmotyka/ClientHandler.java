package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import jmotyka.clientRequestHandlers.RequestHandlersFactory;
import jmotyka.entities.PrivateChannel;
import lombok.Getter;
import lombok.Setter;
import jmotyka.requests.Request;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandler implements Runnable {

    @Getter
    private final ClientHandlers clientHandlers;
    @Getter
    private Socket socket;

    @Getter @Setter
    private ObjectInputStream objectInputStream;
    @Getter @Setter
    private ObjectOutputStream objectOutputStream;

    @Getter @Setter
    private String clientUsername;
    @Getter @Setter
    private String channelName;

    @Getter @Setter
    private PrivateChannel privateChannel; // to identify private channel in new map. Czy ja tego w ogóle używam?

    private RequestHandlersFactory requestHandlersFactory;

    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public ClientHandler(Socket socket, ClientHandlers clientHandlers) throws IOException {
        this.clientHandlers = clientHandlers;
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.requestHandlersFactory = new RequestHandlersFactory(this, clientHandlers);
        } catch (IOException e) {
            //close(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) { // DODAŁEM PĘTLĘ..
            try {
                logger.log(Level.INFO, "client handler waiting for requests...");

                Request request = ((Request)objectInputStream.readObject()); // tu gmeram
                RequestHandler requestHandler = requestHandlersFactory.getRequestHandler(request);
                requestHandler.processRequest();

                logger.log(Level.INFO, "client handler processed request.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                //close(socket, bufferedReader, bufferedWriter);
                //break;
           }
        }
    }

    // to chyba będzie niepotrzebne

    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        clientHandlers.remove(this);
        try{
            if(bufferedReader !=null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



