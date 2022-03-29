package jmotyka.clientRequestHandlers;

import java.io.File;
import java.util.List;

public interface ChatHistoryReader {

    public <E> List<E> read(String userName, String channelName) throws NoAccessToChatHistoryException;

}
