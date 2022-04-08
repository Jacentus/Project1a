package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.entities.ClientHandlersManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ShutDownHookSavingHistoryToFile implements Runnable {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private ClientHandlersManager manager;
    public ShutDownHookSavingHistoryToFile(ClientHandlersManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        saveToFile(ClientHandlersManager.getDatabase(), manager.getMapOfAllChannels());
    }

    public <T extends Map> void saveToFile(File file, T map) {
        try {
            lock.readLock().lock();
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file));
            writer.writeObject(map);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

}