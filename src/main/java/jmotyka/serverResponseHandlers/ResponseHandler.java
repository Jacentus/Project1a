package jmotyka.serverResponseHandlers;

import jmotyka.Client;
import jmotyka.ClientHandler;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler { // to działa po stronie KLIENTA!

    //private Client client;

    public ResponseHandler(Client client) {
        //this.client = client;
    }

    public void handleResponse(Response response) throws IOException {
        if (response instanceof GetAllChannelsResponse) { // działa
            System.out.print("ALL ROOMS IN CHAT: ");
            System.out.println(((GetAllChannelsResponse) response).getAllChannelsNames());
        }
        if (response instanceof JoinGroupChatResponse) { // działa
            System.out.println(String.format("You have joined %s channel!", ((JoinGroupChatResponse) response).getChannelName()));
        }
        if (response instanceof MessageResponse) { // działa
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
        if (response instanceof ErrorResponse) {
            System.out.println("ERROR || " + ((ErrorResponse) response).getMessage());
        }
        if (response instanceof SendFileResponse) { //działa
            System.out.println("A FILE HAS BEEN RECEIVED FROM " + ((SendFileResponse) response).getUserName());
            String filePath = "D:\\RECEIVED_FILES\\newFile.pdf";
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



