package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.MessageResponse;

import java.util.ArrayList;
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
        MessageResponse messageResponse = new MessageResponse(request.getUserName(), request.getChannelName(), request.getText());
        logger.log(Level.INFO, "proceeding to save msg to chat history......!");

        // save to file
        chatHistorySaver.save(request);
        //

        logger.log(Level.INFO, "msg saved to history!");
        ArrayList<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(messageResponse.getChannelName());
        for (ClientHandler client : addressees) {
            broadcast(client, messageResponse);
            logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
        }
    }

}
