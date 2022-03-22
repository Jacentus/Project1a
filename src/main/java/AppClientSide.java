import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AppClientSide {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        Socket socket = new Socket(Server.getHOST(), Server.getPORT());
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }

}
