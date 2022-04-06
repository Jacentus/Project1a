package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class SendFilePrivatelyRequest extends SendFileRequest {

    @Getter
    private PrivateChannel channel;

    public SendFilePrivatelyRequest(String userName, RequestType requestType, String fileName, byte[] byteFile, PrivateChannel channel) {
        super(userName, requestType, fileName, byteFile);
        this.channel = channel;
    }

}
