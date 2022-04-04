package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.SendFileRequest;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ChatBox {

    protected final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem
    protected Scanner scanner;
    protected FileConverter fileConverter;
    protected Client client;

    protected String publicChannel;
    protected PrivateChannel privateChannel;

    public ChatBox(Scanner scanner, FileConverter fileConverter, Client client/*, String publicChannel*/) {
        this.scanner = scanner;
        this.fileConverter = fileConverter;
        this.client = client;
        //this.publicChannel = publicChannel;
    }

/*    public ChatBox(Scanner scanner, FileSender fileSender, Client client, PrivateChannel privateChannel) {
        this.scanner = scanner;
        this.fileSender = fileSender;
        this.client = client;
        this.privateChannel = privateChannel;
    }*/

    public void launchChatBox() {
        System.out.println("** START CHATTING **");
        System.out.println("** TYPE #EXIT TO QUIT, #FILE TO SEND A FILE **");
        String text = null;
        while (true) {
            text = scanner.nextLine();
            if (text.equalsIgnoreCase("#EXIT")) {
                System.out.println("Exiting chatroom...");
                break;
            }
            if (text.equalsIgnoreCase("#FILE")) {
                System.out.println("TYPE PATH TO FILE: (eg. D:\\file.txt)");
                String path = scanner.nextLine();
                // to przeniesc do osobnej metody? Klasy?
                File file = new File(path);
                System.out.println("FILENAME: " + file.getName()); // TODO: dodać nazwę pliku do requesta tak, aby nie nadpisywał każdorazowo pliku już istniejącego
                sendFile(file);   //public SendFilePrivatelyRequest(String userName, String fileName, byte[] byteFile, PrivateChannel channel) {
                // TO MOGĘ WYJĄĆ DO OSOBNYCH FUNKCJI I JE PRZECIĄŻYĆ
                //SendFileRequest sendFileRequest = fileConverter.transformIntoBytes(client.getUsername(), client.getChannelName(), file);
                //client.sendRequest(sendFileRequest);
                /////////////////////////////////////////////////
                logger.log(Level.INFO, "File request send (abstract class)");
                text = null;
            }
            else {
                sendMessage(text);
                // TO MOGĘ WYJĄĆ DO OSOBNYCH FUNKCJI I JE PRZECIĄŻYĆ
                //MessageRequest message = new MessageRequest(client.getUsername(), client.getChannelName(), text);
                //client.sendRequest(message);
                /////////////////////////////////////////////////
                //logger.log(Level.INFO, "Message send");
            }
        }
    }

    abstract void sendMessage(String text);

    abstract void sendFile(File file);

}
