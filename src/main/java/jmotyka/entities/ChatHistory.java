package jmotyka.entities;

import jmotyka.ClientHandlersManager;
import jmotyka.chatHistoryReaderAndWriter.ChatHistoryReader;
import jmotyka.chatHistoryReaderAndWriter.FileHistoryReader;

import lombok.Getter;

import java.io.File;
import java.util.HashMap;

public class ChatHistory {

    @Getter
    private final File database = new File("chatHistory.txt");
    @Getter
    private final ChatHistoryReader fileHistoryReader = new FileHistoryReader();

    public ChatHistory() {
        try {
            if(database.length() <= 0){
                ClientHandlersManager.setMapOfAllChannels(new HashMap<>());
            } else {
                ClientHandlersManager.setMapOfAllChannels(fileHistoryReader.readFromFile(database));
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("EXCEPTION - EMPTY FILE WAS READ");
        }
    }





}
