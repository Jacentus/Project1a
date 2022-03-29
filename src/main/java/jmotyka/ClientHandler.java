package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import lombok.Getter;
import lombok.Setter;
import jmotyka.requests.Request;
import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandler implements Runnable, Serializable{

    @Getter
    private final ClientHandlers clientHandlers;
    @Getter
    private Socket socket;

    // proba ustawienia socketu na read file
    @Getter @Setter
    private ObjectInputStream objectInputStream;
    @Getter @Setter
    private ObjectOutputStream objectOutputStream;
    // próba dodania FileInput i Output Streamów w celu zapisywania pików
    @Getter
    private FileInputStream fileInputStream;

    /////////////////////////
    @Getter @Setter
    private String clientUsername;
    @Getter @Setter
    private String channelName;

    private RequestHandler requestHandler;

    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public ClientHandler(Socket socket, ClientHandlers clientHandlers) throws IOException {
        this.clientHandlers = clientHandlers;
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            //this.fileInputStream = new FileInputStream(socket.getOutputStream());
            this.requestHandler = new RequestHandler(this, clientHandlers);
        } catch (IOException e) {
            //close(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) { // DODAŁEM PĘTLĘ..
            try {
                logger.log(Level.INFO, "client handler waiting for requests...");
                requestHandler.processRequest((Request)objectInputStream.readObject());
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



