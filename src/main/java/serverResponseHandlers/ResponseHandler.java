package serverResponseHandlers;

import requests.GetAllChannelsRequest;
import requests.JoinGroupChatRequest;
import requests.Request;
import requests.SendFileRequest;
import responses.GetAllChannelsResponse;
import responses.JoinGroupChatResponse;
import responses.Response;
import responses.SendFileResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResponseHandler {

    private ClientHandler clientHandler;

    public ResponseHandler() {
    }

    public void handleResponse(Response response) {
        if (response instanceof GetAllChannelsResponse) {
            System.out.print("ALL ROOMS IN CHAT: ");
            System.out.println(((GetAllChannelsResponse) response).getAllChannelsNames());
        }
        if (response instanceof JoinGroupChatResponse) {
            System.out.println(String.format("You have joined %s channel!", ((JoinGroupChatResponse) response).getChannelName()));
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bufferedWriter.write(username + ": " + message);
                //outputStreamWriter.writeObject(request);
                //outputStreamWriter.flush();
                //System.out.println("infinite loop");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        if (response instanceof SendFileResponse) {

        }
    }


   public Response processRequest(Request request) throws IOException {
        if(this.clientUsername == null) {
            this.setClientUsername(request.getUserName());
        }
        if (request instanceof JoinGroupChatRequest){
            clientHandlers.add(this); // dodaj do kanału, jeśli nie istnieje - utwórz TODO: jakoś to rozróżnić
            //próba odpalenia czatu
            String message = bufferedReader.readLine();
            broadcastMessage(message, channelName);
            // jakoś odpalić nadawanie wiadomości
        }

        if (request instanceof SendFileRequest){
            for(ClientHandler client : clientHandlers.getClientHandlers().get(channelName) ){ //dostaję się do listy
                //do sth
            }
        }
        if (request instanceof GetAllChannelsRequest) { // działa
            List<String> channelNames = new ArrayList<>();
            clientHandlers.getClientHandlers().forEach((k,v) -> { channelNames.add(getChannelName());});
            GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);
            if (getAllChannelsResponse.getAllChannelsNames()!=null) return getAllChannelsResponse;
            else return null;
        }
        return null;
    }



}
