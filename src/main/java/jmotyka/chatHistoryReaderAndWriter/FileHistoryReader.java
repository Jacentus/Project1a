package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.ClientHandlersManager;
import jmotyka.entities.ChatHistory;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.MessageRequest;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHistoryReader implements ChatHistoryReader {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem

    public List<MessageRequest> readFromCache(String userName, String channelName) throws NoAccessToChatHistoryException {
        try {
            logger.log(Level.INFO, String.format("reading chat history..."));
            lock.readLock().lock();
            List<MessageRequest> allMessagesFromChannel;
            allMessagesFromChannel = getMessagesFromChannel(channelName, ClientHandlersManager.getHistory().getPublicChatHistory());
            Boolean permittedToSeeHistory = validateUser(allMessagesFromChannel, userName);
            if (permittedToSeeHistory) {
                return allMessagesFromChannel;
            } else throw new NoAccessToChatHistoryException("YOU ARE NOT ENTITLED TO VIEW THIS CHANNEL'S HISTORY");
        } finally {
            lock.readLock().unlock();
        }
    }

/*    public List<MessageRequest> readFromCache(String userName, PrivateChannel privateChannel) throws NoAccessToChatHistoryException {
        try {
            logger.log(Level.INFO, String.format("reading chat history..."));
            lock.readLock().lock();
            Boolean isPermitted = validateUser(ClientHandlersManager.getHistory().getPrivateChatHistory(), privateChannel, userName);
            if (isPermitted) {
                return new ArrayList<>(ClientHandlersManager.getHistory().getPrivateChatHistory().get(privateChannel));
            } else throw new NoAccessToChatHistoryException("YOU ARE NOT ENTITLED TO VIEW THIS CHANNEL'S HISTORY");
        } finally {
            lock.readLock().unlock();
        }
    }*/

    @Override
    public <K, V extends List> Map<K, V> readFromFile(File file) {
        Map<K, V> history = null;
        try {
            lock.readLock().lock();
            logger.log(Level.INFO, String.format("Trying to read from file..."));
            Object object = null;
            FileInputStream fIs = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(fIs);
            object =  reader.readObject();
            reader.close();
            history = (HashMap<K, V>)object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.INFO, String.format("Exception in readFromFile - class not found"));
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.log(Level.INFO, String.format("Exception in readFromFile - IO"));
        } finally {
            lock.readLock().unlock();
        }
        return history;
    }

    public Boolean validateUser(List<MessageRequest> messages, String userName){
        Boolean permittedToSeeHistory = false;
        for (MessageRequest message : messages) {
            if (message.getUserName().equals(userName)) {
                permittedToSeeHistory = true;
                logger.log(Level.INFO, String.format("A MATCHING USER HAS BEEN FOUND"));
                return permittedToSeeHistory;
            }
        }
        return permittedToSeeHistory;
    }

/*    public Boolean validateUser(Map<PrivateChannel, List<MessageRequest>>map, PrivateChannel privateChannel, String userName){
        Set<PrivateChannel> allPrivateChannels = map.keySet();
        System.out.println(allPrivateChannels);
        Boolean isPermitted = false;
        for (PrivateChannel channel: allPrivateChannels) {
            System.out.println("Iteruję. Kanał: " + channel.getChannelName());
            if (channel.equals(privateChannel)) {
                logger.log(Level.INFO, "a matching private channel has been found: " + channel);
                isPermitted = channel.getPermittedUsers().contains(userName);
                return isPermitted;
            }
        }
        logger.log(Level.INFO, "no match has been found...");
        return isPermitted;
    }*/

    public List<MessageRequest> getMessagesFromChannel(String key, Map<String, List<MessageRequest>> history) throws NoAccessToChatHistoryException {
        List<MessageRequest> messages;
        if (!history.containsKey(key)) {
            throw new NoAccessToChatHistoryException("NO SUCH CHANNEL IN HISTORY");
        } else {
            messages = history.get(key);
        }
        return messages;
    }

}