package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.chathistoryreaderandwriter.FileHistorySaver;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.PrivateMessageRequest;
import jmotyka.requests.PublicMessageRequest;
import jmotyka.responses.MessageResponse;
import jmotyka.responses.PrivateMessageResponse;
import jmotyka.responses.PublicMessageResponse;

import java.util.List;
import java.util.logging.Level;

public class MessageRequestHandler extends RequestHandler{

    private MessageRequest request;

    public MessageRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, MessageRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling message request...!");
        if (request instanceof PublicMessageRequest) {
            PublicMessageRequest publicMessageRequest = (PublicMessageRequest) request;
            MessageResponse messageResponse = new PublicMessageResponse(publicMessageRequest.getUserName(), publicMessageRequest.getChannel(), publicMessageRequest.getText());
            logger.log(Level.INFO, "proceeding to save public msg to chat history......!");
             // do poprawki na interfejs
            FileHistorySaver.saveToCache(publicMessageRequest);

            logger.log(Level.INFO, "msg saved to history!");
            List<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(clientHandler.getChannelName());
            for (ClientHandler client : addressees) {
                if(request.getUserName() != client.getClientUsername()) {
                    broadcast(client, messageResponse);
                    logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
                }
            }
        }
        else if (request instanceof PrivateMessageRequest){
            PrivateMessageRequest privateMessageRequest = (PrivateMessageRequest) request;
            MessageResponse messageResponse = new PrivateMessageResponse(privateMessageRequest.getUserName(), privateMessageRequest.getChannel(), privateMessageRequest.getText());
            logger.log(Level.INFO, "saving private message to chat history");

           FileHistorySaver.saveToCache(privateMessageRequest);

            logger.log(Level.INFO, "msg saved to history!");
            List<ClientHandler> addressees = ClientHandlers.getMapOfPrivateRooms().get(clientHandler.getPrivateChannel());
            for (ClientHandler client : addressees) {
                if(request.getUserName() != client.getClientUsername()) { // próba uniknięcia dublowania się wiadomości
                    broadcast(client, messageResponse);
                    logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
                }
            }
        }
    }



}
