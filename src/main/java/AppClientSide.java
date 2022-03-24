import requests.GetAllChannelsRequest;
import requests.JoinGroupChatRequest;
import requests.Request;

import java.io.IOException;
import java.net.Socket;

public class AppClientSide {

    public static void main(String[] args) throws IOException {

        GUI gui = new GUI();
        String username = gui.askForUsername();
        gui.printMenu();
        //Object clientRequest = gui.chooseFromMenu(); // do funkcji przekażę wybór, i tam mogę zwrócić np. rodzaj Klienta...? Może przydzielić ID...
        // reszta powinna zależeć od wyboru...
        //String channelname = gui.askForChannelName();
        Socket socket = new Socket(Server.getHOST(), Server.getPORT());
        Client client = new Client(socket, username); // Client mógłby przyjmować nazwę kanału i być zwracany z GUI

        client.listenForMessage();

        //mógłbym zebrać dane "w paczkę", co chce i kim jest Klient (username, wybór) i zanim wejdziemy do "loopa" z wymianą wiadomości, dostać informacje z serwera!

        Request request = new GetAllChannelsRequest(username);
        Request request1 = new JoinGroupChatRequest("Jacentus", "KANAL");

        client.sendMessage(request1);

    }

}
