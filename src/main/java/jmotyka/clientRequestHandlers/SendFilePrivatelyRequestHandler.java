package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.SendFilePrivatelyRequest;
import jmotyka.requests.SendFilePubliclyRequest;
import jmotyka.requests.SendFileRequest;
import jmotyka.responses.SendFileResponse;

import java.util.List;
import java.util.logging.Level;

public class SendFilePrivatelyRequestHandler extends SendFileRequestHandler{

    public SendFilePrivatelyRequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlers, clientHandler, request);
    }

    @Override
    protected void distributeFile() {
        System.out.println("handling request in SendPrivately...");
        SendFilePrivatelyRequest sendFileRequest = (SendFilePrivatelyRequest) request;
        List<ClientHandler> addressees = ClientHandlers.getMapOfPrivateRooms().get(sendFileRequest.getChannel());
        SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getFileName(), sendFileRequest.getByteFile()); // TODO: zaimpleementowa przekazanie nazwy pliku
        for (ClientHandler client : addressees) {
            broadcast(client, sendFileResponse);
            logger.log(Level.INFO, "File sent to " + client.getClientUsername());
        }
    }

}
