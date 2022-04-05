package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.CreatePrivateChatRequest;
import jmotyka.responses.CreatePrivateChatResponse;
import lombok.Getter;

import java.util.Set;
import java.util.logging.Level;

public class CreatePrivateChatRequestHandler extends RequestHandler {

    @Getter
    private CreatePrivateChatRequest request;

    public CreatePrivateChatRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, CreatePrivateChatRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        System.out.println(request.getPrivateChannel().getChannelName());
        logger.log(Level.INFO, "creating a private chat...!");
        if (checkIfAlreadyExists(request)) {
            broadcast(clientHandler, new CreatePrivateChatResponse(request.getPrivateChannel().getPermittedUsers(), false));
        } else {
            clientHandler.setPrivateChannel(request.getPrivateChannel());
            clientHandlersManager.addClientToPrivateChannel(clientHandler);
            CreatePrivateChatResponse response = new CreatePrivateChatResponse(request.getPrivateChannel().getPermittedUsers(), true);
            broadcast(clientHandler, response);
        }
    }

    public Boolean checkIfAlreadyExists(CreatePrivateChatRequest request) {
        Set<PrivateChannel> allPrivateChannels = ClientHandlersManager.getMapOfAllPrivateChannels().keySet();
        Boolean exists = false;
        for (PrivateChannel channel : allPrivateChannels) {
            if (channel.equals(request.getPrivateChannel())) {
                logger.log(Level.INFO, "a matching private channel has been found");
                exists = true;
            }
        }
        return exists;
    }

}
