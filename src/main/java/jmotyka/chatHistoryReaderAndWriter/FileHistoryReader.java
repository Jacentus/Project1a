package jmotyka.chatHistoryReaderAndWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHistoryReader implements ChatHistoryReader {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public <K, V> Map<K, V> read(File file) {
        Map<K, V> history = null;
        lock.readLock().lock();
        try {
            logger.log(Level.INFO, "Trying to read from file...");
            Object object;
            FileInputStream fIs = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(fIs);
            object = reader.readObject();
            reader.close();
            history = (HashMap<K, V>) object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.INFO, "Exception in readFromFile - class not found");
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.log(Level.INFO, "Exception in readFromFile - IO");
        } finally {
            lock.readLock().unlock();
        }
        return history;
    }

}