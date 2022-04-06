package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.chatHistoryReaderAndWriter.FileHistoryReader;
import jmotyka.chatHistoryReaderAndWriter.FileHistorySaver;
import jmotyka.entities.PrivateChannel;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.*;
import jmotyka.responses.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler {

    private final ClientHandlersManager clientHandlersManager;
    private final ClientHandler clientHandler;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    //private FileHistorySaver historySaver;

    protected static final Logger logger = Logger.getLogger(RequestHandler.class.getName()); // TODO: ukryć pod interfejsem

    public RequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler) {
        this.clientHandlersManager = clientHandlersManager;
        this.clientHandler = clientHandler;
        //this.historySaver = new FileHistorySaver(clientHandlersManager);
    }

    public void handleRequest(HandleableRequest request) {
        clientHandler.setClientUsername(request.getUserName());
        switch (request.getRequestType()) {
            case PUBLIC_MESSAGE:
                processRequest((PublicMessageRequest) request);
                break;
            case PRIVATE_MESSAGE:
                processRequest((PrivateMessageRequest) request);
                break;
            case GET_ALL_CHANNELS:
                processRequest((GetAllChannelsRequest) request);
                break;
            case GET_PUBLIC_CHANNEL_HISTORY:
                processRequest((GetPublicChannelHistoryRequest) request);
                break;
            case GET_PRIVATE_CHANNEL_HISTORY:
                processRequest((GetPrivateChannelHistoryRequest) request);
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
            case SEND_FILE_PUBLICLY:
                processRequest((SendFilePubliclyRequest) request);
                break;
            case SEND_FILE_PRIVATELY:
                processRequest((SendFilePrivatelyRequest) request);
                break;
            case REMOVE_FROM_PUBLIC_CHANNEL:
                processRequest((RemoveFromPublicChannelRequest) request);
                break;
            case REMOVE_FROM_PRIVATE_CHANNEL:
                processRequest((RemoveFromPrivateChannelRequest) request);
                break;
        }
    }

    public void processRequest(GetAllChannelsRequest request) {
        logger.log(Level.INFO, "returning list of available public channels...!");
        List<String> channelNames = new ArrayList<>();
        lock.readLock().lock();
        ClientHandlersManager.getMapOfAllPublicChannels().forEach((k, v) -> channelNames.add(k));
        lock.readLock().unlock();
        GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);
        if (getAllChannelsResponse.getAllChannelsNames() != null) {
            broadcast(clientHandler, getAllChannelsResponse);
        } else {
            String error = "No channels avaialble. Create a new one!";
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }

    public void processRequest(CreatePrivateChannelRequest request){
        logger.log(Level.INFO, "creating a private channel...!");
        if (privateChannelAlreadyExists(request)) {
            broadcast(clientHandler, new CreatePrivateChatResponse(request.getPrivateChannel().getPermittedUsers(), false));
        } else {
            clientHandler.setPrivateChannel(request.getPrivateChannel());
            clientHandlersManager.addClientToPrivateChannel(clientHandler);
            CreatePrivateChatResponse response = new CreatePrivateChatResponse(request.getPrivateChannel().getPermittedUsers(), true);
            broadcast(clientHandler, response);
        }
    }

    public void processRequest(JoinPublicChannelRequest request) {
        logger.log(Level.INFO, "Joining to a group chat...!");
        clientHandler.setChannelName(request.getChannelName());
        logger.log(Level.INFO, "Open channel: " + request.getChannelName());
        clientHandlersManager.addClientToPublicChannel(clientHandler);
        JoinPublicChatResponse joinPublicChatResponse = new JoinPublicChatResponse(request.getChannelName());
        broadcast(clientHandler, joinPublicChatResponse);
        //TODO: mogę wyciąć to do metody np. notifyOtherUsers
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(clientHandler.getChannelName());
        lock.readLock().unlock();
        for (ClientHandler client : addressees) {
            if(request.getUserName() != client.getClientUsername()) {
                broadcast(client, new PublicMessageResponse("SERVER: ", request.getUserName(), "HAS ENTERED THE CHANNEL!"));
                logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
            }
        }
    }

    public void processRequest(JoinPrivateChannelRequest request) {
        logger.log(Level.INFO, "Joining to a private chat...!");
        PrivateChannel isPermitted = checkIfPermittedToJoin(request); //TODO: to nie jest fortunne, mógłbym to zmienić na boolean
        if (isPermitted == null) {
            broadcast(clientHandler, new JoinPrivateChatResponse(request.getPrivateChannel(), false));
        } else if (isPermitted instanceof PrivateChannel) {
            lock.readLock().lock();
            List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(request.getPrivateChannel());
            lock.readLock().unlock();
            //TODO: mogę wyciąć to do metody np. notifyOtherUsers
            for (ClientHandler client : addressees) {
                broadcast(client, new PublicMessageResponse("SERVER: ", request.getUserName(), "HAS JOINED THE CHANNEL!"));
                logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
            }
            clientHandler.setPrivateChannel(isPermitted);
            clientHandler.getPrivateChannel().setClientPermittedToChat(true);
            clientHandlersManager.addClientToPrivateChannel(clientHandler);
            JoinPrivateChatResponse response = new JoinPrivateChatResponse(clientHandler.getPrivateChannel(), true);
            broadcast(clientHandler, response);
        }
    }

    public void processRequest(RemoveFromPrivateChannelRequest request) {
        logger.log(Level.INFO, "Handling removing from private channel request...!");
        clientHandlersManager.remove(clientHandler, request.getChannel());
        MessageResponse messageResponse = new PublicMessageResponse("SERVER: " + request.getUserName() ,request.getChannel().getChannelName()," HAS LEFT THE CHANNEL!");
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(request.getChannel());
        lock.readLock().unlock();
        //TODO: mogę wyciąć to do metody np. notifyOtherUsers
        for (ClientHandler client : addressees) {
            broadcast(client, messageResponse);
            logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
        }
        logger.log(Level.INFO, "handling request to remove from channel has finished...!");
    }

    public void processRequest(RemoveFromPublicChannelRequest request) {
        logger.log(Level.INFO, "Handling removing from channel request...!");
        clientHandlersManager.remove(clientHandler);
        MessageResponse messageResponse = new PublicMessageResponse("SERVER: " + request.getUserName() ,request.getChannelName()," HAS LEFT THE CHANNEL!");
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(request.getChannelName());
        lock.readLock().unlock();
        for (ClientHandler client : addressees) {
            broadcast(client, messageResponse);
            logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
        }
        logger.log(Level.INFO, "handling request to remove from channel has finished...!");
    }

    protected void processRequest(SendFilePubliclyRequest request) { //TODO: na pewno mogę to ugenerycznić
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(request.getChannel());
        lock.readLock().unlock();
        SendFileResponse sendFileResponse = new SendFileResponse(request.getUserName(), request.getFileName(), request.getByteFile());
        for (ClientHandler client : addressees) { //TODO: TO chyba mógłbym zastąpić usunięciem danego adresata z listy, aby nie robić tylu ifów
            if (request.getUserName() != client.getClientUsername()) {
                broadcast(client, sendFileResponse);
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
    }

    protected void processRequest(SendFilePrivatelyRequest request) { //TODO: na pewno mogę to ugenerycznić
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(request.getChannel());
        lock.readLock().unlock();
        SendFileResponse sendFileResponse = new SendFileResponse(request.getUserName(), request.getFileName(), request.getByteFile());
        for (ClientHandler client : addressees) {
            if (request.getUserName() != client.getClientUsername()) { //TODO: TO chyba mógłbym zastąpić usunięciem danego adresata z listy, aby nie robić tylu ifów
                broadcast(client, sendFileResponse);
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
    }

    public void processRequest(GetPrivateChannelHistoryRequest request) {
        logger.log(Level.INFO, "Trying to get private channel history...");
        FileHistoryReader reader = new FileHistoryReader();
        List<MessageRequest> channelHistory = null;
        try {
            channelHistory = reader.readFromCache(request.getUserName(), request.getChannel());
            GetChatHistoryResponse response = new GetChatHistoryResponse(channelHistory);
            broadcast(clientHandler, response);
        } catch (NoAccessToChatHistoryException e) {
            e.printStackTrace();
            String error = e.getMessage();
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }

    public void processRequest(GetPublicChannelHistoryRequest request) {
        logger.log(Level.INFO, "Trying to get channel history...");
        List<MessageRequest> channelHistory = null;
        FileHistoryReader reader = new FileHistoryReader();
        try{
            channelHistory = reader.readFromCache(request.getUserName(), request.getChannel());
            GetChatHistoryResponse response = new GetChatHistoryResponse(channelHistory);
            broadcast(clientHandler, response);
        } catch (NoAccessToChatHistoryException e) {
            e.printStackTrace();
            String error = e.getMessage();
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }

    public void processRequest(PublicMessageRequest request) {
            logger.log(Level.INFO, "Handling public message request...!");
            MessageResponse messageResponse = new PublicMessageResponse(request.getUserName(), request.getChannel(), request.getText());
            logger.log(Level.INFO, "proceeding to save public msg to chat history......!");
            // todo: do poprawki na interfejs
            FileHistorySaver.saveToCache(request);
            logger.log(Level.INFO, "msg saved to history!");
            List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(clientHandler.getChannelName());
            for (ClientHandler client : addressees) {
                if(request.getUserName() != client.getClientUsername()) { //TODO: TO chyba mógłbym zastąpić usunięciem danego adresata z listy, aby nie robić tylu ifów
                    broadcast(client, messageResponse);
                    logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
                }
            }
        }

        public void processRequest (PrivateMessageRequest request){
            MessageResponse messageResponse = new PrivateMessageResponse(request.getUserName(), request.getChannel(), request.getText());
            logger.log(Level.INFO, "saving private message to chat history");

            FileHistorySaver.saveToCache(request); // TODO: tu jest problem

            logger.log(Level.INFO, "msg saved to history!");
            List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(clientHandler.getPrivateChannel());
            for (ClientHandler client : addressees) {
                if(request.getUserName() != client.getClientUsername()) { //TODO: TO chyba mógłbym zastąpić usunięciem danego adresata z listy, aby nie robić tylu ifów
                    broadcast(client, messageResponse);
                    logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
                }
            }
        }

    //todo: przenieść gdzie indziej i zrobić statycznymi? A co z dostępem do pól z handlera?
    ////////////////////////////////////////////////////// UTILS - MOVE TO SUBCLASS ////////////////////////////////////////////////////////////////////////////////////////

    public Boolean privateChannelAlreadyExists(CreatePrivateChannelRequest request) {
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
    }

    public void broadcast(ClientHandler clientHandler, Response response) {
        try {
            clientHandler.getObjectOutputStream().writeObject(response);
            clientHandler.getObjectOutputStream().flush();
            logger.log(Level.INFO, "MESSAGE HAS BEEN BROADCASTED");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}