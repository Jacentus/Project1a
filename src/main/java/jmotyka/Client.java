package jmotyka;

import jmotyka.clientRequestHandlers.RequestHandler;
import jmotyka.requests.Request;
import jmotyka.responses.Response;
import jmotyka.serverResponseHandlers.ResponseHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.Socket;

public class Client {

    private ResponseHandler responseHandler = new ResponseHandler(this);

    @Getter
    private Socket socket;
    @Getter
    private BufferedReader bufferedReader;
    @Getter
    private BufferedWriter bufferedWriter;
    @Getter
    private ObjectInputStream inputStreamReader;
    @Getter
    private ObjectOutputStream outputStreamWriter;
    @Getter
    private FileInputStream fileInputStream;
    @Getter
    private FileOutputStream fileOutputStream;
    @Getter
    private String username;

    @Getter @Setter
    private String channelName; // zmienna określająca, na jaki kanał chce nadawać Klient.
    //private ClientsOptionsChoice clientRequest; // początkowy request sterujący tym, co zrobi serwer

    public Client(Socket socket, String username) throws IOException {
        try {
            this.socket = socket;
            this.username = username;
            //this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ///this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.inputStreamReader = new ObjectInputStream(socket.getInputStream());
            this.outputStreamWriter = new ObjectOutputStream(socket.getOutputStream());
            //this.channelName = channelName;
        } catch (IOException e) {
            e.printStackTrace();
            //close(socket, bufferedReader, bufferedWriter);
            close(socket, outputStreamWriter, inputStreamReader);
        }
    }

    public void sendRequest(Request request) {
        try {
            //bufferedWriter.write(username); // TODO: te rzeczy się powtarzają, na pewno mogę to przenieść do jakiejś innej klasy
            //bufferedWriter.newLine();
            //bufferedWriter.flush();
            //bufferedWriter.write(channelName); // TODO: te rzeczy się powtarzają, na pewno mogę to przenieść do jakiejś innej klasy
            //bufferedWriter.newLine();
            //bufferedWriter.flush();
            //bufferedWriter.write(String.valueOf(clientRequest)); // TODO: te rzeczy się powtarzają, na pewno mogę to przenieść do jakiejś innej klasy
           //bufferedWriter.newLine();
            //bufferedWriter.flush();
            //Scanner scanner = new Scanner(System.in);
            //while (socket.isConnected()) {
                //String message = scanner.nextLine();
                //bufferedWriter.write(username + ": " + message);
                outputStreamWriter.writeObject(request);
                outputStreamWriter.flush();
                //System.out.println("infinite loop");
                //bufferedWriter.newLine();
                //bufferedWriter.flush();
            //}
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, outputStreamWriter, inputStreamReader);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //String message;
                while (socket.isConnected()) {
                    try {
                        Response response = (Response) inputStreamReader.readObject();// dostaje objekt
                        responseHandler.handleResponse(response); // przekazuję obiekt do klasy która wie co z tym zrobić oraz ma dostęp do zasobów klienta
                        //message = bufferedReader.readLine();
                        //System.out.println(message);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void close(Socket socket, ObjectOutputStream bufferedReader, ObjectInputStream bufferedWriter){//BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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





