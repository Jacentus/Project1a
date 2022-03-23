package serverResponseHandlers;

import responses.GetAllChannelsResponse;
import responses.Response;
import responses.SendFileResponse;

public class ResponseHandler {

    public ResponseHandler(Response response) {
        if (response instanceof GetAllChannelsResponse){
            System.out.print("ALL ROOMS IN CHAT: ");
            System.out.println(((GetAllChannelsResponse) response).getAllChannelsNames());
        }
        if (response instanceof SendFileResponse){

        }
    }


}
