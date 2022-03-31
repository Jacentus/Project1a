package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.*;
import jmotyka.responses.*;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class RequestHandlersFactory {

    private final ClientHandlers clientHandlers;
    private final ClientHandler clientHandler;

    public RequestHandlersFactory(ClientHandler clientHandler, ClientHandlers clientHandlers) {
        this.clientHandlers = clientHandlers;
        this.clientHandler = clientHandler;
    }

    public RequestHandler getRequestHandler(Request request){
        if (clientHandler.getClientUsername() == null) {
            clientHandler.setClientUsername(request.getUserName());
        }
        if (request instanceof GetAllChannelsRequest) {
            return new GetAllChannelsRequestHandler(clientHandlers, clientHandler);
        }
        if (request instanceof JoinGroupChatRequest){
            return new JoinGroupChatRequestHandler(clientHandlers, clientHandler, (JoinGroupChatRequest) request);
        }
        if (request instanceof MessageRequest){
            return new MessageRequestHandler(clientHandlers, clientHandler, (MessageRequest) request);
        }
        if (request instanceof RemoveFromChannelRequest){
            return new RemoveFromChannelRequestHandler(clientHandlers, clientHandler, (RemoveFromChannelRequest) request);
        }
        if (request instanceof GetChatHistoryRequest){
            return new GetChatHistoryRequestHandler(clientHandlers, clientHandler, (GetChatHistoryRequest) request);
        }
        if (request instanceof SendFileRequest){
            return new SendFileRequestHandler(clientHandlers, clientHandler, (SendFileRequest) request);
        }
        else return null;
    }

}


