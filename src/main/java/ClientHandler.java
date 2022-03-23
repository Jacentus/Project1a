import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final ClientHandlers clientHandlers;

    private Socket socket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Getter
    private String clientUsername;
    @Getter
    private String channelName;
    @Getter @Setter
    private ClientsOptionsChoice clientRequest;

    public ClientHandler(Socket socket, ClientHandlers clientHandlers) {
        this.clientHandlers = clientHandlers;
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // owijamy biteStream w charStream bo chcemy wysłać chary
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.channelName = bufferedReader.readLine();
            //this.clientRequest = (ClientsOptionsChoice) bufferedReader.readLine();
            System.out.println("CHANNEL NAME: " + channelName);
            System.out.println("USER NAME: " + clientUsername);
            System.out.println("WIELKOŚĆ MAPY CLIENTHANDLERS: " + clientHandlers.getClientHandlers().size());
        } catch (IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
        clientHandlers.add(this); // dodaję do odpowiedniego pokoju zidentyfikowanego po jego nazwie
    }

    @Override
    public void run() {
        String message;
        while (socket.isConnected()) {
            try {
                message = bufferedReader.readLine();
                if(message == "#FILE"){
                    System.out.println("WYKRYŁEM KOMENDĘ NA TRANSFER PLIKÓW");
                }
                broadcastMessage(message, channelName);
            } catch (IOException e) {
                e.printStackTrace();
                close(socket, bufferedReader, bufferedWriter);
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

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage(String.format("SERVER: %s has left the %s channel!", clientUsername, this.channelName), this.channelName); // TODO: pozamykać połączenia i pousuwać userów na każde zamknięcie/wyjście
    }

    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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



