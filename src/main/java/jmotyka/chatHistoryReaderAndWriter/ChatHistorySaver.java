package jmotyka.chatHistoryReaderAndWriter;

import jmotyka.requests.MessageRequest;

public interface ChatHistorySaver {

    void save(MessageRequest request, String channelName);

}
