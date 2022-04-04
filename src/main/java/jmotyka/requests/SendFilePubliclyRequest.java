package jmotyka.requests;

import lombok.Getter;

public class SendFilePubliclyRequest extends SendFileRequest{

    @Getter
    private String channel;

    public SendFilePubliclyRequest(String userName, String fileName, byte[] byteFile, String channel) {
        super(userName, fileName, byteFile);
        this.channel = channel;
    }

}
