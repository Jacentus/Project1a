package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.Request;
import jmotyka.requests.SendFilePrivatelyRequest;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;

public class PrivateChatBox extends ChatBox{

    public PrivateChatBox(Scanner scanner, FileConverter fileConverter, Client client) {
        super(scanner, fileConverter, client);
    }

    @Override
    public void sendMessage(String text){
        MessageRequest message = new MessageRequest(client.getUsername(), client.getChannelName(), Request.RequestType.MESSAGE, text);
        client.sendRequest(message);
        logger.log(Level.INFO, "Message has been send");
    }

    @Override
    public void sendFile(File file){
        byte[] bytes = fileConverter.transformIntoBytes(file);
        //SendFilePrivatelyRequest request = new SendFilePrivatelyRequest(client.getUsername(), Request.RequestType.SEND_FILE_PRIVATELY, file.getName(), bytes, client.getPrivateChannel());
        //client.sendRequest(request);
        logger.log(Level.INFO, "File send privately");
    }

}
