package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.GetChatHistoryRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetChatHistoryResponse;

import java.util.List;
import java.util.logging.Level;

public class GetChatHistoryRequestHandler extends RequestHandler {

    private GetChatHistoryRequest request;

    public GetChatHistoryRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, GetChatHistoryRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Trying to get channel history...");
        GetChatHistoryRequest getHistoryRequest = (GetChatHistoryRequest)request;
        try {
            logger.log(Level.INFO, "in try block");
            List<MessageRequest> channelHistory;
            channelHistory = chatHistoryReader.read(request.getUserName(), getHistoryRequest.getChannelName());
            logger.log(Level.INFO, "came back to request handler to try block");
            GetChatHistoryResponse response = new GetChatHistoryResponse(channelHistory);
            broadcast(clientHandler, response);
        } catch (NoAccessToChatHistoryException exception) {
            logger.log(Level.INFO,"to mój message z errora: " + exception.getMessage());
            String error = exception.getMessage();
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }
}
