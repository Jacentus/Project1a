package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.GetAllChannelsRequest;
import jmotyka.requests.JoinGroupChatRequest;
import jmotyka.requests.Request;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.GetAllChannelsResponse;
import jmotyka.responses.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler { // to działa na requestach, jakie wpadły na serwer, mam więc dostęp do wszystkich zasobów serwera

    ClientHandler clientHandler;

    public RequestHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Response processRequest(Request request) throws IOException {
        // set client's name if null
        if(clientHandler.getClientUsername() == null) {
            clientHandler.setClientUsername(request.getUserName());
        }

        if (request instanceof JoinGroupChatRequest){
            clientHandler.getClientHandlers().getMapOfAllRooms().put("KANAL", new ArrayList<>());
            clientHandler.getClientHandlers().getMapOfAllRooms().get(((JoinGroupChatRequest) request).getChannelName()).add(clientHandler); // dodaj do kanału, jeśli nie istnieje - utwórz TODO: jakoś to rozróżnić
            //próba odpalenia czatu
            String message = clientHandler.getBufferedReader().readLine();
            broadcastMessage(message, clientHandler.getChannelName());
            // TODO: jakoś odpalić nadawanie wiadomości
        }

        if (request instanceof SendFileRequest){
            for(ClientHandler client : clientHandler.getClientHandlers().getMapOfAllRooms().get(clientHandler.getChannelName())){ //dostaję się do listy
                //do sth
            }
        }

        if (request instanceof GetAllChannelsRequest) { // działało wcześniej :)
            List<String> channelNames = new ArrayList<>();
            ArrayList<ClientHandler> mockList = new ArrayList<>();
            ClientHandler mockHandle = new ClientHandler(new Socket(), new ClientHandlers());
            mockHandle.setChannelName("MOCK_KANAL");
            mockHandle.setClientUsername("MOCK_USER");
            mockList.add(mockHandle);
            clientHandler.getClientHandlers().getMapOfAllRooms().put("MOCK_KANAL", mockList);
            clientHandler.getClientHandlers().getMapOfAllRooms().forEach((k, v) -> {
                channelNames.add(clientHandler.getChannelName());});

            GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);

            if (getAllChannelsResponse.getAllChannelsNames()!=null) return getAllChannelsResponse;
            //else return null;
        }
        return null;
    }


    public void broadcastMessage(String message, String channelName) { //TO WYSLANIE WIADOMOSC. JAK ZROBIC CZAT W LOOPIE?! KTORY MA BYĆ ODPALANY W RAZIE ODPOWIEDNIEGO REQUESTU I RESPONSU
        for(ClientHandler client : clientHandler.getClientHandlers().getMapOfAllRooms().get(channelName) ) {
            try {
                if (!client.getClientUsername().equals(clientHandler.getClientUsername())) {
                   client.getBufferedWriter().write(message);
                   client.getBufferedWriter().newLine();
                   client.getBufferedWriter().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                //clientHandler.close(socket, bufferedReader, bufferedWriter);
            }
        }
    }


}
