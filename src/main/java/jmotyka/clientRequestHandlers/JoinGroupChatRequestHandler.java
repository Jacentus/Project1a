package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.JoinGroupChatRequest;
import jmotyka.responses.JoinPublicChatResponse;
import jmotyka.responses.PublicMessageResponse;

import java.util.List;
import java.util.logging.Level;

public class JoinGroupChatRequestHandler extends RequestHandler{

    private JoinGroupChatRequest request;

    public JoinGroupChatRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, JoinGroupChatRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        System.out.println(request.getChannelName());
        logger.log(Level.INFO, "Joining to a group chat...!");
        clientHandler.setChannelName(request.getChannelName());
        clientHandlersManager.addClientToPublicChannel(clientHandler);
        JoinPublicChatResponse joinPublicChatResponse = new JoinPublicChatResponse(request.getChannelName());
        broadcast(clientHandler, joinPublicChatResponse);
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(clientHandler.getChannelName());
        for (ClientHandler client : addressees) {
            if(request.getUserName() != client.getClientUsername()) {
                broadcast(client, new PublicMessageResponse("SERVER: ", request.getUserName(), "HAS ENTERED THE CHANNEL!"));
                logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
            }
        }
    }

}
