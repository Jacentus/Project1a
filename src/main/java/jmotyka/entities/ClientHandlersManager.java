package jmotyka.entities;

import jmotyka.chatHistoryReaderAndWriter.ChatHistoryReader;
import jmotyka.chatHistoryReaderAndWriter.FileHistoryReader;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class ClientHandlersManager {

    @Getter
    @Setter
    private Map<String, Channel> mapOfAllChannels = new HashMap();
    private final ChatHistoryReader fileHistoryReader = new FileHistoryReader();
    @Getter
    private static final File database = new File("chatHistory.txt");
    private final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem

    public ClientHandlersManager() {
        readHistory();
    }

    public Boolean checkIfChannelAlreadyExists(String channelName) {
        return mapOfAllChannels.containsKey(channelName);
    }

    public void readHistory() {
        logger.log(Level.INFO, "Reading database...");
        try {
            if (database.length() <= 0) {
                this.setMapOfAllChannels(new HashMap<>());
            } else {
                this.setMapOfAllChannels(fileHistoryReader.read(database));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EXCEPTION - EMPTY FILE WAS READ");
        }
    }

}
