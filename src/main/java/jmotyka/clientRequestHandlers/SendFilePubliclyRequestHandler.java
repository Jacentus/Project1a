package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.SendFilePubliclyRequest;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.SendFileResponse;

import java.util.List;
import java.util.logging.Level;

public class SendFilePubliclyRequestHandler extends SendFileRequestHandler {

    public SendFilePubliclyRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlersManager, clientHandler, request);
    }

    @Override
    protected void distributeFile() {
        SendFilePubliclyRequest sendFileRequest = (SendFilePubliclyRequest) request;
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPublicChannels().get(sendFileRequest.getChannel());
        SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getFileName(), sendFileRequest.getByteFile());
        for (ClientHandler client : addressees) {
            if (request.getUserName() != client.getClientUsername()) {
                broadcast(client, sendFileResponse);
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
    }

}