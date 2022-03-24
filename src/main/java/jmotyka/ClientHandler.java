package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import lombok.Getter;
import lombok.Setter;
import jmotyka.requests.Request;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, Serializable{

    @Getter
    private final ClientHandlers clientHandlers;
    @Getter
    private Socket socket;
    @Getter
    private ObjectInputStream objectInputStream;
    @Getter
    private ObjectOutputStream objectOutputStream;
    @Getter @Setter
    private String clientUsername;
    @Getter @Setter
    private String channelName;

    private final RequestHandler requestHandler = new RequestHandler(this);

    public ClientHandler(Socket socket, ClientHandlers clientHandlers) {
        this.clientHandlers = clientHandlers;
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //close(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                requestHandler.processRequest((Request)objectInputStream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                //close(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

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



