package jmotyka.entities;

import jmotyka.entities.Channel;
import jmotyka.entities.ChatHistory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;

@Log
public class ClientHandlersManager {

    @Getter
    @Setter
    private volatile static Map<String, Channel> mapOfAllChannels = new HashMap<>();
    @Getter
    private static ChatHistory history;

    public ClientHandlersManager(ChatHistory history) {
        this.history = history;
    }

    public Boolean checkIfChannelAlreadyExists(String channelName) {
        return mapOfAllChannels.containsKey(channelName);
    }

}
