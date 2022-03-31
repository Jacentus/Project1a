package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.chathistoryreaderandwriter.ChatHistoryReader;
import jmotyka.chathistoryreaderandwriter.ChatHistorySaver;
import jmotyka.chathistoryreaderandwriter.FileHistoryReader;
import jmotyka.chathistoryreaderandwriter.FileHistorySaver;
import jmotyka.responses.Response;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RequestHandler {

    protected final ClientHandlers clientHandlers;
    protected final ClientHandler clientHandler;
    protected static ChatHistorySaver chatHistorySaver = new FileHistorySaver();
    protected static ChatHistoryReader chatHistoryReader = new FileHistoryReader();

    protected final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public RequestHandler(ClientHandlers clientHandlers, ClientHandler clientHandler) {
        this.clientHandlers = clientHandlers;
        this.clientHandler = clientHandler;
    }

    public abstract void processRequest();

    public void broadcast(ClientHandler clientHandler, Response response) {
        try {
            logger.log(Level.INFO, "BROADCASTING RESPONSE " + response);
            clientHandler.getObjectOutputStream().writeObject(response);
            clientHandler.getObjectOutputStream().flush();
            logger.log(Level.INFO, "MESSAGE HAS BEEN BROADCASTED");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
