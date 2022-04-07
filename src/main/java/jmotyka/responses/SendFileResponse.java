package jmotyka.responses;

import lombok.Getter;


public class SendFileResponse extends Response{

    @Getter
    private String userName;
    @Getter
    private byte[] file;
    @Getter
    private String fileName;

    public SendFileResponse(ResponseType responseType, String userName, String fileName, byte[] file) {
        super(responseType);
        this.userName = userName;
        this.fileName = fileName;
        this.file = file;
    }

}

