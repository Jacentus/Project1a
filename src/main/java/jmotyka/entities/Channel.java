package jmotyka.entities;

import jmotyka.exceptions.NoAccessToChannelException;
import jmotyka.exceptions.NoAccessToChatHistoryException;
import jmotyka.exceptions.NoSuchChannelException;
import jmotyka.requests.MessageRequest;
import jmotyka.responses.Response;
import lombok.Getter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Channel implements Serializable {

    @Getter
    private final String channelName;
    @Getter
    private final boolean isPrivate;
    @Getter
    private final List<String> permittedUsers;
    @Getter
    private transient List<ClientHandler> usersInChannel;
    @Getter
    private volatile List<MessageRequest> channelHistory;
    @Getter
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private transient Logger logger = Logger.getLogger(getClass().getName());

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

    public void addClientToChannel(ClientHandler clientHandler) throws NoAccessToChannelException {
        lock.writeLock().lock();
        try {
            if (isPermittedToJoin(clientHandler.getClientUsername())) {
                usersInChannel.add(clientHandler);
                if (!isPrivate) {
                    permittedUsers.add(clientHandler.getClientUsername());
                }
                logger.log(Level.INFO, String.format("New user %s entered %s channel", clientHandler.getClientUsername(), channelName));
                logger.log(Level.INFO, String.format("%s active users in %s channel", usersInChannel.size(), channelName));
            } else throw new NoAccessToChannelException("You are not allowed to join this channel!");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Boolean isPermittedToJoin(String userName) {
        if (!this.isPrivate) {
            return true;
        } else {
            for (String user : this.getPermittedUsers()) {
                if (user.equals(userName)) return true;
            }
        }
        return false;
    }

    public void remove(ClientHandler clientHandler) {
        logger.log(Level.INFO, String.format("Removing %s from  %s channel", clientHandler.getClientUsername(), channelName));
        lock.writeLock().lock();
        try {
            usersInChannel.remove(clientHandler);
            logger.log(Level.INFO, String.format("Client %s left %s channel", clientHandler.getClientUsername(), channelName));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void broadcast(ClientHandler clientHandler, Response response) {
        lock.readLock().lock();
        try {
            for (ClientHandler client : usersInChannel) {
                if (!client.getClientUsername().equals(clientHandler.getClientUsername())) {
                    try {
                        client.getObjectOutputStream().writeObject(response);
                        client.getObjectOutputStream().flush();
                        logger.log(Level.INFO, "MESSAGE HAS BEEN SEND TO " + client.getClientUsername());
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        usersInChannel = new ArrayList<>();
        logger = Logger.getLogger(getClass().getName());
    }

    public void save(MessageRequest message) {
        try {
            lock.writeLock().lock();
            this.getChannelHistory().add(message);
            logger.log(Level.INFO, String.format("Message from %s saved to history of %s channel", message.getUserName(), this.getChannelName()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<MessageRequest> readHistory(String username) throws NoAccessToChatHistoryException, NoSuchChannelException {
        lock.readLock().lock();
        try {
            if (isPermittedToGetHistory(username)) {
                logger.log(Level.INFO, "reading chat history...");
                return this.channelHistory;
            } else {
                throw new NoAccessToChatHistoryException("You are not permitted to see this history");
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public Boolean isPermittedToGetHistory(String userName) throws NoSuchChannelException {
        Boolean permittedToSeeHistory = false;
        if (this == null) {
            throw new NoSuchChannelException("SUCH CHANNEL DOES NOT EXIST");
        }
        if (this.getPermittedUsers().contains(userName)) {
            logger.log(Level.INFO, "USER PERMITTED TO GET HISTORY");
            permittedToSeeHistory = true;
        }
        return permittedToSeeHistory;
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