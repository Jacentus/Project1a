package jmotyka.requests;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SendFileRequest extends Request {

    @Getter
    private byte[] byteFile;
    @Getter
    private String channelName;

    public SendFileRequest(String userName, String channelName, byte[] byteFile) {
        super(userName);
        this.channelName = channelName;
        this.byteFile = byteFile;
    }

}
