package jmotyka.clientRequestHandlers;

import jmotyka.requests.SendFileRequest;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public class SendFileRequestHandler {

/*    private final Logger logger = Logger.getLogger(getClass().getName()); // ukryć pod interfejsem

    public byte[] transformIntoBytes(SendFileRequest request) {
        byte[] byteFile = new byte[0];
        logger.log(Level.INFO, "Inside transform method...");
        //File filePath = request.getFilePath();
        try {
            byteFile = Files.readAllBytes(filePath.toPath());
        } catch (IOException e) {
            logger.log(Level.INFO, "EXCEPTION READING FILE !!!");
            System.out.println("exception when transforming file into bytes");
            e.printStackTrace();
        }
        return byteFile;
    }*/

}
