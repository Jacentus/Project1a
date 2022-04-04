package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Log
public class GUI {

    private final Logger logger = Logger.getLogger(getClass().getName()); //TODO: transfer to interface
    private final FileConverter fileConverter = new FileConverter();

    @Getter
    @Setter
    private Client client;

    public void printMenu() {
        System.out.println("***** CHAT APP *****");
        System.out.println("[1] show open channels [2] join/create public channel");
        System.out.println("[3] create private channel [4] join private channel [5] download message history");
    }

    public String askForUsername() {
        System.out.println("Enter username: "); //TODO: input control + check if user exists
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        return username;
    }

    public void chooseFromMenu() throws InterruptedException {
        while (true) {
            printMenu();
            String choice;
            System.out.print("Your choice: ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    client.sendRequest(new GetAllChannelsRequest(client.getUsername()));
                    TimeUnit.SECONDS.sleep(1);
                    break;
                case "2":
                    System.out.println("Type channel name: ");
                    String channelName = scanner.nextLine();
                    client.setChannelName(channelName);
                    client.sendRequest(new JoinGroupChatRequest(client.getUsername(), client.getChannelName()));
                    ChatBox publicChatBox = new PublicChatBox(scanner, fileConverter, client);
                    publicChatBox.launchChatBox();
                    client.sendRequest(new RemoveFromChannelRequest(client.getUsername(), client.getChannelName()));
                    client.setChannelName(null);
                    break;
                case "3": // create private channel
                    System.out.println("Type channel name: ");
                    String newPrivateChannelName = scanner.nextLine();
                    PrivateChannel privateChannel = new PrivateChannel(newPrivateChannelName);
                    System.out.println("Provide list of users you want to chat with, one by one.");
                    System.out.println("Type #DONE when finished: ");

                    String permittedUser = null;
                    privateChannel.getPermittedUsers().add(client.getUsername());
                    while (true) {
                        permittedUser = scanner.nextLine();
                        if (permittedUser.equalsIgnoreCase("#DONE")) {
                            System.out.println("BREAK!");
                            break;
                        }
                        privateChannel.getPermittedUsers().add(permittedUser);
                    }
                    client.setPrivateChannel(privateChannel);
                    client.sendRequest(new CreatePrivateChatRequest(client.getUsername(), client.getPrivateChannel()));
                    ChatBox chatBox = new PrivateChatBox(scanner, fileConverter, client);
                    chatBox.launchChatBox();
                    // TODO: remove from channel, unset client private channel
                    client.setPrivateChannel(null);
                    break;
                case "4":
                    System.out.println("Type channel name you want to join: ");
                    String privateChannelName = scanner.nextLine();
                    //System.out.println("A TO CLIENT W GUI: " + client + "i jego czy permitted przed requestem: " + client.getPrivateChannel().getClientPermittedToChat());
                    // to nie działa muszę dać chyba CountDownLatch

                    client.setPrivateChannel(new PrivateChannel(privateChannelName)); // ze względu na equals powinno dać się znaleźć po nazwie
                    System.out.println("Client w gui: " + client);
                    client.getLock().getServerResponseLock().lock();
                    try {
                        client.sendRequest(new JoinPrivateChatRequest(client.getUsername(), client.getPrivateChannel()));
                        System.out.println("thread should be waiting...");
                        client.getLock().getResponseHandled().await();
                    } finally {
                        client.getLock().getServerResponseLock().unlock();
                    }

                    //Thread.currentThread().join();

                    System.out.println("przed ifem w GUI odpalającym chatbox. Permitted: " + client.getPrivateChannel().getClientPermittedToChat());
                    //client.getPrivateChannel().setClientPermittedToChat(true); // to tylko na potrzeby testów !!! Konieczna synchronizacja wątków !!!
                    // to powoduje, że każdy mimo braku dostępu będzie miał odpalony chatbox
                    // jest problem z threadem, najpierw wykonuje się to, nie czekając na odpowiedź z serwera
                    //Thread.currentThread().wait(1000);

                    if (client.getPrivateChannel().getClientPermittedToChat() == true) {
                        ChatBox chatBox1 = new PrivateChatBox(scanner, fileConverter, client);
                        chatBox1.launchChatBox();
                        //chatBox(scanner); //TODO: MAKE CHATBOX WORK FOR BOTH KIND OF MESSAGES
                        // TODO: remove from channel
                    }
                    client.setPrivateChannel(null);
                    break;
                case "5":
                    System.out.println("Private or public channel? [1] PRIVATE [2] PUBLIC");
                    String historyChoice = scanner.nextLine();
                    switch (historyChoice) {
                        case "1":
                            System.out.println("Type channel name you wish get history from: ");
                            String historicPrivateChannelName = scanner.nextLine();
                            PrivateChannel historicChannel = new PrivateChannel(historicPrivateChannelName);
                            client.sendRequest(new GetPrivateChannelHistoryRequest(client.getUsername(), historicChannel));
                            break;
                        case "2":
                            System.out.println("Type channel name you wish get history from: ");
                            String historicPublicChannelName = scanner.nextLine();
                            client.sendRequest(new GetPublicChannelHistoryRequest(client.getUsername(), historicPublicChannelName));
                            break;
                        default:
                            System.out.println("wrong choice!");
                            break;
                    }
                    TimeUnit.SECONDS.sleep(1);
                    break;
                default:
                    System.out.println("No such command in menu! Try again");
            }
            choice = null;
        }
    }

    /*public void chatBox(Scanner scanner) {
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
                // to przeniesc do osobnej metody? Klasy?
                File file = new File(path);
                System.out.println("FILENAME: " + file.getName()); // TODO: dodać nazwę pliku do requesta tak, aby nie nadpisywał każdorazowo pliku już istniejącego
                SendFileRequest sendFileRequest = fileSender.transformIntoBytes(client.getUsername(), client.getChannelName(), file);
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
    }*/

}
