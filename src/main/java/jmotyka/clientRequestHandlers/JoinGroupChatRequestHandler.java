package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.JoinGroupChatRequest;
import jmotyka.responses.JoinPublicChatResponse;

import java.util.logging.Level;

public class JoinGroupChatRequestHandler extends RequestHandler{

    private JoinGroupChatRequest request;

    public JoinGroupChatRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, JoinGroupChatRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        System.out.println(request.getChannelName());
        logger.log(Level.INFO, "Joining to a group chat...!");
        clientHandler.setChannelName(request.getChannelName());
        clientHandlers.addClientToOpenChannel(clientHandler);
        JoinPublicChatResponse joinPublicChatResponse = new JoinPublicChatResponse(request.getChannelName());
        broadcast(clientHandler, joinPublicChatResponse);
    }

}
