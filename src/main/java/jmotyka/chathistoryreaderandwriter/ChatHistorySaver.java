package jmotyka.chathistoryreaderandwriter;

import jmotyka.requests.MessageRequest;

public interface ChatHistorySaver {

    void save(MessageRequest request);

}
