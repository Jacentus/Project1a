import lombok.Getter;

import java.util.ArrayList;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClientHandlers {

    @Getter
    private final Map<String, ArrayList<ClientHandler>> clientHandlers = new TreeMap<>();  // zamieniłem na mapę!
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void add(ClientHandler clientHandler) {
        String roomName = clientHandler.getChannelName();
        lock.writeLock().lock();
        if(clientHandlers.get(roomName) == null) {
            ArrayList<ClientHandler> room = new ArrayList<>();
            room.add(clientHandler);
            clientHandlers.put(roomName, room);
            System.out.println("ILOŚĆ LUDZI NA NOWO UTWORZONYM KANALE: " + clientHandler.getChannelName() + " = " + room.size());
        } else {
            clientHandlers.get(roomName).add(clientHandler);
            System.out.println("ILOŚĆ LUDZI NA ISTNIEJĄCYM JUŻ KANALE: " + clientHandler.getChannelName() + " = " + clientHandlers.get(roomName).size());
        }
        lock.writeLock().unlock();
    }

    public void remove(ClientHandler clientHandler) {
        lock.writeLock().lock();
        clientHandlers.get(clientHandler.getChannelName()).remove(clientHandler); // weź pokój -> wyrzuć z pokoju
        lock.writeLock().unlock();
        clientHandler.broadcastMessage(String.format("SERVER: %s has left the %d channel!", clientHandler.getClientUsername()), clientHandler.getChannelName());
    }

/*    public void broadcast(String text) {
        lock.readLock().lock();
        clientHandlers.forEach(clientHandler -> clientHandler.send(text));
        lock.readLock().unlock();
    }*/


}
