package jmotyka;

import jmotyka.requests.GetAllChannelsRequest;
import jmotyka.requests.JoinGroupChatRequest;
import jmotyka.requests.MessageRequest;
import jmotyka.requests.Request;

import java.io.IOException;
import java.net.Socket;

public class AppClientSide {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(Server.getHOST(), Server.getPORT());

        GUI gui = new GUI();
        String username = gui.askForUsername();

        Client client = new Client(socket, username);

        client.listenForMessage();
        gui.setClient(client);
        gui.printMenu();
        gui.chooseFromMenu();

        //Request clientRequest = gui.chooseFromMenu(username);
        //String channelname = gui.askForChannelName();

        //Request request = new GetAllChannelsRequest(username);
        //Request request1 = new JoinGroupChatRequest("Marek", "KANAL"); // nie ma nullpointera gdy kanał istnieje !

        //MessageRequest request2 = new MessageRequest("Marek", "SIEMANKO WSZYSTKIM");
        //request2.setChannelName("KANAL");
        //client.sendRequest(request1);
        //client.sendRequest(request2);
        //client.sendRequest(clientRequest);
    }

}
