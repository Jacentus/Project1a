package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.responses.ErrorResponse;
import jmotyka.responses.GetAllChannelsResponse;

import java.util.ArrayList;
import java.util.List;

public class GetAllChannelsRequestHandler extends RequestHandler {

    public GetAllChannelsRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler) {
        super(clientHandlers, clientHandler);
    }

    @Override
    public void processRequest() {
        List<String> channelNames = new ArrayList<>();
        ClientHandlers.getMapOfAllRooms().forEach((k, v) -> {
            channelNames.add(k);
        });
        GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);
        if (getAllChannelsResponse.getAllChannelsNames() != null) {
            broadcast(clientHandler, getAllChannelsResponse);
        } else {
            String error = "no active channels";
            broadcast(clientHandler, new ErrorResponse(error));
        }
    }

}

