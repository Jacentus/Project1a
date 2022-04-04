package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.chathistoryreaderandwriter.FileHistoryReader;
import jmotyka.entities.PrivateChannel;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.GetChannelHistoryRequest;
import jmotyka.requests.GetPrivateChannelHistoryRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetChatHistoryResponse;

import java.util.List;
import java.util.logging.Level;

public class GetPrivateChannelHistoryRequestHandler extends GetChatHistoryRequestHandler {

    private GetPrivateChannelHistoryRequest request;

    public GetPrivateChannelHistoryRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, GetPrivateChannelHistoryRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Trying to get private channel history...");
        FileHistoryReader reader = new FileHistoryReader();
        List<MessageRequest> channelHistory = null;
        try{
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

