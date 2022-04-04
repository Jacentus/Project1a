package jmotyka.chathistoryreaderandwriter;

import jmotyka.ClientHandlers;
import jmotyka.entities.PrivateChannel;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.JoinPrivateChatRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.PrivateMessageRequest;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHistoryReader implements ChatHistoryReader {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public List<MessageRequest> readFromCache(String userName, String channelName) throws NoAccessToChatHistoryException {
        try {
            logger.log(Level.INFO, String.format("Inside chat reader"));
            lock.readLock().lock();
            List<MessageRequest> allMessagesFromChannel;
            allMessagesFromChannel = getMessagesFromChannel(channelName, ClientHandlers.getHistory().getPublicChatHistory());
            Boolean permittedToSeeHistory = validateUser(allMessagesFromChannel, userName);
            if (permittedToSeeHistory == true) {
                return allMessagesFromChannel;
            } else throw new NoAccessToChatHistoryException("YOU ARE NOT ENTITLED TO VIEW THIS CHANNEL'S HISTORY");
        } finally {
            lock.readLock().unlock();
        }
    }


    public List<MessageRequest> readFromCache(String userName, PrivateChannel privateChannel) throws NoAccessToChatHistoryException {
        try {
            logger.log(Level.INFO, String.format("Inside chat reader"));
            lock.readLock().lock();
            Boolean isPermitted = validateUser(ClientHandlers.getHistory().getPrivateChatHistory(), privateChannel, userName);
            if (isPermitted) {
                return new ArrayList<>(ClientHandlers.getHistory().getPrivateChatHistory().get(privateChannel));
            } else throw new NoAccessToChatHistoryException("YOU ARE NOT ENTITLED TO VIEW THIS CHANNEL'S HISTORY");
        } finally {
            lock.readLock().unlock();
        }
    }

    public <K, V extends List> Map<K, V> readFromFile(File file) {
        Map<K, V> history = null;
        try {
            lock.readLock().lock();
            logger.log(Level.INFO, String.format("Trying to read from file..."));
            Object object = null;
            System.out.println("Object przed zapisem = " + object);
            System.out.println("File: " + file);
            FileInputStream fIs = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(fIs);
            object =  reader.readObject();
            reader.close();
            System.out.println("Object po zapisie: " + object);
            history = (HashMap<K, V>)object;
            System.out.println("Object po konwersji na moją haszmapę: " + history);
            System.out.println("KEY SET: " + history.keySet());
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
            System.out.println("Szukany: " + userName + " Znaleziony: " + message.getUserName());
            if (message.getUserName().equals(userName)) {
                permittedToSeeHistory = true;
                logger.log(Level.INFO, String.format("A MATCHED USER HAS BEEN FOUND"));
                return permittedToSeeHistory;
            }
        }
        return permittedToSeeHistory;
    }

    public Boolean validateUser(Map<PrivateChannel, List<MessageRequest>>map, PrivateChannel privateChannel, String userName){
        Set<PrivateChannel> allPrivateChannels = map.keySet();
        Boolean isPermitted = false;
        for (PrivateChannel channel: allPrivateChannels) {
            System.out.println("iteruję po kanałach. Kanał: " + channel);
            if (channel.equals(privateChannel)) {
                logger.log(Level.INFO, "a matching private channel has been found: " + channel);
                System.out.println("isPermitted = " + isPermitted);
                System.out.println("Userzy: " + channel.getPermittedUsers());
                isPermitted = channel.getPermittedUsers().contains(userName);
                System.out.println("isPermitted po contains " + isPermitted);
                return isPermitted;
                /*if (channel.getPermittedUsers().contains(userName)) {
                    logger.log(Level.INFO, "a matching user has been found");
                    isPermitted = true;
                }*/
            }
        }
        //logger.log(Level.INFO, "no match has been found...");
        return isPermitted;
    }

    public List<MessageRequest> getMessagesFromChannel(String key, Map<String, List<MessageRequest>> history) throws NoAccessToChatHistoryException {
        List<MessageRequest> messages;
        if (!history.containsKey(key)) {
            logger.log(Level.INFO, String.format("NO SUCH KEY"));
            throw new NoAccessToChatHistoryException("NO SUCH CHANNEL IN HISTORY");
        } else {
            messages = history.get(key);
            logger.log(Level.INFO, String.format("OBTAINED A LIST OF ALL MESSAGES, PRINTING IT:"));
            System.out.println(messages);
        }
        return messages;
    }

}