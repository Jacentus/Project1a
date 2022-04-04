package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.chathistoryreaderandwriter.FileHistoryReader;
import jmotyka.entities.PrivateChannel;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.requests.GetChannelHistoryRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetChatHistoryResponse;

import java.util.List;
import java.util.logging.Level;

public abstract class GetChatHistoryRequestHandler extends RequestHandler {

    private GetChannelHistoryRequest request;

    public GetChatHistoryRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler) {
        super(clientHandlers, clientHandler);
    }

   /* @Override
    public void processRequest() {
        logger.log(Level.INFO, "Trying to get channel history...");
        List<MessageRequest> channelHistory = null;
        try { // TO NIE MOŻE TAK BYĆ BO MAM LOCK W LOCKU I KOD JEST POMIESZANY. SPRAWDZMY CZY DZIAŁA
            FileHistoryReader reader = new FileHistoryReader();

            Boolean isPermittedToGetPrivateChannelHistory = reader.validateUser(clientHandlers.getHistory().getPrivateChatHistory(),new PrivateChannel(request.getChannelName()), request.getUserName());
            Boolean isPermittedToGetPublicChannelHistory = reader.validateUser(clientHandlers.getHistory().getPublicChatHistory().get(request.getChannelName()), request.getUserName());
            if (isPermittedToGetPrivateChannelHistory){
                System.out.println("GETTING PRIVATE MESSAGES");
                channelHistory = reader.readFromCache(request.getUserName(), new PrivateChannel(request.getChannelName()));
            }
            if (isPermittedToGetPublicChannelHistory){
                System.out.println("GETTING PUBLIC MESSAGES");
                channelHistory = reader.readFromCache(request.getUserName(), request.getChannelName());
            }
            GetChatHistoryResponse response = new GetChatHistoryResponse(channelHistory);
            broadcast(clientHandler, response);
            } catch (NoAccessToChatHistoryException exception) {
            exception.printStackTrace();
            logger.log(Level.INFO,"to mój message z errora: " + exception.getMessage());
            String error = exception.getMessage();
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }*/

/*    private Boolean checkIfPermittedToGetPrivateChannelHistory(String channelName, String username){
        Boolean isPermitted = false;
        Set<PrivateChannel> set = clientHandlers.getHistory().getPrivateChatHistory().keySet();
        for (PrivateChannel channel: set) {
            if (channel.equals(new PrivateChannel(channelName))) {
                logger.log(Level.INFO, "a matching private channel has been found");
                if (channel.getPermittedUsers().contains(username)) {
                    logger.log(Level.INFO, "a matching user has been found");
                    isPermitted = true;
                }
            }
        }
        return isPermitted;
    }*/

}
