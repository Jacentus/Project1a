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
                ClientHandlersManager.setMapOfAllChannels(new HashMap<>()); // czy aby napewno? Konstruktor w konstruktorze, może nie zadziałać. Czy na pewno cały channel jest serializowany?
            } else { // w momencie gdy zdechnie serwer w bazie mogą zapisać się hanbdlery z zamkniętymi socketami, może to powodować kwas z wysyłaniem do zamklnietych socketów.
                ClientHandlersManager.setMapOfAllChannels(fileHistoryReader.readFromFile(database));
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("EXCEPTION - EMPTY FILE WAS READ");
        }
    }





}
