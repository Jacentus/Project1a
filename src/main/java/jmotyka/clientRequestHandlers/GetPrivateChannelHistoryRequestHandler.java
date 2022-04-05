package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.chatHistoryReaderAndWriter.FileHistoryReader;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.GetPrivateChannelHistoryRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetChatHistoryResponse;

import java.util.List;
import java.util.logging.Level;

public class GetPrivateChannelHistoryRequestHandler extends GetChatHistoryRequestHandler {

    private GetPrivateChannelHistoryRequest request;

    public GetPrivateChannelHistoryRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, GetPrivateChannelHistoryRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Trying to get private channel history...");
        FileHistoryReader reader = new FileHistoryReader();
        List<MessageRequest> channelHistory = null;
        try {
            channelHistory = reader.readFromCache(request.getUserName(), request.getChannel());
            GetChatHistoryResponse response = new GetChatHistoryResponse(channelHistory);
            broadcast(clientHandler, response);
        } catch (NoAccessToChatHistoryException e) {
            e.printStackTrace();
            String error = e.getMessage();
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }

}

