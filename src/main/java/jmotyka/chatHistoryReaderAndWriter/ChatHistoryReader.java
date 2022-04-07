package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.exceptions.NoSuchChannelException;
import jmotyka.requests.MessageRequest;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ChatHistoryReader { //TODO: do poprawy

    <K, V> Map<K, V> readFromFile(File file);

    List<MessageRequest> read(String username, String channelName) throws NoAccessToChatHistoryException, NoSuchChannelException;

}
