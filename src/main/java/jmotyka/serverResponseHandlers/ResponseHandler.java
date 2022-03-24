package jmotyka.serverResponseHandlers;

import jmotyka.Client;
import jmotyka.ClientHandler;
import jmotyka.responses.*;

import java.io.IOException;

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
        if (response instanceof JoinGroupChatResponse) { // działa
            System.out.println(String.format("You have joined %s channel!", ((JoinGroupChatResponse) response).getChannelName()));
        }
        if (response instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) response;
            System.out.println("Masz wiadomość: ");
            System.out.println(messageResponse);
        }

        if (response instanceof SendFileResponse) {
        }
    }








    }



