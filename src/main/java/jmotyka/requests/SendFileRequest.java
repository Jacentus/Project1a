package jmotyka.requests;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SendFileRequest extends Request {

    @Getter
    private byte[] file;
    private File filePath;
    private String channelName;

    public SendFileRequest(String userName, File filePath, String channelName) {
        super(userName);
        this.filePath = filePath;
        this.channelName = channelName;
    }

    public void transformIntoBytes(File filePath) throws IOException {
        byte[] byteFile = Files.readAllBytes(filePath.toPath());
        this.file = byteFile;
    }

}
