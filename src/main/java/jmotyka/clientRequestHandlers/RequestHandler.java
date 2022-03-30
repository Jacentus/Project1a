package jmotyka.clientRequestHandlers;

import jmotyka.ClientHandler;
import jmotyka.ClientHandlers;
import jmotyka.requests.*;
import jmotyka.responses.*;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class RequestHandler { // to działa na requestach, jakie wpadły na serwer, mam więc dostęp do wszystkich zasobów serwera

    @Getter
    private final ClientHandlers clientHandlers;
    @Getter
    private final ClientHandler clientHandler;
    @Getter
    private static ChatHistorySaver chatHistorySaver  = new FileHistorySaver();

    private static ChatHistoryReader chatHistoryReader = new FileHistoryReader();

    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public RequestHandler(ClientHandler clientHandler, ClientHandlers clientHandlers) {
        this.clientHandlers = clientHandlers;
        this.clientHandler = clientHandler;
     /*   this.chatHistorySaver = new FileHistorySaver();   // PROBA ZMIANY NA JEDEN OUTPUT I INPUT STREAM
        this.chatHistoryReader = new FileHistoryReader();*/
    }

    public void processRequest(Request request) throws IOException {
        // set client's name if null
        if (clientHandler.getClientUsername() == null) {
            clientHandler.setClientUsername(request.getUserName());
        }
        if (request instanceof JoinGroupChatRequest) { // działa
            System.out.println(((JoinGroupChatRequest) request).getChannelName());
            logger.log(Level.INFO, "Joining to a group chat...!");
            clientHandler.setChannelName(((JoinGroupChatRequest) request).getChannelName());
            clientHandlers.addClientToRoom(clientHandler);
            JoinGroupChatResponse joinGroupChatResponse = new JoinGroupChatResponse(((JoinGroupChatRequest) request).getChannelName());
            broadcast(joinGroupChatResponse);
        }
        if (request instanceof GetAllChannelsRequest) { // działa
            List<String> channelNames = new ArrayList<>();
            ClientHandlers.getMapOfAllRooms().forEach((k, v) -> {
                channelNames.add(k);
            });
            GetAllChannelsResponse getAllChannelsResponse = new GetAllChannelsResponse(channelNames);
            if (getAllChannelsResponse.getAllChannelsNames() != null) {
                broadcast(getAllChannelsResponse);
            } else {
                String error = "no active channels";
                broadcast(new ErrorResponse(error));
            }
        }
        if (request instanceof MessageRequest) { // dziala
            logger.log(Level.INFO, "Handling message request...!");
            MessageResponse messageResponse = new MessageResponse(request.getUserName(), ((MessageRequest) request).getChannelName(), ((MessageRequest) request).getText());
            logger.log(Level.INFO, "proceeding to save msg to chat history......!");
            // TODO: save to file
            chatHistorySaver.save((MessageRequest)request);
            //
            logger.log(Level.INFO, "msg saved to history!");
            ArrayList<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(messageResponse.getChannelName());
            for (ClientHandler client : addressees) {
                client.getObjectOutputStream().writeObject(messageResponse);
                client.getObjectOutputStream().flush();
                //clientHandler.getObjectOutputStream().close();
                logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
            }
        }
        if (request instanceof RemoveFromChannelRequest) { // działa, mogę też znowu dołączyć!
            logger.log(Level.INFO, "Handling removing from channel request...!");
            clientHandlers.remove(clientHandler);
            MessageResponse messageResponse = new MessageResponse("SERVER: ", request.getUserName(), "HAS LEFT THE CHANNEL!");
            ArrayList<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(((RemoveFromChannelRequest) request).getChannelName());
            for (ClientHandler client : addressees) {
                client.getObjectOutputStream().writeObject(messageResponse);
                client.getObjectOutputStream().flush();
                logger.log(Level.INFO, "Message sent to " + client.getClientUsername());
            }
            logger.log(Level.INFO, "handling request to remove from channel has finished...!");
        }
        if (request instanceof GetChatHistoryRequest) { // działa ! Działa też kontrola dostępu po username
            logger.log(Level.INFO, "Trying to get channel history...");
            GetChatHistoryRequest getHistoryRequest = (GetChatHistoryRequest)request;
            try {
                logger.log(Level.INFO, "in try block");
                List<MessageRequest> channelHistory;
                channelHistory = chatHistoryReader.read(request.getUserName(), getHistoryRequest.getChannelName());
                logger.log(Level.INFO, "came back to request handler to try block");
                GetChatHistoryResponse response = new GetChatHistoryResponse(channelHistory);
                clientHandler.getObjectOutputStream().writeObject(response);
                clientHandler.getObjectOutputStream().flush();
            } catch (NoAccessToChatHistoryException exception) {
                System.out.println("to mój message z errora: " + exception.getMessage());
                String error = exception.getMessage();
                clientHandler.getObjectOutputStream().writeObject(new ErrorResponse(error));
                clientHandler.getObjectOutputStream().flush();
            }
        }
        if (request instanceof SendFileRequest){ // działa !!!
            logger.log(Level.INFO, "Handling send file request...");
            SendFileRequest sendFileRequest = (SendFileRequest)request;
            //SendFileRequestHandler sendFileRequestHandler = new SendFileRequestHandler();
            //byte[] file = sendFileRequestHandler.transformIntoBytes(sendFileRequest); // zamieniam plik na strumień bajtów
            //logger.log(Level.INFO, "File should have been transformerd! Returned to RequestHandler");
            ArrayList<ClientHandler> addressees = ClientHandlers.getMapOfAllRooms().get(sendFileRequest.getChannelName()); //biorę listę adresatów po kanale
            SendFileResponse sendFileResponse = new SendFileResponse(sendFileRequest.getUserName(), sendFileRequest.getChannelName(), sendFileRequest.getByteFile());
            for (ClientHandler client : addressees) { //iteruję po adresatach
                client.getObjectOutputStream().writeObject(sendFileResponse);  // wysyłam plik zawinięty w obiekt Response do każdego z nich
                client.getObjectOutputStream().flush();
                //clientHandler.getObjectOutputStream().close();
                logger.log(Level.INFO, "File sent to " + client.getClientUsername());
            }
        }
        // TODO: FURTHER REQUESTS
    }

    public void broadcast(Response response) throws IOException {
        logger.log(Level.INFO, "BROADCASTING RESPONSE " + response);
        clientHandler.getObjectOutputStream().writeObject(response);
        clientHandler.getObjectOutputStream().flush();
        logger.log(Level.INFO, "MESSAGE HAS BEEN BROADCASTED");
    }

}
