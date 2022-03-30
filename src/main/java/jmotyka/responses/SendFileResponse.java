package jmotyka.responses;

import lombok.Getter;

public class SendFileResponse extends Response{

    @Getter
    private String userName;
    @Getter
    private byte[] file;
    @Getter
    private String channelName;

    public SendFileResponse(String userName, String channelName, byte[] file) {
        this.userName = userName;
        this.channelName = channelName;
        this.file = file;
    }

}
