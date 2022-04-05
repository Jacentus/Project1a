package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.RemoveFromPrivateChannelRequest;
import jmotyka.responses.MessageResponse;
import jmotyka.responses.PublicMessageResponse;

import java.util.List;
import java.util.logging.Level;

public class RemoveFromPrivateChannelRequestHandler extends RequestHandler{

    private RemoveFromPrivateChannelRequest request;

    public RemoveFromPrivateChannelRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, RemoveFromPrivateChannelRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling removing from private channel request...!");
        clientHandlersManager.remove(clientHandler, request.getChannel());
        MessageResponse messageResponse = new PublicMessageResponse("SERVER: " + request.getUserName() ,request.getChannel().getChannelName()," HAS LEFT THE CHANNEL!");
        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(request.getChannel());
        lock.readLock().unlock();
        for (ClientHandler client : addressees) {
            broadcast(client, messageResponse);
            logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
        }
        logger.log(Level.INFO, "handling request to remove from channel has finished...!");
    }

}
