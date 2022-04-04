package jmotyka.entities;

import jmotyka.chathistoryreaderandwriter.FileHistoryReader;
import jmotyka.chathistoryreaderandwriter.FileHistorySaver;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.MessageRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.EOFException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatHistory {

    @Getter
    private final File publicChatDatabase = new File("publicChatHistory.txt");
    @Getter
    private final File privateChatDatabase = new File("privateChatHistory.txt");
    @Getter
    private final FileHistorySaver fileHistorySaver = new FileHistorySaver();
    @Getter
    private final FileHistoryReader fileHistoryReader = new FileHistoryReader();
    @Getter
    private Map<String, List<MessageRequest>> publicChatHistory;
    @Getter
    private Map<PrivateChannel, List<MessageRequest>> privateChatHistory = new HashMap<>();

    public ChatHistory() {
        try {
            if(publicChatDatabase.length() <= 0){
                this.publicChatHistory = new HashMap<>();
            } else {
                this.publicChatHistory = fileHistoryReader.readFromFile(publicChatDatabase);
            }
            if (privateChatDatabase.length() <= 0){
                this.privateChatHistory = new HashMap<>();
            } else {
                this.privateChatHistory = fileHistoryReader.readFromFile(privateChatDatabase);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("EXCEPTION EMPTY FILE WAS READ");
        }
    }

}
