import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    private String username;
    private String channelName; // zmienna określająca, na jaki kanał chce nadawać Klient.
    private ClientsOptionsChoice clientRequest; // początkowy request sterujący tym, co zrobi serwer

    public Client(Socket socket, String username, String channelName, ClientsOptionsChoice clientRequest) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.channelName = channelName;
            this.clientRequest = clientRequest;
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username); // TODO: te rzeczy się powtarzają, na pewno mogę to przenieść do jakiejś innej klasy
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.write(channelName); // TODO: te rzeczy się powtarzają, na pewno mogę to przenieść do jakiejś innej klasy
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.write(String.valueOf(clientRequest)); // TODO: te rzeczy się powtarzają, na pewno mogę to przenieść do jakiejś innej klasy
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                if(message == "#FILE"){
                    System.out.println("WYKRYŁEM KOMENDĘ NA TRANSFER PLIKÓW");
                }
                bufferedWriter.write(username + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();
                        if(message == "#FILE"){
                            System.out.println("WYKRYŁEM KOMENDĘ NA TRANSFER PLIKÓW");
                        }
                        System.out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





