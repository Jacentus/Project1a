package jmotyka.clientRequestHandlers;

import jmotyka.requests.MessageRequest;

public interface ChatHistorySaver {

    public void save(MessageRequest request);

}
