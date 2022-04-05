package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.responses.Response;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RequestHandler {

    protected final ClientHandlersManager clientHandlersManager;
    protected final ClientHandler clientHandler;
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem

    public RequestHandler(ClientHandlersManager clientHandlersManager, ClientHandler clientHandler) {
        this.clientHandlersManager = clientHandlersManager;
        this.clientHandler = clientHandler;
    }

    public abstract void processRequest();

    public void broadcast(ClientHandler clientHandler, Response response) {
        try {
            clientHandler.getObjectOutputStream().writeObject(response);
            clientHandler.getObjectOutputStream().flush();
            logger.log(Level.INFO, "MESSAGE HAS BEEN BROADCASTED");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}