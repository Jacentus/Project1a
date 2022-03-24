package jmotyka.serverResponseHandlers;

import jmotyka.Client;
import jmotyka.responses.GetAllChannelsResponse;
import jmotyka.responses.JoinGroupChatResponse;
import jmotyka.responses.Response;
import jmotyka.responses.SendFileResponse;

import java.io.IOException;
import java.util.Scanner;

public class ResponseHandler { // to działa po stronie KLIENTA!

    private Client client;

    public ResponseHandler(Client client) {
        this.client = client;
    }

    public void handleResponse(Response response) throws IOException {
        if (response instanceof GetAllChannelsResponse) {
            System.out.print("ALL ROOMS IN CHAT: ");
            System.out.println(((GetAllChannelsResponse) response).getAllChannelsNames());
        }
        if (response instanceof JoinGroupChatResponse) {
            System.out.println(String.format("You have joined %s channel!", ((JoinGroupChatResponse) response).getChannelName()));
            while (client.getSocket().isConnected()) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                client.getBufferedWriter().write(client.getUsername() + ": " + message);
                //outputStreamWriter.writeObject(request);
                //outputStreamWriter.flush();
                //System.out.println("infinite loop");
                client.getBufferedWriter().newLine();
                client.getBufferedWriter().flush();
            }
        }

        if (response instanceof SendFileResponse) {

        }
    }



}
