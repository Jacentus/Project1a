package jmotyka.serverResponseHandlers;

import jmotyka.Client;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler { // to działa po stronie KLIENTA!

    private Client client;

    public ResponseHandler(Client client) {
        this.client = client;
    }

    public void handleResponse(Response response) throws IOException {
        if (response instanceof GetAllChannelsResponse) { // działa
            System.out.print("ALL ROOMS IN CHAT: ");
            System.out.println(((GetAllChannelsResponse) response).getAllChannelsNames());
        }
        if (response instanceof JoinPublicChatResponse) { // działa
            System.out.println(String.format("\\nYou have joined %s channel!", ((JoinPublicChatResponse) response).getChannelName()));
            //client.getPrivateChannel().setClientPermittedToChat(true); // ustawiam flagę aby wpuścić go do chatroomu - tu pojawia się nullpointer gdy dodaję się do publicznego kanału (bo nie ma w odpowiedzi PrivateCHannelName)
        }
        if (response instanceof JoinPrivateChatResponse) { // działa
            System.out.println(String.format("\\nYou have joined %s channel!", ((JoinPrivateChatResponse) response).getChannel().getChannelName()));
            System.out.println("JESTEM W RESPONSE HANDLER. CLIENT: " + client);
            System.out.println("RESPONSE WARTOŚĆ IS PERMITTED: " + ((JoinPrivateChatResponse) response).getChannel().getClientPermittedToChat());
            client.setPrivateChannel(((JoinPrivateChatResponse) response).getChannel()); // czy to w ogóle jest potrzebne? Nastawiłem to wcześniej
            client.getPrivateChannel().setClientPermittedToChat(true); // ustawiam flagę aby wpuścić go do chatroomu - tu pojawia się nullpointer gdy dodaję się do publicznego kanału (bo nie ma w odpowiedzi PrivateCHannelName)
        }
        if (response instanceof MessageResponse) { // działa
            System.out.println("JESTEM W MESSAGE RESPONSE");
            MessageResponse messageResponse = (MessageResponse) response;
            System.out.println(messageResponse);
        }
        if (response instanceof GetChatHistoryResponse){ // działa
            System.out.println("JESTEM W RESPONSE Z GETCHATHISTORY");
            GetChatHistoryResponse getChatHistoryResponse = (GetChatHistoryResponse)response;
            for (MessageRequest message : getChatHistoryResponse.getChatHistory()) {
                System.out.println(message);
            }
        }
        if(response instanceof CreatePrivateChatResponse){
            System.out.println("Private chat created! Waiting for " + ((CreatePrivateChatResponse) response).getPermittedUsers() + " to join...");
        }
        if (response instanceof ErrorResponse) {
            System.out.println("ERROR || " + ((ErrorResponse) response).getMessage());
        }
        if (response instanceof SendFileResponse) { //działa
            System.out.println("A FILE HAS BEEN RECEIVED FROM " + ((SendFileResponse) response).getUserName());
            String filePath = "D:\\RECEIVED_FILES\\" + ((SendFileResponse) response).getFileName(); // ścieżka do pliku
            System.out.println("path: " + filePath);
            File file = new File(filePath);
            try{
                OutputStream os = new FileOutputStream(file);
                os.write(((SendFileResponse) response).getFile());
                os.close();
                System.out.println("A FILE HAS BEEN READ SUCCESSFULLY");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}



