package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.SendFilePrivatelyRequest;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.SendFileResponse;

import java.util.List;
import java.util.logging.Level;

public class SendFilePrivatelyRequestHandler extends SendFileRequestHandler {

    public SendFilePrivatelyRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlersManager, clientHandler, request);
    }

    @Override
    protected void distributeFile() {
        SendFilePrivatelyRequest sendFileRequest = (SendFilePrivatelyRequest) request;
        List<ClientHandler> addressees = ClientHandlersManager.getMapOfAllPrivateChannels().get(sendFileRequest.getChannel());
        SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getFileName(), sendFileRequest.getByteFile());
        for (ClientHandler client : addressees) {
            if (request.getUserName() != client.getClientUsername()) {
                broadcast(client, sendFileResponse);
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
    }

}