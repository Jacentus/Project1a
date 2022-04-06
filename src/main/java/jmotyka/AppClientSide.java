package jmotyka;

import jmotyka.GUI.GUI;
import jmotyka.requests.IntroductionRequest;
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
        client.sendRequest(new IntroductionRequest(username, Request.RequestType.INTRODUCTION));
        gui.setClient(client);
        gui.chooseFromMenu();
    }

}
