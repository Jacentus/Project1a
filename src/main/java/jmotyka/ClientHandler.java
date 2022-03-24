package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import lombok.Getter;
import lombok.Setter;
import jmotyka.requests.Request;
import jmotyka.responses.Response;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, Serializable{
    // moja lista userów i kanałów.
    @Getter
    private final ClientHandlers clientHandlers;

    @Getter
    private Socket socket;

    @Getter
    private BufferedReader bufferedReader;
    @Getter
    private BufferedWriter bufferedWriter;
    @Getter
    private ObjectInputStream objectInputStream;
    @Getter
    private ObjectOutputStream objectOutputStream;

    // do identyfikacji kanału i użytkownika
    @Getter @Setter
    private String clientUsername;
    @Getter @Setter
    private String channelName;

    // próba procesowania otrzymywanych requestów
    private final RequestHandler requestHandler = new RequestHandler(this);

    public ClientHandler(Socket socket, ClientHandlers clientHandlers) {
        this.clientHandlers = clientHandlers;
        try {
            this.socket = socket;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // owijamy biteStream w charStream bo chcemy wysłać chary
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            //this.clientUsername = bufferedReader.readLine(); // to do zmiany na protokół
            //this.channelName = bufferedReader.readLine();
            //this.clientRequest = (ClientsOptionsChoice) bufferedReader.readLine();
            //System.out.println("CHANNEL NAME: " + channelName);
            //System.out.println("USER NAME: " + clientUsername);
            //System.out.println("WIELKOŚĆ MAPY CLIENTHANDLERS: " + clientHandlers.getClientHandlers().size());
        } catch (IOException e) {
            //close(socket, bufferedReader, bufferedWriter);
        }
        //clientHandlers.add(this); // dodaję do odpowiedniego pokoju zidentyfikowanego po jego nazwie
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                //message = bufferedReader.readLine();
                Response response = requestHandler.processRequest((Request)objectInputStream.readObject());
                objectOutputStream.writeObject(response);
                objectOutputStream.flush();
                //broadcastMessage(message, channelName);
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



