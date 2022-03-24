package jmotyka;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClientHandlers implements Serializable {

    @Getter
    private final Map<String, ArrayList<ClientHandler>> mapOfAllRooms = new TreeMap<>();  // zamieniłem na mapę!
    private final ReadWriteLock lock = new ReentrantReadWriteLock();


    public ClientHandlers() {
        mapOfAllRooms.put("warszawa", new ArrayList<>());
        mapOfAllRooms.put("gdansk", new ArrayList<>());
        mapOfAllRooms.put("ogolny", new ArrayList<>());
    }

    public void add(ClientHandler clientHandler) {
        String roomName = clientHandler.getChannelName();
        System.out.println("TO NAZWA POKOJU: " + roomName + " a to nazwa usera: " + clientHandler.getClientUsername());
        lock.writeLock().lock();
        if(mapOfAllRooms.isEmpty()) {
            ArrayList<ClientHandler> room = new ArrayList<>();
            room.add(clientHandler);
            mapOfAllRooms.put(roomName, room);
            System.out.println("ILOŚĆ LUDZI NA NOWO UTWORZONYM KANALE: " + clientHandler.getChannelName() + " = " + room.size());
        } else {
            mapOfAllRooms.get(roomName).add(clientHandler);
            System.out.println("ILOŚĆ LUDZI NA ISTNIEJĄCYM JUŻ KANALE: " + clientHandler.getChannelName() + " = " + mapOfAllRooms.get(roomName).size());
        }
        lock.writeLock().unlock();
    }

    public void remove(ClientHandler clientHandler) {
        lock.writeLock().lock();
        mapOfAllRooms.get(clientHandler.getChannelName()).remove(clientHandler); // weź pokój -> wyrzuć z pokoju
        lock.writeLock().unlock();
        //clientHandler.broadcastMessage(String.format("SERVER: %s has left the %d channel!", clientHandler.getClientUsername()), clientHandler.getChannelName());
    }

/*    public void broadcast(String text) { // to metoda z przykładu z Sagesa
        lock.readLock().lock();
        clientHandlers.forEach(clientHandler -> clientHandler.send(text));
        lock.readLock().unlock();
    }*/


}
