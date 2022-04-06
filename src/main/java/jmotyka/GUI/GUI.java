package jmotyka.GUI;

import jmotyka.Client;
import jmotyka.entities.PrivateChannel;
import jmotyka.requests.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

public class GUI {

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
        System.out.println("Enter username: "); //TODO: input control + check if user already exists
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
                    client.getLock().getServerResponseLock().lock();
                    try {
                        client.sendRequest(new GetAllChannelsRequest(client.getUsername(), Request.RequestType.GET_ALL_CHANNELS));
                        client.getLock().getResponseHandled().await();
                    } finally {
                        client.getLock().getServerResponseLock().unlock();
                    }
                    break;
                case "2":
                    System.out.println("Type channel name: ");
                    String channelName = scanner.nextLine();
                    client.setChannelName(channelName);
                    client.getLock().getServerResponseLock().lock();
                    try {
                        client.sendRequest(new JoinPublicChannelRequest(client.getUsername(), Request.RequestType.JOIN_PUBLIC_CHANNEL, client.getChannelName()));
                        client.getLock().getResponseHandled().await();
                    } finally {
                        client.getLock().getServerResponseLock().unlock();
                    }
                    ChatBox publicChatBox = new PublicChatBox(scanner, fileConverter, client);
                    publicChatBox.launchChatBox();
                    client.sendRequest(new RemoveFromPublicChannelRequest(client.getUsername(), Request.RequestType.REMOVE_FROM_PUBLIC_CHANNEL, client.getChannelName()));
                    client.setChannelName(null);
                    break;
                case "3":
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
                            break;
                        }
                        privateChannel.getPermittedUsers().add(permittedUser);
                    }
                    client.setPrivateChannel(privateChannel);
                    client.getLock().getServerResponseLock().lock();
                    try {
                        client.sendRequest(new CreatePrivateChannelRequest(client.getUsername(), Request.RequestType.CREATE_NEW_PRIVATE_CHANNEL, client.getPrivateChannel()));
                        client.getLock().getResponseHandled().await();
                    } finally {
                        client.getLock().getServerResponseLock().unlock();
                    }
                    System.out.println("Permitted: " + client.getPrivateChannel().getClientPermittedToChat());
                    if (client.getPrivateChannel().getClientPermittedToChat()) {
                        ChatBox creatorChatBox = new PrivateChatBox(scanner, fileConverter, client);
                        creatorChatBox.launchChatBox();
                        client.sendRequest(new RemoveFromPrivateChannelRequest(client.getUsername(), Request.RequestType.REMOVE_FROM_PRIVATE_CHANNEL, client.getPrivateChannel()));
                    }
                    client.setPrivateChannel(null);
                    break;
                case "4":
                    System.out.println("Type channel name you want to join: ");
                    String privateChannelName = scanner.nextLine();
                    client.setPrivateChannel(new PrivateChannel(privateChannelName));
                    client.getLock().getServerResponseLock().lock();
                    try {
                        client.sendRequest(new JoinPrivateChannelRequest(client.getUsername(), Request.RequestType.JOIN_PRIVATE_CHANNEL, client.getPrivateChannel()));
                        client.getLock().getResponseHandled().await();
                    } finally {
                        client.getLock().getServerResponseLock().unlock();
                    }
                    System.out.println("Permitted: " + client.getPrivateChannel().getClientPermittedToChat());
                    if (client.getPrivateChannel().getClientPermittedToChat()) {
                        ChatBox joinerChatBox = new PrivateChatBox(scanner, fileConverter, client);
                        joinerChatBox.launchChatBox();
                        client.sendRequest(new RemoveFromPrivateChannelRequest(client.getUsername(), Request.RequestType.REMOVE_FROM_PRIVATE_CHANNEL, client.getPrivateChannel()));
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
                            client.getLock().getServerResponseLock().lock();
                            try {
                                client.sendRequest(new GetPrivateChannelHistoryRequest(client.getUsername(), Request.RequestType.GET_PRIVATE_CHANNEL_HISTORY, historicChannel));
                                client.getLock().getResponseHandled().await();
                            } finally {
                                client.getLock().getServerResponseLock().unlock();
                            }
                            break;
                        case "2":
                            System.out.println("Type channel name you wish get history from: ");
                            String historicPublicChannelName = scanner.nextLine();
                            client.getLock().getServerResponseLock().lock();
                            try {
                                client.sendRequest(new GetPublicChannelHistoryRequest(client.getUsername(), Request.RequestType.GET_PUBLIC_CHANNEL_HISTORY, historicPublicChannelName));
                                client.getLock().getResponseHandled().await();
                            } finally {
                                client.getLock().getServerResponseLock().unlock();
                            }
                            break;
                        default:
                            System.out.println("Invalid choice!");
                            break;
                    }
                    break;
                default:
                    System.out.println("No such command! Try again");
            }
            choice = null;
        }
    }

}
