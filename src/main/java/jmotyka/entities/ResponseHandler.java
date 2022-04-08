package jmotyka.entities;

import jmotyka.requests.*;
import jmotyka.responses.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ResponseHandler {

    private Client client;

    public ResponseHandler(Client client) {
        this.client = client;
    }

    public void handleResponse(Response response) {
        switch (response.getResponseType()) {
            case GET_ALL_CHANNELS_RESPONSE:
                processResponse((GetAllChannelsResponse) response);
                break;
            case CREATE_PRIVATE_CHANNEL_RESPONSE:
                processResponse((CreatePrivateChannelResponse) response);
                break;
            case ERROR:
                processResponse((ErrorResponse) response);
                break;
            case GET_CHANNEL_HISTORY_RESPONSE:
                processResponse((GetChannelHistoryResponse) response);
                break;
            case JOIN_PRIVATE_CHANNEL_RESPONSE:
                processResponse((JoinPrivateChannelResponse) response);
                break;
            case JOIN_PUBLIC_CHANNEL_RESPONSE:
                processResponse((JoinPublicChannelResponse) response);
                break;
            case MESSAGE_RESPONSE:
                processResponse((MessageResponse) response);
                break;
            case SEND_FILE_RESPONSE:
                processResponse((SendFileResponse) response);
                break;
        }
    }

    public void processResponse(GetAllChannelsResponse response) {
        client.getLock().getServerResponseLock().lock();
        try {
            System.out.print("ALL PUBLIC CHANNELS AVAILABLE: ");
            System.out.println(response.getAllChannelsNames());
            client.getLock().getResponseHandled().signal();
        } finally {
            client.getLock().getServerResponseLock().unlock();
        }
    }

    public void processResponse(CreatePrivateChannelResponse response) {
        client.getLock().getServerResponseLock().lock();
        try {
            client.setIsPermittedToChat(response.getIsPermitted());
            if (client.getIsPermittedToChat()) {
                System.out.println("Private channel has been created! Waiting for " + (response.getPermittedUsers()) + " to join...");
            } else {
                System.out.println("Such channel already exists! Use join private channel option");
            }
            client.getLock().getResponseHandled().signal();
        } finally {
            client.getLock().getServerResponseLock().unlock();
        }
    }

    public void processResponse(JoinPublicChannelResponse response) {
        client.getLock().getServerResponseLock().lock();
        try {
            System.out.println(String.format("You have joined %s channel!", (response).getChannelName()));
            client.getLock().getResponseHandled().signal();
        } finally {
            client.getLock().getServerResponseLock().unlock();
        }
    }

    public void processResponse(MessageResponse response) {
        System.out.println(response);
    }

    public void processResponse(JoinPrivateChannelResponse response) {
        client.getLock().getServerResponseLock().lock();
        try {
            System.out.println(String.format("You tried to join %s channel!", response.getChannelName()));
            client.setIsPermittedToChat(response.getIsPermitted());
            if (!client.getIsPermittedToChat()) {
                System.out.println("You are not allowed to join that channel.");
            }
            client.getLock().getResponseHandled().signal();
        } finally {
            client.getLock().getServerResponseLock().unlock();
        }
    }

    public void processResponse(ErrorResponse response) {
        client.getLock().getServerResponseLock().lock();
        try {
            System.out.println("ERROR || " + response.getMessage());
            client.getLock().getResponseHandled().signal();
        } finally {
            client.getLock().getServerResponseLock().unlock();
        }
    }

    public void processResponse(SendFileResponse response) {
        String filePath = "D:\\RECEIVED_FILES\\" + response.getFileName();
        System.out.println("path: " + filePath);
        File file = new File(filePath);
        try {
            OutputStream os = new FileOutputStream(file);
            os.write((response).getFile());
            os.close();
            System.out.println("A FILE HAS BEEN SUCCESSFULLY RECEIVED FROM " + response.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processResponse(GetChannelHistoryResponse response) {
        client.getLock().getServerResponseLock().lock();
        List<MessageRequest> channelHistory = response.getChatHistory();
        try {
            for (MessageRequest message : channelHistory) {
                System.out.println(message);
            }
            client.getLock().getResponseHandled().signal();
        } finally {
            client.getLock().getServerResponseLock().unlock();
        }
    }

}