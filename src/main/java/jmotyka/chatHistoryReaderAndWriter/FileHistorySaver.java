package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.ClientHandlersManager;
import jmotyka.requests.MessageRequest;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class FileHistorySaver implements ChatHistorySaver {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger logger = Logger.getLogger(FileHistorySaver.class.getName()); // TODO: ukryć pod interfejsem

    /*public static void saveToCache(PrivateMessageRequest message) {
        try {
            lock.writeLock().lock();
            if (ClientHandlersManager.getHistory().getPrivateChatHistory().isEmpty()
                    || !ClientHandlersManager.getHistory().getPrivateChatHistory().containsKey(message.getChannel())) {
                ArrayList<MessageRequest> roomHistory = new ArrayList<>();
                roomHistory.add(message);
                ClientHandlersManager.getHistory().getPrivateChatHistory().put(message.getChannel(), roomHistory);
                logger.log(Level.INFO, String.format("History for a new room '%s' has been created", message.getChannel().getChannelName()));
            } else {
                ClientHandlersManager.getHistory().getPrivateChatHistory().get(message.getChannel()).add(message);
                logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), message.getChannel().getChannelName()));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }*/

   /* public static void saveToCache(PublicMessageRequest message) {
        try {
            lock.writeLock().lock();
            if (ClientHandlersManager.getHistory().getPublicChatHistory().isEmpty()
                    || !ClientHandlersManager.getHistory().getPublicChatHistory().containsKey(message.getChannel())) {
                ArrayList<MessageRequest> roomHistory = new ArrayList<>();
                roomHistory.add(message);
                ClientHandlersManager.getHistory().getPublicChatHistory().put(message.getChannel(), roomHistory);
                logger.log(Level.INFO, String.format("History for a new room '%s' has been created", message.getChannel()));
            } else {
                ClientHandlersManager.getHistory().getPublicChatHistory().get(message.getChannel()).add(message);
                logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), message.getChannel()));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }*/

}
