package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.*;
import lombok.extern.java.Log;

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
        if (request instanceof GetPrivateChannelHistoryRequest){
            return new GetPrivateChannelHistoryRequestHandler(clientHandlers, clientHandler, (GetPrivateChannelHistoryRequest) request);
        }
        if (request instanceof GetPublicChannelHistoryRequest){
            return new GetPublicChannelHistoryRequestHandler(clientHandlers, clientHandler, (GetPublicChannelHistoryRequest) request);
        }
        if (request instanceof SendFilePrivatelyRequest){
            return new SendFilePrivatelyRequestHandler(clientHandlers, clientHandler, (SendFileRequest) request);
        }
        if (request instanceof SendFilePubliclyRequest){
            return new SendFilePubliclyRequestHandler(clientHandlers, clientHandler, (SendFileRequest) request);
        }
        if (request instanceof CreatePrivateChatRequest) {
            return new CreatePrivateChatRequestHandler(clientHandlers, clientHandler, (CreatePrivateChatRequest) request);
        }
        if (request instanceof JoinPrivateChatRequest) {
            return new JoinPrivateChatRequestHandler(clientHandlers, clientHandler, (JoinPrivateChatRequest) request);
        }
        else return null;
    }

}


