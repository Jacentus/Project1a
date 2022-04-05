package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.entities.PrivateChannel;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ChatBox {

    protected final Logger logger = Logger.getLogger(getClass().getName()); // TODO: ukryć pod interfejsem
    protected Scanner scanner;
    protected FileConverter fileConverter;
    protected Client client;
    protected PrivateChannel privateChannel;

    public ChatBox(Scanner scanner, FileConverter fileConverter, Client client) {
        this.scanner = scanner;
        this.fileConverter = fileConverter;
        this.client = client;
    }

    public void launchChatBox() {
        System.out.println("** START CHATTING **");
        System.out.println("** TYPE #EXIT TO QUIT, TYPE #FILE TO SEND A FILE **");
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
                File file = new File(path);
                sendFile(file);
                logger.log(Level.INFO, "File request send");
                text = null;
            }
            else {
                sendMessage(text);
            }
        }
    }

    abstract void sendMessage(String text);

    abstract void sendFile(File file);

}
