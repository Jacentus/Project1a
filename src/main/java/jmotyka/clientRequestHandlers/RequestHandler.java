package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.*;
import jmotyka.responses.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler { // to działa na requestach, jakie wpadły na serwer, mam więc dostęp do wszystkich zasobów serwera

    ClientHandler clientHandler;

    public RequestHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void processRequest(Request request) throws IOException {
        // set client's name if null
        if (clientHandler.getClientUsername() == null) {
            clientHandler.setClientUsername(request.getUserName());
        }
        if (request instanceof JoinGroupChatRequest) { // działa
            clientHandler.getClientHandlers().getMapOfAllRooms().put("KANAL", new ArrayList<>()); // to do wyrzucenia
            clientHandler.getClientHandlers().getMapOfAllRooms().get(((JoinGroupChatRequest) request).getChannelName()).add(clientHandler); // dodaj do kanału, jeśli nie istnieje - utwórz TODO: jakoś to rozróżnić
            JoinGroupChatResponse joinGroupChatResponse = new JoinGroupChatResponse(((JoinGroupChatRequest) request).getChannelName());
            broadcast(joinGroupChatResponse);
        }
        if (request instanceof GetAllChannelsRequest) { // działa
            List<String> channelNames = new ArrayList<>();
            clientHandler.getClientHandlers().getMapOfAllRooms().forEach((k, v) -> {
                channelNames.add(k);
            });
            GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);
            if (getAllChannelsResponse.getAllChannelsNames() != null) {
                broadcast(getAllChannelsResponse);
            } else {
                String error = "no active channels";
                broadcast(new ErrorResponse(error));
            }
        }
        if (request instanceof MessageRequest) {
            System.out.println(request + "TO MOJ REQUEST");
            MessageResponse messageResponse = new MessageResponse(request.getUserName(), ((MessageRequest) request).getChannelName(), ((MessageRequest) request).getText());
            System.out.println(messageResponse + "TO MÓJ RESPONSE");
            ArrayList<ClientHandler> receivers = clientHandler.getClientHandlers().getMapOfAllRooms().get(messageResponse.getChannelName());
            for (ClientHandler receiver: receivers) {
                broadcast(messageResponse);
            }

            System.out.println("TO LISTA ODBIORCÓW: " + receivers + " , JESTEM W REQUEST HANDLER!");
            /*for (ClientHandler client : clientHandler.getClientHandlers().getMapOfAllRooms().get(messageResponse.getChannelName())) {
                System.out.println("a to jest adresat z listy których powinno byc więcej" + client);
                try {
                    //if (!client.getClientUsername().equals(clientHandler.getClientUsername())) {
                        broadcast(messageResponse);
                    //}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }

        if (request instanceof SendFileRequest) {
                    //for (ClientHandler client : clientHandler.getClientHandlers().getMapOfAllRooms().get(clientHandler.getChannelName())) { //dostaję się do listy
                        //do sth
                    }
                }

    public void broadcast(Response response) throws IOException {
        clientHandler.getObjectOutputStream().writeObject(response);
        clientHandler.getObjectOutputStream().flush();
    }

/*    public void broadcastToALL(MessageRequest messageRequest) { //TO WYSLANIE WIADOMOSC. JAK ZROBIC CZAT W LOOPIE?! KTORY MA BYĆ ODPALANY W RAZIE ODPOWIEDNIEGO REQUESTU I RESPONSU
        //clientHandler.getClientHandlers().getMapOfAllRooms().put("KANAL", new ArrayList<>()); // to do wyrzucenia
        for (ClientHandler client : clientHandler.getClientHandlers().getMapOfAllRooms().get(messageRequest.getChannelName())) {
            try {
                if (!client.getClientUsername().equals(clientHandler.getClientUsername())) {
                    broadcast(messageRequest);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //clientHandler.close(socket, bufferedReader, bufferedWriter);
            }
        }
    }*/

}
