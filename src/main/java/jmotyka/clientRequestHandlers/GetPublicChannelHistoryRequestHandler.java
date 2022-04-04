package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.chathistoryreaderandwriter.FileHistoryReader;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.GetChannelHistoryRequest;
import jmotyka.requests.GetPublicChannelHistoryRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetChatHistoryResponse;
import lombok.Getter;

import java.util.List;
import java.util.logging.Level;

public class GetPublicChannelHistoryRequestHandler extends GetChatHistoryRequestHandler{

    @Getter
    private GetPublicChannelHistoryRequest request;

    public GetPublicChannelHistoryRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, GetPublicChannelHistoryRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Trying to get channel history...");
        List<MessageRequest> channelHistory = null;
        FileHistoryReader reader = new FileHistoryReader();
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
