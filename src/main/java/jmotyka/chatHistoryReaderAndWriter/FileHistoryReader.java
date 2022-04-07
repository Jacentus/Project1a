package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.ClientHandlersManager;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.exceptions.NoSuchChannelException;
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

    @Override
    public List<MessageRequest> read(String username, String channelName) throws NoAccessToChatHistoryException, NoSuchChannelException {
        lock.readLock().lock();
        try {
            if (isPermittedToGetHistory(channelName, username)) {
                logger.log(Level.INFO, String.format("reading chat history..."));
                return ClientHandlersManager.getMapOfAllChannels().get(channelName).getChannelHistory();
            } else {
                throw new NoAccessToChatHistoryException("You are not permitted to see this history");
            }
        } finally{
                lock.readLock().unlock();
            }
        }

    @Override
    public <K, V> Map<K, V> readFromFile(File file) {
        Map<K, V> history = null;
        lock.readLock().lock();
        try {
            logger.log(Level.INFO, String.format("Trying to read from file..."));
            Object object = null;
            FileInputStream fIs = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(fIs);
            object = reader.readObject();
            reader.close();
            history = (HashMap<K, V>) object;
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

    public Boolean isPermittedToGetHistory(String channelName, String userName) throws NoSuchChannelException {
        Boolean permittedToSeeHistory = false;
        if (ClientHandlersManager.getMapOfAllChannels().get(channelName) == null){
        throw new NoSuchChannelException("SUCH CHANNEL DOES NOT EXIST");
        }
        if (ClientHandlersManager.getMapOfAllChannels().get(channelName).getPermittedUsers().contains(userName)) {
            logger.log(Level.INFO, String.format("USER PERMITTED TO GET HISTORY"));
            permittedToSeeHistory = true;
        }
        return permittedToSeeHistory;
    }

}