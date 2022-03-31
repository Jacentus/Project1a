package jmotyka.chathistoryreaderandwriter;

import jmotyka.exceptions.NoAccessToChatHistoryException;

import java.util.List;

public interface ChatHistoryReader {

    <E> List<E> read(String userName, String channelName) throws NoAccessToChatHistoryException;

}
