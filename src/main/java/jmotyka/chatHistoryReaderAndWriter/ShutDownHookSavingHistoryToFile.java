package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.ClientHandlersManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ShutDownHookSavingHistoryToFile implements Runnable{

    ReadWriteLock lock = new ReentrantReadWriteLock();

    ClientHandlersManager clientHandlersManager;

    public ShutDownHookSavingHistoryToFile(ClientHandlersManager clientHandlersManager) {
        this.clientHandlersManager = clientHandlersManager;
    }

    @Override
    public void run() {
        saveToFile(clientHandlersManager.getHistory().getPrivateChatDatabase(), clientHandlersManager.getHistory().getPrivateChatHistory());
        saveToFile(clientHandlersManager.getHistory().getPublicChatDatabase(), clientHandlersManager.getHistory().getPublicChatHistory());
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
