package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.PublicMessageRequest;
import jmotyka.requests.SendFilePubliclyRequest;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;

public class PublicChatBox extends ChatBox {

    public PublicChatBox(Scanner scanner, FileConverter fileConverter, Client client) {
        super(scanner, fileConverter, client);
    }

    @Override
    void sendMessage(String text) {
        MessageRequest message = new PublicMessageRequest(client.getUsername(), client.getChannelName(), text);
        client.sendRequest(message);
        logger.log(Level.INFO, "Message send");
    }

    @Override
    public void sendFile(File file) {
        byte[] bytes = fileConverter.transformIntoBytes(file);
        SendFilePubliclyRequest request = new SendFilePubliclyRequest(client.getUsername(), file.getName(), bytes, client.getChannelName());
        client.sendRequest(request);
        logger.log(Level.INFO, "File send");
    }

}
