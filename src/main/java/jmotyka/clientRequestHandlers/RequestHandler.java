package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.chatHistoryReaderAndWriter.CacheHistorySaver;
import jmotyka.chatHistoryReaderAndWriter.ChatHistoryReader;
import jmotyka.chatHistoryReaderAndWriter.ChatHistorySaver;
import jmotyka.chatHistoryReaderAndWriter.FileHistoryReader;
import jmotyka.entities.Channel;
import jmotyka.exceptions.NoAccesToChannelException;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.exceptions.NoSuchChannelException;
import jmotyka.requests.*;
import jmotyka.responses.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler {

    private final ClientHandlersManager clientHandlersManager;
    private final ClientHandler clientHandler;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private ChatHistorySaver historySaver = new CacheHistorySaver();
    private ChatHistoryReader historyReader = new FileHistoryReader();

    protected static final Logger logger = Logger.getLogger(RequestHandler.class.getName()); // TODO: ukryć pod interfejsem

    public RequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler) {
        this.clientHandlersManager = clientHandlersManager;
        this.clientHandler = clientHandler;
    }

    public void handleRequest(HandleableRequest request) {
        switch (request.getRequestType()) {
            case MESSAGE:
                processRequest((MessageRequest) request);
                break;
            case GET_ALL_CHANNELS:
                processRequest((GetAllChannelsRequest) request);
                break;
            case GET_CHANNEL_HISTORY:
                processRequest((GetChannelHistoryRequest) request);
                break;
            case JOIN_PUBLIC_CHANNEL:
                processRequest((JoinPublicChannelRequest) request);
                break;
            case JOIN_PRIVATE_CHANNEL:
                processRequest((JoinPrivateChannelRequest) request);
                break;
            case CREATE_NEW_PRIVATE_CHANNEL:
                processRequest((CreatePrivateChannelRequest) request);
                break;
            case SEND_FILE_REQUEST:
                processRequest((SendFileRequest) request);
                break;
            case REMOVE_FROM_CHANNEL:
                processRequest((RemoveFromChannelRequest) request);
                break;
            case INTRODUCTION:
                processRequest((IntroductionRequest) request);
                break;
        }
    }

    public void processRequest(IntroductionRequest request) {
        clientHandler.setClientUsername(request.getUserName());
        logger.log(Level.INFO, "User has been matched with clientHandler");
    }

    public void processRequest(GetAllChannelsRequest request) {
        logger.log(Level.INFO, "returning list of available public channels...!");
        List<String> channelNames = new ArrayList<>();
        lock.readLock().lock();
        ClientHandlersManager.getMapOfAllChannels().forEach((k, v) -> {
            if (!v.isPrivate()) channelNames.add(k);
        });
        lock.readLock().unlock();
        GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(Response.ResponseType.GET_ALL_CHANNELS_RESPONSE, channelNames);
        if (getAllChannelsResponse.getAllChannelsNames() != null) {
            broadcastToSender(clientHandler, getAllChannelsResponse);
        } else {
            String error = "No channels avaialble. Create a new one!";
            broadcastToSender(clientHandler, new ErrorResponse(Response.ResponseType.ERROR, error));
        }
    }

    public void processRequest(CreatePrivateChannelRequest request) {
        logger.log(Level.INFO, "creating a private channel...!");
        if (clientHandlersManager.checkIfChannelAlreadyExists(request.getChannelName())) {
            broadcastToSender(clientHandler, new CreatePrivateChannelResponse(Response.ResponseType.CREATE_PRIVATE_CHANNEL_RESPONSE, false));
        } else {
            Channel channel = new Channel(request.getChannelName(), request.getIsPrivate(), request.getPermittedUsers());
            channel.getUsersInChannel().add(clientHandler);
            ClientHandlersManager.getMapOfAllChannels().put(channel.getChannelName(), channel);
            broadcastToSender(clientHandler, new CreatePrivateChannelResponse(Response.ResponseType.CREATE_PRIVATE_CHANNEL_RESPONSE, channel.getPermittedUsers(), true));
        }
    }

    public void processRequest(JoinPublicChannelRequest request) {
        logger.log(Level.INFO, "Joining to a public channel " + request.getChannelName());
        if (ClientHandlersManager.getMapOfAllChannels().containsKey(request.getChannelName())) {
            try {
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).addClientToChannel(clientHandler);
                JoinPublicChannelResponse joinPublicChannelResponse = new JoinPublicChannelResponse(Response.ResponseType.JOIN_PUBLIC_CHANNEL_RESPONSE, request.getChannelName());
                broadcastToSender(clientHandler, joinPublicChannelResponse);
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, new MessageResponse(Response.ResponseType.MESSAGE_RESPONSE, "SERVER", request.getUserName(), "HAS JOINED THE CHANNEL"));
                logger.log(Level.INFO, "Notifications has been send");
            } catch (NoAccesToChannelException exception) {
                broadcastToSender(clientHandler, new ErrorResponse(Response.ResponseType.ERROR, exception.getMessage()));
            }
        } else {
            lock.writeLock().lock(); //todo: upewnij się że to nie blokuje wątku
            try {
                Channel newChannel = new Channel(request.getChannelName());
                newChannel.getPermittedUsers().add(request.getUserName());
                newChannel.getUsersInChannel().add(clientHandler);
                ClientHandlersManager.getMapOfAllChannels().put(request.getChannelName(), newChannel);
                broadcastToSender(clientHandler, new JoinPublicChannelResponse(Response.ResponseType.JOIN_PUBLIC_CHANNEL_RESPONSE, request.getChannelName()));
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public void processRequest(MessageRequest request) {
        logger.log(Level.INFO, "Handling message request...!");
        MessageResponse messageResponse = new MessageResponse(Response.ResponseType.MESSAGE_RESPONSE, request.getUserName(), request.getChannelName(), request.getText());
        logger.log(Level.INFO, "proceeding to save public msg to chat history......!");
        historySaver.save(request, request.getChannelName()); //todo: zapisywanie do cache
        logger.log(Level.INFO, "msg saved to history!");
        ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, messageResponse);
        logger.log(Level.INFO, "Messages broadcasted");
    }

    public void processRequest(JoinPrivateChannelRequest request) {
        logger.log(Level.INFO, "Joining to a private chat " + request.getChannelName());
        if (ClientHandlersManager.getMapOfAllChannels().containsKey(request.getChannelName())) {
            try {
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).addClientToChannel(clientHandler);
                broadcastToSender(clientHandler, new JoinPrivateChannelResponse(Response.ResponseType.JOIN_PRIVATE_CHANNEL_RESPONSE, true, request.getChannelName()));
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, new MessageResponse(Response.ResponseType.MESSAGE_RESPONSE, "SERVER", request.getUserName(), "HAS JOINED THE CHANNEL"));
                logger.log(Level.INFO, "Notifications has been send");
            } catch (NoAccesToChannelException exception) {
                broadcastToSender(clientHandler, new JoinPrivateChannelResponse(Response.ResponseType.JOIN_PRIVATE_CHANNEL_RESPONSE, false, request.getChannelName()));
            }
        } else {
            broadcastToSender(clientHandler, new JoinPrivateChannelResponse(Response.ResponseType.JOIN_PRIVATE_CHANNEL_RESPONSE, false, request.getChannelName()));
        }
    }

    public void processRequest(RemoveFromChannelRequest request) {
        logger.log(Level.INFO, "Handling removing from channel request...!");
        logger.log(Level.INFO, "Removing " + request.getUserName() + " from " + request.getChannelName());
        logger.log(Level.INFO, "Channel from my channel manager: " + ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()));
        ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).remove(clientHandler);
        ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName())
                .broadcast(clientHandler, new MessageResponse(Response.ResponseType.MESSAGE_RESPONSE, "SERVER", clientHandler.getClientUsername(), "HAS LEFT THE CHANNEL"));
        logger.log(Level.INFO, "handling request to remove from channel has finished...!");
    }

    public static void broadcastToSender(ClientHandler clientHandler, Response response) {
        try {
            clientHandler.getObjectOutputStream().writeObject(response);
            clientHandler.getObjectOutputStream().flush();
            logger.log(Level.INFO, "MESSAGE HAS BEEN BROADCASTED");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    protected void processRequest(SendFileRequest request) {
        SendFileResponse sendFileResponse = new SendFileResponse(Response.ResponseType.SEND_FILE_RESPONSE, request.getUserName(), request.getFileName(), request.getByteFile());
        ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, sendFileResponse);
        logger.log(Level.INFO, "Finished distributing file");
    }


    public void processRequest(GetChannelHistoryRequest request) {
        logger.log(Level.INFO, "Trying to get channel history...");
        List<MessageRequest> channelHistory;
        try {
            channelHistory = historyReader.read(request.getUserName(), request.getChannelName());
            GetChannelHistoryResponse response = new GetChannelHistoryResponse(Response.ResponseType.GET_CHANNEL_HISTORY_RESPONSE, channelHistory);
            broadcastToSender(clientHandler, response);
        } catch (NoAccessToChatHistoryException | NoSuchChannelException e) {
            e.printStackTrace();
            String error = e.getMessage();
            broadcastToSender(clientHandler, new ErrorResponse(Response.ResponseType.ERROR, error));
        }
    }

}
