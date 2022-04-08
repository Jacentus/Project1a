package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.ClientHandlersManager;
import jmotyka.requests.MessageRequest;
import lombok.extern.java.Log;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class CacheHistorySaver implements ChatHistorySaver {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger logger = Logger.getLogger(CacheHistorySaver.class.getName());

    public void save(MessageRequest message, String channelName) {
        try {
            lock.writeLock().lock();
            ClientHandlersManager.getMapOfAllChannels().get(channelName).getChannelHistory().add(message);
            logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), message.getChannelName()));
        } finally {
            lock.writeLock().unlock();
        }
    }

}