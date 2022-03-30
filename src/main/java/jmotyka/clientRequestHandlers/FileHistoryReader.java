package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.Request;
import jmotyka.responses.MessageResponse;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHistoryReader implements ChatHistoryReader {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    //private ObjectInputStream reader ; // MUSZE MIEC JEDEN READER I WRITER DO PLIKU, INACZEJ WYSKAKUJE MI CORRUPTED STREAM EXCEPTION. ZAIMPLEMENTOWAC

    public FileHistoryReader() { // dodałem do konstruktowa aby w razie czego móc wyjąć jego inputreadera
/*        try {
            this.reader = new ObjectInputStream(new FileInputStream(FileHistorySaver.getFile())); /// tu chyba coś nie tak? Czy jeden input ?
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.INFO, String.format("Exception when creating a fileHistoryReader"));
        }*/
    }

    @Override
    public List<MessageRequest> read(String userName, String channelName) throws NoAccessToChatHistoryException {
        logger.log(Level.INFO, String.format("Inside reader"));
        HashMap<String, List<MessageRequest>> historyFromAllChannels = null;
        List<MessageRequest> allMessagesFromChannel;
        lock.readLock().lock();

        try {
            logger.log(Level.INFO, String.format("Trying to read from file in the while loop...")); // chyba nie trafia tam gdzie powinien output....
            //

            //
            Object object = null;
            System.out.println("Object przed zapisem = " + object);
            File toRead = FileHistorySaver.getFile();
            System.out.println("File: " + toRead);
            FileInputStream fIs = new FileInputStream(toRead);
            ObjectInputStream reader = new ObjectInputStream(fIs);
            //while (reader.available() > 0) {
                object =  reader.readObject(); // to nie czyta !!!!!
                reader.close();
            //}

            System.out.println("Object po zapisie: " + object);
            historyFromAllChannels = (HashMap<String, List<MessageRequest>>)object;

            System.out.println("Object po konwersji na moją haszmapę: " + historyFromAllChannels);
            System.out.println("KEY SET: " + historyFromAllChannels.keySet());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.INFO, String.format("Exception when reading history from file"));
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.log(Level.INFO, String.format("Check if end of file has been reached"));
        }
        if (!historyFromAllChannels.containsKey(channelName)) {
                logger.log(Level.INFO, String.format("NO SUCH KEY AS"));
                //return null;
                throw new NoAccessToChatHistoryException("NO SUCH CHANNEL IN HISTORY"); //TODO: add new error class and handle such situation
        } else {
                allMessagesFromChannel = historyFromAllChannels.get(channelName);
                logger.log(Level.INFO, String.format("OBTAINED A LIST OF ALL MESSAGES, PRINTING IT:"));
                System.out.println(allMessagesFromChannel);
            }
        Boolean permittedToSeeHistory = false;
        for (MessageRequest message : allMessagesFromChannel) {
            System.out.println("Szukany: " + userName + " Znaleziony: " + message.getUserName());
            if (message.getUserName().equals(userName)) {
                permittedToSeeHistory = true;
                System.out.println("zmieniam wartość PERMITTED na true...");
                logger.log(Level.INFO, String.format("A MATCHED USER HAS BEEN FOUND"));
                }
            }
        if (permittedToSeeHistory==true) {
            return allMessagesFromChannel;
        } else throw new NoAccessToChatHistoryException("YOU ARE NOT ENTITLED TO VIEW THIS CHANNEL'S HISTORY");
    }
}