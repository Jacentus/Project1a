package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;

public abstract class GetChatHistoryRequestHandler extends RequestHandler {

    public GetChatHistoryRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler) {
        super(clientHandlersManager, clientHandler);
    }

}
