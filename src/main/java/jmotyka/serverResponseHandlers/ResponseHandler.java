package jmotyka.serverResponseHandlers;

import jmotyka.Client;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler { //TODO: zastanów się, czy nie podzielić tego na osobne klasy i zrobić fabryki

    private Client client;

    public ResponseHandler(Client client) {
        this.client = client;
    }

    public void handleResponse(Response response) throws IOException {
        if (response instanceof GetAllChannelsResponse) {
            client.getLock().getServerResponseLock().lock();
            try {
                System.out.print("ALL PUBLIC CHANNELS AVAILABLE: ");
                System.out.println(((GetAllChannelsResponse) response).getAllChannelsNames());
                client.getLock().getResponseHandled().signal();
            } finally {
                client.getLock().getServerResponseLock().unlock();
            }
        }
        if (response instanceof JoinPublicChatResponse) {
            client.getLock().getServerResponseLock().lock();
            try {
                System.out.println(String.format("You have joined %s channel!", ((JoinPublicChatResponse) response).getChannelName()));
                client.getLock().getResponseHandled().signal();
            } finally {
                client.getLock().getServerResponseLock().unlock();
            }
        }
        if (response instanceof JoinPrivateChatResponse) {
            client.getLock().getServerResponseLock().lock();
            try {
                System.out.println(String.format("\nYou tried to join %s channel!", ((JoinPrivateChatResponse) response).getChannel().getChannelName()));
                client.setPrivateChannel(((JoinPrivateChatResponse) response).getChannel());
                client.getPrivateChannel().setClientPermittedToChat(((JoinPrivateChatResponse) response).getIsPermitted());
                client.getLock().getResponseHandled().signal();
            } finally {
                client.getLock().getServerResponseLock().unlock();
            }
        }
        if (response instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) response;
            System.out.println(messageResponse);
        }
        if (response instanceof GetChatHistoryResponse) {
            client.getLock().getServerResponseLock().lock();
            try {
                GetChatHistoryResponse getChatHistoryResponse = (GetChatHistoryResponse) response;
                for (MessageRequest message : getChatHistoryResponse.getChatHistory()) {
                    System.out.println(message);
                }
                client.getLock().getResponseHandled().signal();
            } finally {
                client.getLock().getServerResponseLock().unlock();
            }
        }
        if (response instanceof CreatePrivateChatResponse) {
            client.getLock().getServerResponseLock().lock();
            try {
                client.getPrivateChannel().setClientPermittedToChat(((CreatePrivateChatResponse) response).getIsPermitted());
                if (client.getPrivateChannel().getClientPermittedToChat()) {
                    System.out.println("Private channel created! Waiting for " + ((CreatePrivateChatResponse) response).getPermittedUsers() + " to join...");
                } else {
                    System.out.println("Such channel already exists and you are not permitted to join it!");
                }
                client.getLock().getResponseHandled().signal();
            } finally {
                client.getLock().getServerResponseLock().unlock();
            }
        }
        if (response instanceof ErrorResponse) {
            System.out.println("ERROR || " + ((ErrorResponse) response).getMessage());
        }
        if (response instanceof SendFileResponse) {
            String filePath = "D:\\RECEIVED_FILES\\" + ((SendFileResponse) response).getFileName();
            System.out.println("path: " + filePath);
            File file = new File(filePath);
            try {
                OutputStream os = new FileOutputStream(file);
                os.write(((SendFileResponse) response).getFile());
                os.close();
                System.out.println("A FILE HAS BEEN SUCCESSFULLY RECEIVED FROM " + ((SendFileResponse) response).getUserName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



