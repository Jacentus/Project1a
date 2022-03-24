package jmotyka;

import jmotyka.requests.GetAllChannelsRequest;
import jmotyka.requests.JoinGroupChatRequest;
import jmotyka.requests.MessageRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

public class GUI {

    @Getter @Setter
    private Client client;

    public String askForUsername(){
        System.out.println("Enter username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        return username;
    }

    public void printMenu(){
        System.out.println("***** CHAT APP *****");
        System.out.println("[1] show channels [2] join existing channel [3] start new public channel");
        System.out.println("[4] start or join private chat [5] download my message history [6] send file ");
    }

    public void chooseFromMenu(){
        boolean stayInMenu = true;
        while (stayInMenu) {
            System.out.print("Your choice: ");
            Scanner scanner = new Scanner(System.in); // TODO: INPUT CONTROL
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("your choice was 1");
                    client.sendRequest(new GetAllChannelsRequest(client.getUsername()));
                case "2":
                    System.out.println("Type channel name: ");
                    String channelName = scanner.nextLine();
                    client.setChannelName(channelName);
                    client.sendRequest(new JoinGroupChatRequest(client.getUsername(), client.getChannelName()));
                    enterChatRoom(client);
                case "3":
                    System.out.println("Type channel name (you will join if already exists): ");// TODO: zapytaj o nazwę, jeśli nie jest zajęta - utwórz nowy kanał
                    String newChannelName = scanner.nextLine();
                    client.setChannelName(newChannelName);
                    client.sendRequest(new JoinGroupChatRequest(client.getUsername(), client.getChannelName()));
                case "4":
                    //return null; //private chat
                    // TODO: ŚCIĄGNIJ HISTORIĘ JEŚLI BYŁEM NA DANYM KANALE
                default:
                    System.out.println("Error");
            }
        }
    }

    public void enterChatRoom(Client client){
        System.out.println("you have entered the chat room");
        while(true){
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            if (text == "#EXIT\n") break;
            MessageRequest message = new MessageRequest(client.getUsername(), client.getChannelName(), text);
            client.sendRequest(message);
        }
    }

}
