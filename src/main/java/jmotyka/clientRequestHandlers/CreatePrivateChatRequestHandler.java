package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.CreatePrivateChatRequest;
import jmotyka.responses.CreatePrivateChatResponse;
import lombok.Getter;

import java.util.logging.Level;

public class CreatePrivateChatRequestHandler extends RequestHandler {

    @Getter
    private CreatePrivateChatRequest request;

    public CreatePrivateChatRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, CreatePrivateChatRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        System.out.println(request.getPrivateChannel().getChannelName());
        logger.log(Level.INFO, "creating a private chat...!");
        //
        clientHandler.setPrivateChannel(request.getPrivateChannel());
        //
        clientHandlers.addClientToPrivateChannel(clientHandler);
        CreatePrivateChatResponse response = new CreatePrivateChatResponse(request.getPrivateChannel().getPermittedUsers());
        broadcast(clientHandler, response);
    }

}
