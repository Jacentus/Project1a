package jmotyka;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;
import lombok.extern.java.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandlers implements Serializable {

    @Getter
    private static final Map<String, List<ClientHandler>> mapOfAllRooms = new TreeMap<>(); //dodałem static, czy to możliwe że to ten problem?

    @Getter
    private static final Map<List<PrivateChannel>, List<ClientHandler>> mapOfPrivateRooms = new TreeMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public ClientHandlers() {
        mapOfAllRooms.put("warszawa", new ArrayList<>());
        mapOfAllRooms.put("gdansk", new ArrayList<>());
        mapOfAllRooms.put("ogolny", new ArrayList<>());
    }

    public void addClientToOpenChannel(ClientHandler clientHandler) {
        String roomName = clientHandler.getChannelName();
        lock.writeLock().lock();
        if(mapOfAllRooms.isEmpty() || !mapOfAllRooms.containsKey(clientHandler.getChannelName())) {
            addClientToNewRoom(roomName, clientHandler, mapOfAllRooms);
        } else {
            mapOfAllRooms.get(roomName).add(clientHandler);
            logger.log(Level.INFO, String.format("New user %s entered %s channel", clientHandler.getClientUsername(), roomName));
            logger.log(Level.INFO, String.format("%s active users in %s channel", mapOfAllRooms.get(roomName).size(), roomName));
        }
        lock.writeLock().unlock();
    }

    public void addClientToPrivateChannel(ClientHandler clientHandler) {
        String roomName = clientHandler.getChannelName();
        lock.writeLock().lock();
        if(mapOfPrivateRooms.isEmpty() || !mapOfPrivateRooms.containsKey(clientHandler.getChannelName())) {
            addClientToNewRoom(roomName, clientHandler, mapOfPrivateRooms);
 /*           ArrayList<ClientHandler> room = new ArrayList<>();
            room.add(clientHandler);
            mapOfAllRooms.put(roomName, room);
            logger.log(Level.INFO, String.format("New room %s has been created", clientHandler.getChannelName()));
            logger.log(Level.INFO, String.format("%s active users in %s channel", room.size(), roomName));*/
        } else {
            mapOfPrivateRooms.get(roomName).add(clientHandler);
            logger.log(Level.INFO, String.format("New user %s entered %s channel", clientHandler.getClientUsername(), roomName));
            logger.log(Level.INFO, String.format("%s active users in %s channel", mapOfAllRooms.get(roomName).size(), roomName));
        }
        lock.writeLock().unlock();
    }

    public void addClientToNewRoom(String roomName, ClientHandler clientHandler, Map<String, List<ClientHandler>> rooms){
        ArrayList<ClientHandler> room = new ArrayList<>();
        room.add(clientHandler);
        rooms.put(roomName, room);
        logger.log(Level.INFO, String.format("New room %s has been created", clientHandler.getChannelName()));
        logger.log(Level.INFO, String.format("%s active users in %s channel", room.size(), roomName));
    }

    public void remove(ClientHandler clientHandler) {
        logger.log(Level.INFO, String.format("Removing %s from  %s channel", clientHandler.getClientUsername(), clientHandler.getChannelName()));
        lock.writeLock().lock();
        mapOfAllRooms.get(clientHandler.getChannelName()).remove(clientHandler); // weź pokój -> wyrzuć z pokoju
        logger.log(Level.INFO, String.format("Client %s left %s channel", clientHandler.getClientUsername(), clientHandler.getChannelName()));
        lock.writeLock().unlock();
    }

}
