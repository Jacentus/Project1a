package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.SendFileResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class SendFileRequestHandler extends RequestHandler {

    protected SendFileRequest request;

    public SendFileRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling send file request in abstract handler...");
        distributeFile();
        /*SendFileRequest sendFileRequest = request;
        List<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(sendFileRequest.getChannelName());
        SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getChannelName(), sendFileRequest.getByteFile());
        for (ClientHandler client : addressees) {
            broadcast(client, sendFileResponse);
            logger.log(Level.INFO, "File sent to " + client.getClientUsername());
        }*/
    }

    protected abstract void distributeFile();

}
