package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.SendFilePubliclyRequest;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.SendFileResponse;

import java.util.List;
import java.util.logging.Level;

public class SendFilePubliclyRequestHandler extends SendFileRequestHandler{

    public SendFilePubliclyRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlers, clientHandler, request);
    }

    @Override
    protected void distributeFile() {
        System.out.println("handling request in SendPublicly...");
        SendFilePubliclyRequest sendFileRequest = (SendFilePubliclyRequest) request;
        List<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(sendFileRequest.getChannel());
        SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getFileName(), sendFileRequest.getByteFile()); //TODO: zaimplementować w gui przekazanie nazwy pliku
        for (ClientHandler client : addressees) {
            broadcast(client, sendFileResponse);
            logger.log(Level.INFO, "File sent to " + client.getClientUsername());
        }
    }

}
