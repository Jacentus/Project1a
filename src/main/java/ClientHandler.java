import clientRequestHandlers.RequestHandler;
import lombok.Getter;
import lombok.Setter;
import requests.GetAllChannelsRequest;
import requests.JoinGroupChatRequest;
import responses.GetAllChannelsResponse;
import requests.Request;
import requests.SendFileRequest;
import responses.Response;
import serverResponseHandlers.ResponseHandler;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable, Serializable{
    // moja lista userów i kanałów.
    private final ClientHandlers clientHandlers;

    private Socket socket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectInputStream objectInputStream;
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
                Response response = responseHandler.processRequest((Request)objectInputStream.readObject());
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


    public void broadcastMessage(String message, String channelName) {
        for(ClientHandler client : clientHandlers.getClientHandlers().get(channelName) ) {
                    try {
                        if (!client.clientUsername.equals(clientUsername)) {
                            client.bufferedWriter.write(message);
                            client.bufferedWriter.newLine();
                            client.bufferedWriter.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        close(socket, bufferedReader, bufferedWriter);
                    }
                }
            }

    /*public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage(String.format("SERVER: %s has left the %s channel!", clientUsername, this.channelName), this.channelName); // TODO: pozamykać połączenia i pousuwać userów na każde zamknięcie/wyjście
    }*/

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



