package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.JoinPrivateChatRequest;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.JoinPrivateChatResponse;
import jmotyka.responses.JoinPublicChatResponse;
import lombok.Getter;

import java.util.Set;
import java.util.logging.Level;

public class JoinPrivateChatRequestHandler extends RequestHandler{

    @Getter
    private JoinPrivateChatRequest request;

    public JoinPrivateChatRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, JoinPrivateChatRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        System.out.println("Trying to join private channel: " + request.getPrivateChannel().getChannelName());
        logger.log(Level.INFO, "Joining to a private chat...!");
        PrivateChannel isPermitted = checkIfPermittedToJoin(request);
        System.out.println("STATUS isPermitted: " + isPermitted);
        if (isPermitted==null) {
            broadcast(clientHandler, new ErrorResponse("YOU ARE NOT PERMITTED TO JOIN THIS CHANNEL"));
        }
        else if (isPermitted instanceof PrivateChannel) { // poprawiłem to, powinno zadziałać?
            clientHandler.setPrivateChannel(isPermitted);
            clientHandler.getPrivateChannel().setClientPermittedToChat(true); //a to mi po co w końcu?
            clientHandlers.addClientToPrivateChannel(clientHandler);
            JoinPrivateChatResponse response = new JoinPrivateChatResponse(clientHandler.getPrivateChannel(), true);
            broadcast(clientHandler, response);
        }
    }

    public PrivateChannel checkIfPermittedToJoin(JoinPrivateChatRequest request){

        Set<PrivateChannel> allPrivateChannels = ClientHandlers.getMapOfPrivateRooms().keySet();
        // CZY ZADZIAŁA ZALEŻY OD TEOG, CZY MÓJ EQUALS DZIAŁA OK ! POWINIEN PORÓWNYWAĆ TYLKO NAZWY
        Boolean isPermitted = false;
        for (PrivateChannel channel: allPrivateChannels) {
            if (channel.equals(request.getPrivateChannel())) {
                logger.log(Level.INFO, "a matching private channel has been found");
                if (channel.getPermittedUsers().contains(request.getUserName())) {
                    logger.log(Level.INFO, "a matching user has been found");
                    return channel;
                    //isPermitted = true;
                }
            }
        }
        logger.log(Level.INFO, "no match has been found...");
        return null; // zamieniam boolean na private channel aby odzyskać go z historii
        }

}
