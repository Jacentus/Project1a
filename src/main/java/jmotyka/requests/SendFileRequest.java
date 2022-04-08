package jmotyka.requests;

import lombok.Getter;

public class SendFileRequest extends Request {

    @Getter
    private byte[] byteFile;
    @Getter
    private String fileName;
    @Getter
    private String channelName;

    public SendFileRequest(String userName, String channelName, RequestType requestType, String fileName, byte[] byteFile) {
        super(userName, requestType);
        this.channelName = channelName;
        this.fileName = fileName;
        this.byteFile = byteFile;
    }

}