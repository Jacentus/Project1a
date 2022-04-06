package jmotyka.entities;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlersManager;
import jmotyka.exceptions.NoAccesToChannelException;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.Response;
import lombok.Getter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Channel implements Serializable {

    @Getter
    private String channelName;
    @Getter
    private boolean isPrivate;
    @Getter
    private List<String> permittedUsers; // TODO: jeżeli czat jest prywatny, to dostęp do historii będą mieli tylko ci userzy. jeśli jest publiczny, to każdy user który dołączy będzie tu dodawany.
    @Getter
    private List<ClientHandler> usersInChannel;
    @Getter
    private List<MessageRequest> channelHistory;   // TODO : czy na pewno?
    @Getter
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Channel(String channelName) {
        this.channelName = channelName;
        this.isPrivate = false;
        this.permittedUsers = new ArrayList<>();
        this.usersInChannel = new ArrayList<>();
        this.channelHistory = new ArrayList<>();
    }

    public Channel(String channelName, boolean isPrivate, List<String> permittedUsers) {
        this.channelName = channelName;
        this.isPrivate = isPrivate;
        this.permittedUsers = permittedUsers;
        this.usersInChannel = new ArrayList<>();
        this.channelHistory = new ArrayList<>();
    }

    public void addClientToChannel(ClientHandler clientHandler) throws NoAccesToChannelException {
        lock.writeLock().lock();
        try {
            if (isPermittedToJoin(clientHandler.getClientUsername())) {
                usersInChannel.add(clientHandler);
                if (!isPrivate) {
                    permittedUsers.add(clientHandler.getClientUsername());
                }
                logger.log(Level.INFO, String.format("New user %s entered %s channel", clientHandler.getClientUsername(), channelName));
                logger.log(Level.INFO, String.format("%s active users in %s channel", usersInChannel.size(), channelName));
            } else throw new NoAccesToChannelException("You are not allowed to join this channel!");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Boolean isPermittedToJoin(String userName) {
        if (this.isPrivate) {
            for (String user : this.getPermittedUsers()) {
                if (user.equals(userName)) return true;
            }
        } else if (!this.isPrivate) return true;
        return false;
    }

    public void remove(ClientHandler clientHandler) {
        logger.log(Level.INFO, String.format("Removing %s from  %s channel", clientHandler.getClientUsername(), channelName));
        lock.writeLock().lock();
        try {
            usersInChannel.remove(clientHandler.getClientUsername());
            logger.log(Level.INFO, String.format("Client %s left %s channel", clientHandler.getClientUsername(), channelName));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void broadcast(ClientHandler clientHandler, Response response) { // TODO: nie jestem pewien czy nadaję do dobrych socketów
        for (ClientHandler client : usersInChannel) {
            if (!client.getClientUsername().equals(clientHandler.getClientUsername())) {
                try {
                    client.getObjectOutputStream().writeObject(response);
                    client.getObjectOutputStream().flush();
                    logger.log(Level.INFO, "MESSAGE HAS BEEN BROADCASTED");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Channel)) return false;
        Channel channel = (Channel) o;
        return channelName.equals(channel.channelName);
    }

    @Override
    public int hashCode() {
        return channelName.hashCode();
    }

}
