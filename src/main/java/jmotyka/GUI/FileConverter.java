package jmotyka.GUI;

import jmotyka.requests.SendFileRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileConverter {

    private final Logger logger = Logger.getLogger(getClass().getName()); //TODO: transfer to interface

    public byte[] transformIntoBytes(File file) {
        byte[] byteFile = new byte[0];
        logger.log(Level.INFO, "Inside transform method form GUI...");
        try {
            byteFile = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            logger.log(Level.INFO, "EXCEPTION READING FILE !!!");
            System.out.println("exception when transforming file into bytes");
            e.printStackTrace();
        }
        //SendFileRequest sendFileRequest = new SendFileRequest(userName, channelName, byteFile);
        return byteFile; //sendFileRequest;
    }



}
