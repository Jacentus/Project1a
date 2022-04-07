package jmotyka;

import jmotyka.entities.Channel;
import jmotyka.entities.ChatHistory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandlersManager {

    @Getter @Setter
    private static Map<String, Channel> mapOfAllChannels = new HashMap<>();

    @Getter
    private static ChatHistory history;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem

    public ClientHandlersManager(ChatHistory history) {
        //mapOfAllChannels.put("Ogólny", new Channel("Ogólny"));
        //mapOfAllChannels.put("Warszawa", new Channel("Warszawa"));
        //mapOfAllChannels.put("Gdańsk", new Channel("Gdańsk"));
        this.history = history;
    }

    public Boolean checkIfChannelAlreadyExists(String channelName) {
        if (mapOfAllChannels.containsKey(channelName)) {
            return true;
        } else return false;
    }

}
