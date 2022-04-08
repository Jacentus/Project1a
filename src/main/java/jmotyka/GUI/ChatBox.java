package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.Request;
import jmotyka.requests.SendFileRequest;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBox {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    protected Scanner scanner;
    protected FileConverter fileConverter;
    protected Client client;
    private String channelName;

    public ChatBox(Scanner scanner, FileConverter fileConverter, Client client, String channelName) {
        this.scanner = scanner;
        this.fileConverter = fileConverter;
        this.client = client;
        this.channelName = channelName;
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
                text = null;
            } else {
                sendMessage(text);
            }
        }
    }

    private void sendMessage(String text) {
        MessageRequest message = new MessageRequest(client.getUsername(), channelName, Request.RequestType.MESSAGE, text);
        client.sendRequest(message);
        logger.log(Level.INFO, "Message send");
    }

    private void sendFile(File file) {
        byte[] bytes = fileConverter.transformIntoBytes(file);
        SendFileRequest request = new SendFileRequest(client.getUsername(), channelName, Request.RequestType.SEND_FILE_REQUEST, file.getName(), bytes);
        client.sendRequest(request);
        logger.log(Level.INFO, "File send");
    }

}