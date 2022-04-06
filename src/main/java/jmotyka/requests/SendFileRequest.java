package jmotyka.requests;

import lombok.Getter;

public abstract class SendFileRequest extends Request {

    @Getter
    private byte[] byteFile;
    @Getter
    private String fileName;

    public SendFileRequest(String userName, RequestType requestType, String fileName, byte[] byteFile) {
        super(userName, requestType);
        this.fileName = fileName;
        this.byteFile = byteFile;
    }

}
