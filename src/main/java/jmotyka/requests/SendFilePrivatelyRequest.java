package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class SendFilePrivatelyRequest extends SendFileRequest {

    @Getter
    private PrivateChannel channel;

    public SendFilePrivatelyRequest(String userName, String fileName, byte[] byteFile, PrivateChannel channel) {
        super(userName, fileName, byteFile);
        this.channel = channel;
    }

}
