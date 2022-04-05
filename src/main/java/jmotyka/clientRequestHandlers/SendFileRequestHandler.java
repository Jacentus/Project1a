package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.requests.SendFileRequest;

import java.util.logging.Level;

public abstract class SendFileRequestHandler extends RequestHandler {

    protected SendFileRequest request;

    public SendFileRequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler, SendFileRequest request) {
        super(clientHandlersManager, clientHandler);
        this.request = request;
    }

    @Override
    public void processRequest() {
        logger.log(Level.INFO, "Handling send file request in abstract handler...");
        distributeFile();
    }

    protected abstract void distributeFile();

}