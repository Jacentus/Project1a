package jmotyka.chathistoryreaderandwriter;

import jmotyka.ClientHandlers;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.PrivateMessageRequest;
import jmotyka.requests.PublicMessageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class FileHistorySaver implements ChatHistorySaver {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger logger = Logger.getLogger(FileHistorySaver.class.getName()); // ukryć pod interfejsem

    public static void saveToCache(PrivateMessageRequest message) {
        try {
            lock.writeLock().lock();
            logger.log(Level.INFO, String.format("Saving private message"));
            if (ClientHandlers.getHistory().getPrivateChatHistory().isEmpty() || !ClientHandlers.getHistory().getPrivateChatHistory().containsKey(message.getChannel())) {
                ArrayList<MessageRequest> roomHistory = new ArrayList<>();
                roomHistory.add(message);
                ClientHandlers.getHistory().getPrivateChatHistory().put(message.getChannel(), roomHistory);
                logger.log(Level.INFO, String.format("History for a new room '%s' has been created", message.getChannel().getChannelName()));
            } else {
                ClientHandlers.getHistory().getPrivateChatHistory().get(message.getChannel()).add(message);
                logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), message.getChannel().getChannelName()));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void saveToCache(PublicMessageRequest message) {
        try {
            lock.writeLock().lock();
            logger.log(Level.INFO, String.format("Saving public message"));
            if (ClientHandlers.getHistory().getPublicChatHistory().isEmpty() || !ClientHandlers.getHistory().getPublicChatHistory().containsKey(message.getChannel())) {
                ArrayList<MessageRequest> roomHistory = new ArrayList<>();
                roomHistory.add(message);
                ClientHandlers.getHistory().getPublicChatHistory().put(message.getChannel(), roomHistory);
                logger.log(Level.INFO, String.format("History for a new room '%s' has been created", message.getChannel()));
            } else {
                ClientHandlers.getHistory().getPublicChatHistory().get(message.getChannel()).add(message);
                logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), message.getChannel()));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

}
