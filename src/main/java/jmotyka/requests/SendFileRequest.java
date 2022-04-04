package jmotyka.requests;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class SendFileRequest extends Request {

    @Getter
    private byte[] byteFile;
    @Getter
    private String fileName;

    public SendFileRequest(String userName, String fileName, byte[] byteFile) {
        super(userName);
        this.fileName = fileName;
        this.byteFile = byteFile;
    }

}
