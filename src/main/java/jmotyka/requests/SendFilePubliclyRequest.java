package jmotyka.requests;

import lombok.Getter;

public class SendFilePubliclyRequest extends SendFileRequest{

    @Getter
    private String channel;

    public SendFilePubliclyRequest(String userName, RequestType requestType, String fileName, byte[] byteFile, String channel) {
        super(userName, requestType, fileName, byteFile);
        this.channel = channel;
    }

}
