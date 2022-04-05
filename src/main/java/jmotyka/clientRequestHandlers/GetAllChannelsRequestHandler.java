package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetAllChannelsResponse;

import java.util.ArrayList;
import java.util.List;

public class GetAllChannelsRequestHandler extends RequestHandler {

    public GetAllChannelsRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler) {
        super(clientHandlersManager, clientHandler);
    }

    @Override
    public void processRequest() {
        List<String> channelNames = new ArrayList<>();
        lock.readLock().lock();
        ClientHandlersManager.getMapOfAllPublicChannels().forEach((k, v) -> channelNames.add(k));
        lock.readLock().unlock();
        GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);
        if (getAllChannelsResponse.getAllChannelsNames() != null) {
            broadcast(clientHandler, getAllChannelsResponse);
        } else {
            String error = "no active channels";
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }

}

