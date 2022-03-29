package jmotyka;

import jmotyka.requests.GetAllChannelsRequest;
import jmotyka.requests.JoinGroupChatRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.Request;

import java.io.IOException;
import java.net.Socket;

public class AppClientSide {

    public static void main(String[] args) throws IOException, InterruptedException {

        Socket socket = new Socket(Server.getHOST(), Server.getPORT());
        GUI gui = new GUI();

        String username = gui.askForUsername();
        Client client = new Client(socket, username);

        client.listenForMessage();
        gui.setClient(client);

        gui.chooseFromMenu();

    }
}
