package jmotyka.chathistoryreaderandwriter;

import jmotyka.requests.MessageRequest;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class FileHistorySaver implements ChatHistorySaver {

    @Getter
    private static final Map<String, List<MessageRequest>> chatHistory = new HashMap<>();
    @Getter
    private static final File file  = new File("history.txt");

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    @Override
    public void save(MessageRequest message){
        try {
            logger.log(Level.INFO, String.format("Saving message"));
            String channelName = message.getChannelName();

            lock.writeLock().lock();

            // TODO: read objects from file to map

            if (chatHistory.isEmpty() || !chatHistory.containsKey(channelName)) {
                ArrayList<MessageRequest> roomHistory = new ArrayList<>();
                roomHistory.add(message);
                chatHistory.put(channelName, roomHistory);
                logger.log(Level.INFO, String.format("History for a new room '%s' has been created", channelName));
            } else {
                chatHistory.get(channelName).add(message);
                logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), message.getChannelName()));
            }
            ObjectOutputStream writer = new ObjectOutputStream( new FileOutputStream(file));
            writer.writeObject(chatHistory);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
            logger.log(Level.INFO, String.format("Error when writing to file..."));
        }
        lock.writeLock().unlock();
    }

}
