package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.RemoveFromPublicChannelRequest;
import jmotyka.responses.MessageResponse;
import jmotyka.responses.PublicMessageResponse;

import java.util.List;
import java.util.logging.Level;

public class RemoveFromPublicChannelRequestHandler extends RequestHandler{

    private RemoveFromPublicChannelRequest request;

    public RemoveFromPublicChannelRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, RemoveFromPublicChannelRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling removing from channel request...!");

        lock.writeLock().lock();
        clientHandlersManager.remove(clientHandler);
        lock.writeLock().unlock();

        MessageResponse messageResponse = new PublicMessageResponse("SERVER: " + request.getUserName() ,request.getChannelName()," HAS LEFT THE CHANNEL!");

        lock.readLock().lock();
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(request.getChannelName());
        lock.readLock().unlock();

        for (ClientHandler client : addressees) {
            broadcast(client, messageResponse);
            logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
        }
        logger.log(Level.INFO, "handling request to remove from channel has finished...!");
    }

}
