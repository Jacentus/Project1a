package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.RemoveFromChannelRequest;
import jmotyka.responses.MessageResponse;

import java.util.ArrayList;
import java.util.logging.Level;

public class RemoveFromChannelRequestHandler extends RequestHandler{

    private RemoveFromChannelRequest request;

    public RemoveFromChannelRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, RemoveFromChannelRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling removing from channel request...!");
        clientHandlers.remove(clientHandler);
        MessageResponse messageResponse = new MessageResponse("SERVER: ", request.getUserName(), "HAS LEFT THE CHANNEL!");
        ArrayList<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(((RemoveFromChannelRequest) request).getChannelName());
        for (ClientHandler client : addressees) {
            broadcast(client, messageResponse);
            logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
        }
        logger.log(Level.INFO, "handling request to remove from channel has finished...!");
    }
}
