package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.JoinPrivateChatRequest;
import jmotyka.responses.JoinPrivateChatResponse;
import jmotyka.responses.MessageResponse;
import jmotyka.responses.PrivateMessageResponse;
import jmotyka.responses.PublicMessageResponse;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class JoinPrivateChatRequestHandler extends RequestHandler {

    @Getter
    private JoinPrivateChatRequest request;

    public JoinPrivateChatRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, JoinPrivateChatRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Joining to a private chat...!");
        PrivateChannel isPermitted = checkIfPermittedToJoin(request);
        if (isPermitted == null) {
            broadcast(clientHandler, new JoinPrivateChatResponse(request.getPrivateChannel(), false));
        } else if (isPermitted instanceof PrivateChannel) {
            lock.readLock().lock();
            List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(request.getPrivateChannel());
            lock.readLock().unlock();
            for (ClientHandler client : addressees) {
                broadcast(client, new PublicMessageResponse("SERVER: ", request.getUserName(), "HAS JOINED THE CHANNEL!"));
                logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
            }
            clientHandler.setPrivateChannel(isPermitted);
            clientHandler.getPrivateChannel().setClientPermittedToChat(true);
            clientHandlersManager.addClientToPrivateChannel(clientHandler);
            JoinPrivateChatResponse response = new JoinPrivateChatResponse(clientHandler.getPrivateChannel(), true);
            broadcast(clientHandler, response);
        }
    }

    public PrivateChannel checkIfPermittedToJoin(JoinPrivateChatRequest request) {
        Set<PrivateChannel> allPrivateChannels = ClientHandlersManager.getMapOfAllPrivateChannels().keySet();
        Boolean isPermitted = false;
        for (PrivateChannel channel : allPrivateChannels) {
            if (channel.equals(request.getPrivateChannel())) {
                logger.log(Level.INFO, "a matching private channel has been found");
                if (channel.getPermittedUsers().contains(request.getUserName())) {
                    logger.log(Level.INFO, "a matching user has been found");
                    return channel;
                }
            }
        }
        logger.log(Level.INFO, "no match has been found...");
        return null;
    }

}
