import java.util.Scanner;

public class GUI {

    public String askForUsername(){
        System.out.println("Enter username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        return username;
    }

    public String askForChannelName(){
        System.out.println("Channel name: ");
        Scanner scanner = new Scanner(System.in);
        String channelName = scanner.nextLine();
        return channelName;
    }

    public void printMenu(){
        System.out.println("***** CHAT APP *****");
        System.out.println("[1] join existing channel [2] start new public channel [3] start or join private chat [4] download my message history");
        System.out.println("If you wish to send a file when connected, type #FILE, click ENTER and specify the path of the file you wish to send.");
    }

    public Object chooseFromMenu(){
        System.out.println("Your choice: ");
        Scanner scanner = new Scanner(System.in);
        Integer choice = scanner.nextInt();
        switch (choice){
            case 1: // TODO: INPUT CONTROL
                // TODO: weź listę wszystkich kanałów, wyświetl, daj wpisać nazwę, zapisz do tego kanału
                return null;
            case 2:
                // TODO: zapytaj o nazwę, jeśli nie jest zajęta - utwórz nowy kanał
                return null;
            case 3:
                // TODO: zapytaj o nazwę, jeśli takiej nie ma - stwórz kanał, jeśli jest - dołącz
                return null;
            case 4:
                return null; //TODO: ŚCIĄGNIJ HISTORIĘ JEŚLI BYŁEM NA DANYM KANALE
            default:
                System.out.println("Error");
        }
        return null;
    }



}
