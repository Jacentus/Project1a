package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.*;
import lombok.extern.java.Log;

@Log
public class RequestHandlersFactory {

    private final ClientHandlersManager clientHandlersManager;
    private final ClientHandler clientHandler;

    public RequestHandlersFactory(ClientHandler clientHandler, ClientHandlersManager clientHandlersManager) {
        this.clientHandlersManager = clientHandlersManager;
        this.clientHandler = clientHandler;
    }

    public RequestHandler getRequestHandler(Request request){
        if (clientHandler.getClientUsername() == null) {
            clientHandler.setClientUsername(request.getUserName());
        }
        if (request instanceof GetAllChannelsRequest) {
            return new GetAllChannelsRequestHandler(clientHandlersManager, clientHandler);
        }
        if (request instanceof JoinGroupChatRequest){
            return new JoinGroupChatRequestHandler(clientHandlersManager, clientHandler, (JoinGroupChatRequest) request);
        }
        if (request instanceof MessageRequest){
            return new MessageRequestHandler(clientHandlersManager, clientHandler, (MessageRequest) request);
        }
        if (request instanceof RemoveFromPublicChannelRequest){
            return new RemoveFromPublicChannelRequestHandler(clientHandlersManager, clientHandler, (RemoveFromPublicChannelRequest) request);
        }
        if (request instanceof RemoveFromPrivateChannelRequest) {
            return new RemoveFromPrivateChannelRequestHandler(clientHandlersManager, clientHandler, (RemoveFromPrivateChannelRequest) request);
        }
        if (request instanceof GetPrivateChannelHistoryRequest){
            return new GetPrivateChannelHistoryRequestHandler(clientHandlersManager, clientHandler, (GetPrivateChannelHistoryRequest) request);
        }
        if (request instanceof GetPublicChannelHistoryRequest){
            return new GetPublicChannelHistoryRequestHandler(clientHandlersManager, clientHandler, (GetPublicChannelHistoryRequest) request);
        }
        if (request instanceof SendFilePrivatelyRequest){
            return new SendFilePrivatelyRequestHandler(clientHandlersManager, clientHandler, (SendFileRequest) request);
        }
        if (request instanceof SendFilePubliclyRequest){
            return new SendFilePubliclyRequestHandler(clientHandlersManager, clientHandler, (SendFileRequest) request);
        }
        if (request instanceof CreatePrivateChatRequest) {
            return new CreatePrivateChatRequestHandler(clientHandlersManager, clientHandler, (CreatePrivateChatRequest) request);
        }
        if (request instanceof JoinPrivateChatRequest) {
            return new JoinPrivateChatRequestHandler(clientHandlersManager, clientHandler, (JoinPrivateChatRequest) request);
        }
        else return null;
    }

}


