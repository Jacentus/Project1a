package jmotyka;

import jmotyka.requests.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class GUI {

    private final Logger logger = Logger.getLogger(getClass().getName()); //TODO: transfer to interface


    @Getter
    @Setter
    private Client client;

    public String askForUsername() {
        System.out.println("Enter username: "); //TODO: input control + check if user exists
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        return username;
    }

    public void chooseFromMenu() throws InterruptedException {
        while (true) {
            printMenu();
            String choice = null;
            System.out.print("Your choice: ");
            Scanner scanner = new Scanner(System.in); // TODO: INPUT CONTROL
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    client.sendRequest(new GetAllChannelsRequest(client.getUsername()));
                    TimeUnit.SECONDS.sleep(3);
                    break;
                case "2":
                    System.out.println("Type channel name: ");
                    String channelName = scanner.nextLine();
                    client.setChannelName(channelName);
                    client.sendRequest(new JoinGroupChatRequest(client.getUsername(), client.getChannelName()));
                    chatBox(scanner);
                    client.sendRequest(new RemoveFromChannelRequest(client.getUsername(), client.getChannelName()));
                    break;
                case "3":
                    System.out.println("Type channel name (you will join if already exists): ");// TODO: zapytaj o nazwę, jeśli nie jest zajęta - utwórz nowy kanał
                    String newChannelName = scanner.nextLine();
                    client.setChannelName(newChannelName);
                    client.sendRequest(new JoinGroupChatRequest(client.getUsername(), client.getChannelName()));
                    break;
                case "4":
                    // TODO: PRIVATE CHANNE:
                    break;
                case "5":
                    System.out.println("Type channel name you wish get history from: ");
                    String historicChannel = scanner.nextLine();
                    System.out.println(historicChannel + " TO JEST MOJ HISTORICCHANNEL");
                    client.sendRequest(new GetChatHistoryRequest(client.getUsername(), historicChannel));
                    break;
                default:
                    System.out.println("error from choiceMenu");
            }
            choice = null;
        }
    }

    public void chatBox(Scanner scanner) {
        System.out.println("** START CHATTING **");
        System.out.println("** TYPE #EXIT TO QUIT, #FILE TO SEND A FILE **");
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
                System.out.println("FILENAME: " + file.getName()); // dodać nazwę pliku do requesta tak, aby nie nadpisywał każdorazowo pliku już istniejącego
                SendFileRequest sendFileRequest = transformIntoBytes(client.getUsername(), client.getChannelName(), file);
                client.sendRequest(sendFileRequest);
                logger.log(Level.INFO, "File request send");
                text = null;
            }
            else {
                MessageRequest message = new MessageRequest(client.getUsername(), client.getChannelName(), text);
                client.sendRequest(message);
                logger.log(Level.INFO, "Message send");
            }
        }
    }

    public SendFileRequest transformIntoBytes(String userName, String channelName, File file) {
        byte[] byteFile = new byte[0];
        logger.log(Level.INFO, "Inside transform method form GUI...");
        try {
            byteFile = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            logger.log(Level.INFO, "EXCEPTION READING FILE !!!");
            System.out.println("exception when transforming file into bytes");
            e.printStackTrace();
        }
        SendFileRequest sendFileRequest = new SendFileRequest(userName, channelName, byteFile);
        return sendFileRequest;
    }

    public void printMenu() {
        System.out.println("***** CHAT APP *****");
        System.out.println("[1] show open channels [2] join/create open channel [3] start new public channel");
        System.out.println("[4] start or join private chat [5] download my message history [6] send file ");
    }

}
