package jmotyka;

import jmotyka.requests.Request;
import jmotyka.responses.Response;
import jmotyka.serverResponseHandlers.ResponseHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.Socket;

public class Client {

    private final ResponseHandler responseHandler = new ResponseHandler(this);
    @Getter
    private Socket socket;
    @Getter
    private ObjectInputStream inputStreamReader;
    @Getter
    private ObjectOutputStream outputStreamWriter;
    @Getter
    private String username;
    @Getter @Setter
    private String channelName;

    public Client(Socket socket, String username) throws IOException {
        try {
            this.socket = socket;
            this.username = username;
            this.inputStreamReader = new ObjectInputStream(socket.getInputStream());
            this.outputStreamWriter = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, outputStreamWriter, inputStreamReader);
        }
    }

    public void sendRequest(Request request) {
        try {
            while (socket.isConnected()) {
                outputStreamWriter.writeObject(request);
                outputStreamWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, outputStreamWriter, inputStreamReader);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        Response response = (Response) inputStreamReader.readObject();// dostaje objekt
                        responseHandler.handleResponse(response); // przekazuję obiekt do klasy która wie co z tym zrobić oraz ma dostęp do zasobów klienta
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





