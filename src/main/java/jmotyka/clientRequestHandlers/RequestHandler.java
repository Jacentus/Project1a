package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.chatHistoryReaderAndWriter.FileHistoryReader;
import jmotyka.chatHistoryReaderAndWriter.FileHistorySaver;
import jmotyka.entities.Channel;
import jmotyka.exceptions.NoAccesToChannelException;
import jmotyka.exceptions.NoAccessToChatHistoryException;
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
/*            case GET_PUBLIC_CHANNEL_HISTORY:
                processRequest((GetPublicChannelHistoryRequest) request);
                break;
            case GET_PRIVATE_CHANNEL_HISTORY:
                processRequest((GetPrivateChannelHistoryRequest) request);
                break;*/
            case JOIN_PUBLIC_CHANNEL:
                processRequest((JoinPublicChannelRequest) request);
                break;
            case JOIN_PRIVATE_CHANNEL:
                processRequest((JoinPrivateChannelRequest) request);
                break;
            case CREATE_NEW_PRIVATE_CHANNEL:
                processRequest((CreatePrivateChannelRequest) request);
                break;
 /*           case SEND_FILE_PUBLICLY:
                processRequest((SendFilePubliclyRequest) request);
                break;
            case SEND_FILE_PRIVATELY:
                processRequest((SendFilePrivatelyRequest) request);
                break;*/
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
        lock.readLock().lock();
        lock.writeLock().lock();
        if (ClientHandlersManager.getMapOfAllChannels().containsKey(request.getChannelName())) {
            try {
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).addClientToChannel(clientHandler);
                JoinPublicChannelResponse joinPublicChannelResponse = new JoinPublicChannelResponse(Response.ResponseType.JOIN_PUBLIC_CHANNEL_RESPONSE, request.getChannelName());
                broadcastToSender(clientHandler, joinPublicChannelResponse);
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, new MessageResponse(Response.ResponseType.MESSAGE_RESPONSE, "SERVER", request.getUserName(), "HAS JOINED THE CHANNEL"));
                logger.log(Level.INFO, "Notifications has been send");
            } catch (NoAccesToChannelException exception) {
                broadcastToSender(clientHandler, new ErrorResponse(Response.ResponseType.ERROR, exception.getMessage()));
            } finally {
                lock.readLock().unlock();
                lock.writeLock().unlock();
            }
        } else {
            lock.writeLock().lock();
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
        // todo: do poprawki na interfejs
        //FileHistorySaver.saveToCache(request); //todo: zapisywanie do pliku
        logger.log(Level.INFO, "msg saved to history!");
        ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, messageResponse);
        logger.log(Level.INFO, "Messages broadcasted");
    }

    public void processRequest(JoinPrivateChannelRequest request) {
        logger.log(Level.INFO, "Joining to a private chat " + request.getChannelName());
        lock.readLock().lock();
        lock.writeLock().lock();
        if (ClientHandlersManager.getMapOfAllChannels().containsKey(request.getChannelName())) {
            try {
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).addClientToChannel(clientHandler);
                broadcastToSender(clientHandler, new JoinPrivateChannelResponse(Response.ResponseType.JOIN_PRIVATE_CHANNEL_RESPONSE, true, request.getChannelName()));
                ClientHandlersManager.getMapOfAllChannels().get(request.getChannelName()).broadcast(clientHandler, new MessageResponse(Response.ResponseType.MESSAGE_RESPONSE, "SERVER", request.getUserName(), "HAS JOINED THE CHANNEL"));
                logger.log(Level.INFO, "Notifications has been send");
            } catch (NoAccesToChannelException exception) {
                broadcastToSender(clientHandler, new JoinPrivateChannelResponse(Response.ResponseType.JOIN_PRIVATE_CHANNEL_RESPONSE, false, request.getChannelName()));
            } finally {
                lock.readLock().unlock();
                lock.writeLock().unlock();
            }
        } else {
            broadcastToSender(clientHandler, new JoinPrivateChannelResponse(Response.ResponseType.JOIN_PRIVATE_CHANNEL_RESPONSE, false, request.getChannelName()));
        }
    }

    public void processRequest(RemoveFromChannelRequest request) {
        logger.log(Level.INFO, "Handling removing from channel request...!");
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


/*    protected void processRequest(SendFilePubliclyRequest request) { //TODO: na pewno mogę to ugenerycznić
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(request.getChannel());
        lock.readLock().unlock();
        SendFileResponse sendFileResponse = new SendFileResponse(request.getUserName(), request.getFileName(), request.getByteFile());
        for (ClientHandler client : addressees) { //TODO: TO chyba mógłbym zastąpić usunięciem danego adresata z listy, aby nie robić tylu ifów
            if (request.getUserName() != client.getClientUsername()) {
                broadcastToSender(client, sendFileResponse);
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
    }*/

/*    protected void processRequest(SendFilePrivatelyRequest request) { //TODO: na pewno mogę to ugenerycznić
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(request.getChannel());
        lock.readLock().unlock();
        SendFileResponse sendFileResponse = new SendFileResponse(request.getUserName(), request.getFileName(), request.getByteFile());
        for (ClientHandler client : addressees) {
            if (request.getUserName() != client.getClientUsername()) { //TODO: TO chyba mógłbym zastąpić usunięciem danego adresata z listy, aby nie robić tylu ifów
                broadcastToSender(client, sendFileResponse);
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
    }*/

/*    public void processRequest(GetPrivateChannelHistoryRequest request) {
        logger.log(Level.INFO, "Trying to get private channel history...");
        FileHistoryReader reader = new FileHistoryReader();
        List<MessageRequest> channelHistory = null;
        try {
            channelHistory = reader.readFromCache(request.getUserName(), request.getChannel());
            GetChannelHistoryResponse response = new GetChannelHistoryResponse(channelHistory);
            broadcastToSender(clientHandler, response);
        } catch (NoAccessToChatHistoryException e) {
            e.printStackTrace();
            String error = e.getMessage();
            broadcastToSender(clientHandler, new ErrorResponse(error));
        }
    }*/

/*    public void processRequest(GetPublicChannelHistoryRequest request) {
        logger.log(Level.INFO, "Trying to get channel history...");
        List<MessageRequest> channelHistory = null;
        FileHistoryReader reader = new FileHistoryReader();
        try {
            channelHistory = reader.readFromCache(request.getUserName(), request.getChannel());
            GetChannelHistoryResponse response = new GetChannelHistoryResponse(channelHistory);
            broadcastToSender(clientHandler, response);
        } catch (NoAccessToChatHistoryException e) {
            e.printStackTrace();
            String error = e.getMessage();
            broadcastToSender(clientHandler, new ErrorResponse(error));
        }
    }*/
//todo: przenieść gdzie indziej i zrobić statycznymi? A co z dostępem do pól z handlera?
////////////////////////////////////////////////////// UTILS - MOVE TO SUBCLASS ////////////////////////////////////////////////////////////////////////////////////////

/*    public Boolean privateChannelAlreadyExists(CreatePrivateChannelRequest request) {
        lock.readLock().lock();
        Set<PrivateChannel> allPrivateChannels = ClientHandlersManager.getMapOfAllPrivateChannels().keySet();
        lock.readLock().unlock();
        Boolean exists = false;
        for (PrivateChannel channel : allPrivateChannels) {
            if (channel.equals(request.getPrivateChannel())) {
                logger.log(Level.INFO, "a matching private channel has been found");
                exists = true;
            }
        }
        return exists;
    }

    public PrivateChannel checkIfPermittedToJoin(JoinPrivateChannelRequest request) {
        lock.readLock().lock();
        Set<PrivateChannel> allPrivateChannels = ClientHandlersManager.getMapOfAllPrivateChannels().keySet();
        lock.readLock().unlock();
        Boolean isPermitted = false;
        for (PrivateChannel channel : allPrivateChannels) {
            if (channel.equals(request.getPrivateChannel())) {
                logger.log(Level.INFO, "a matching private channel has been found");
                if (channel.getPermittedUsers().contains(request.getUserName())) {
                    logger.log(Level.INFO, "a matching user has been found");
                    return channel;
                }
            }
        }
        logger.log(Level.INFO, "no match has been found...");
        return null;
    }*/

}