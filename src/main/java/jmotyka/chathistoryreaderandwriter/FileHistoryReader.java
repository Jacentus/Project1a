package jmotyka.chathistoryreaderandwriter;

import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.MessageRequest;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHistoryReader implements ChatHistoryReader {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    @Override
    public List<MessageRequest> read(String userName, String channelName) throws NoAccessToChatHistoryException {
        logger.log(Level.INFO, String.format("Inside reader"));
        lock.readLock().lock();
        HashMap<String, List<MessageRequest>> historyFromAllChannels = readFromFile();
        lock.readLock().unlock();
        List<MessageRequest> allMessagesFromChannel;
        allMessagesFromChannel = getMessagesFromChannel(channelName, historyFromAllChannels);
        Boolean permittedToSeeHistory = validateUser(allMessagesFromChannel, userName);
        if (permittedToSeeHistory==true) {
            return allMessagesFromChannel;
        } else throw new NoAccessToChatHistoryException("YOU ARE NOT ENTITLED TO VIEW THIS CHANNEL'S HISTORY");
    }

    public HashMap<String, List<MessageRequest>> readFromFile() {
        HashMap<String, List<MessageRequest>> historyFromAllChannels = null;
        try {
            logger.log(Level.INFO, String.format("Trying to read from file in the while loop...")); // chyba nie trafia tam gdzie powinien output....
            Object object = null;
            System.out.println("Object przed zapisem = " + object);
            File toRead = FileHistorySaver.getFile();
            System.out.println("File: " + toRead);
            FileInputStream fIs = new FileInputStream(toRead);
            ObjectInputStream reader = new ObjectInputStream(fIs);
            object =  reader.readObject();
            reader.close();
            System.out.println("Object po zapisie: " + object);
            historyFromAllChannels = (HashMap<String, List<MessageRequest>>)object;
            System.out.println("Object po konwersji na moją haszmapę: " + historyFromAllChannels);
            System.out.println("KEY SET: " + historyFromAllChannels.keySet());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.INFO, String.format("Exception in readFromFile"));
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.log(Level.INFO, String.format("Exception in readFromFile"));
        }
        return historyFromAllChannels;
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