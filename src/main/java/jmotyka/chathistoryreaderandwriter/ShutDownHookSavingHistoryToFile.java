package jmotyka.chathistoryreaderandwriter;

import jmotyka.ClientHandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

public class ShutDownHookSavingHistoryToFile implements Runnable{

    ReadWriteLock lock = new ReentrantReadWriteLock();

    ClientHandlers clientHandlers;

    public ShutDownHookSavingHistoryToFile(ClientHandlers clientHandlers) {
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        saveToFile(clientHandlers.getHistory().getPrivateChatDatabase(), clientHandlers.getHistory().getPrivateChatHistory());
        saveToFile(clientHandlers.getHistory().getPublicChatDatabase(), clientHandlers.getHistory().getPublicChatHistory());
    }

    public <T extends Map> void saveToFile(File file, T map) {
        try {
            lock.writeLock().lock();
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file));
            writer.writeObject(map);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
