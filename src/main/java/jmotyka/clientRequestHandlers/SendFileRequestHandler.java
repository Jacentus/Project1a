package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.SendFileResponse;

import java.util.ArrayList;
import java.util.logging.Level;

public class SendFileRequestHandler extends RequestHandler {

    private SendFileRequest request;

    public SendFileRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlers, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling send file request...");
        SendFileRequest sendFileRequest = request;
        ArrayList<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(sendFileRequest.getChannelName());
        SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getChannelName(), sendFileRequest.getByteFile());
        for (ClientHandler client : addressees) {
            broadcast(client, sendFileResponse);
            logger.log(Level.INFO, "File sent to " + client.getClientUsername());
        }
    }

}
