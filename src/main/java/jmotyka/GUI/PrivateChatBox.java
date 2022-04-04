package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.PrivateMessageRequest;
import jmotyka.requests.SendFilePrivatelyRequest;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;

public class PrivateChatBox extends ChatBox{

    public PrivateChatBox(Scanner scanner, FileConverter fileConverter, Client client) {
        super(scanner, fileConverter, client);
    }

    public void sendMessage(String text){
        System.out.println("SENDING MESSAGE FROM PRIVATE CHATBOX...");
        MessageRequest message = new PrivateMessageRequest(client.getUsername(), client.getPrivateChannel(), text);
        System.out.println("LISTA USERÓW KANAŁU: " + client.getPrivateChannel().getPermittedUsers());
        client.sendRequest(message);
        logger.log(Level.INFO, "Private message has been send");
    }

    public void sendFile(File file){
        byte[] bytes = fileConverter.transformIntoBytes(file);
        SendFilePrivatelyRequest request = new SendFilePrivatelyRequest(client.getUsername(), file.getName(), bytes, client.getPrivateChannel());
        client.sendRequest(request);
    }

}
