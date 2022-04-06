package jmotyka;

import jmotyka.entities.Channel;
import jmotyka.entities.ChatHistory;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandlersManager {

    //@Getter
    //private static final Map<String, List<ClientHandler>> mapOfAllPublicChannels = new HashMap<>();
    //@Getter
    //private static final Map<PrivateChannel, List<ClientHandler>> mapOfAllPrivateChannels = new HashMap<>();
    @Getter
    private static final Map<String, Channel> mapOfAllChannels = new HashMap<>();

    @Getter
    private static ChatHistory history;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem

    public ClientHandlersManager(ChatHistory history) {
        //mapOfAllPublicChannels.put("Warszawa", new ArrayList<>());
        //mapOfAllPublicChannels.put("Gdańsk", new ArrayList<>());
        //mapOfAllPublicChannels.put("Ogólny", new ArrayList<>());
        mapOfAllChannels.put("Ogólny", new Channel("Ogólny"));
        mapOfAllChannels.put("Warszawa", new Channel("Warszawa"));
        mapOfAllChannels.put("Gdańsk", new Channel("Gdańsk"));
        this.history = history;
    }

    public Boolean checkIfChannelAlreadyExists(String channelName){
        if (mapOfAllChannels.containsKey(channelName)) {return true;
        } else return false;
    }

/*    public void addClientToPublicChannel(ClientHandler clientHandler) {
        String channelName = clientHandler.getChannelName();
        lock.writeLock().lock();
        try {
            if (mapOfAllPublicChannels.isEmpty() || !mapOfAllPublicChannels.containsKey(clientHandler.getChannelName())) {
                addClientToNewRoom(channelName, clientHandler, mapOfAllPublicChannels);
            } else {
                mapOfAllPublicChannels.get(channelName).add(clientHandler);
                logger.log(Level.INFO, String.format("New user %s entered %s channel", clientHandler.getClientUsername(), channelName));
                logger.log(Level.INFO, String.format("%s active users in %s channel", mapOfAllPublicChannels.get(channelName).size(), channelName));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addClientToPrivateChannel(ClientHandler clientHandler) { // TODO: to na pewno można by ugenerycznić...
        PrivateChannel privateChannel = clientHandler.getPrivateChannel();
        logger.log(Level.INFO, "Setting clients in private channel. Permitted users: " + clientHandler.getPrivateChannel().getPermittedUsers());
        lock.writeLock().lock();
        try {
            if (mapOfAllPrivateChannels.isEmpty() || !mapOfAllPrivateChannels.containsKey(clientHandler.getPrivateChannel())) {
                ArrayList<ClientHandler> room = new ArrayList<>();
                room.add(clientHandler);
                mapOfAllPrivateChannels.put(privateChannel, room);
                logger.log(Level.INFO, String.format("New room %s has been created", clientHandler.getPrivateChannel().getChannelName()));
                logger.log(Level.INFO, String.format("%s active users in %s channel", room.size(), privateChannel));
            } else {
                mapOfAllPrivateChannels.get(privateChannel).add(clientHandler);
                logger.log(Level.INFO, String.format("New user %s entered %s channel", clientHandler.getClientUsername(), privateChannel.getChannelName()));
                logger.log(Level.INFO, String.format("%s active users in %s channel", mapOfAllPrivateChannels.get(privateChannel).size(), privateChannel));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addClientToNewRoom(String roomName, ClientHandler clientHandler, Map<String, List<ClientHandler>> rooms) {
        ArrayList<ClientHandler> room = new ArrayList<>();
        room.add(clientHandler);
        lock.writeLock().lock();
        try {
            rooms.put(roomName, room);
            logger.log(Level.INFO, String.format("New room %s has been created", clientHandler.getChannelName()));
            logger.log(Level.INFO, String.format("%s active users in %s channel", room.size(), roomName));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(ClientHandler clientHandler) { // TODO: to na pewno można by ugenerycznić...
        logger.log(Level.INFO, String.format("Removing %s from  %s channel", clientHandler.getClientUsername(), clientHandler.getChannelName()));
        lock.writeLock().lock();
        try {
            mapOfAllPublicChannels.get(clientHandler.getChannelName()).remove(clientHandler);
            logger.log(Level.INFO, String.format("Client %s left %s channel", clientHandler.getClientUsername(), clientHandler.getChannelName()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(ClientHandler clientHandler, PrivateChannel channel) {
        logger.log(Level.INFO, String.format("Removing %s from  %s private channel", clientHandler.getClientUsername(), channel.getChannelName()));
        lock.writeLock().lock();
        try {
            mapOfAllPrivateChannels.get(channel).remove(clientHandler);
            logger.log(Level.INFO, String.format("Client %s left %s channel", clientHandler.getClientUsername(), clientHandler.getChannelName()));
        } finally {
            lock.writeLock().unlock();
        }
    }*/

}
