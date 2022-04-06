package jmotyka.requests;

import lombok.Getter;

public class SendFilePrivatelyRequest extends SendFileRequest {


    public SendFilePrivatelyRequest(String userName, RequestType requestType, String fileName, byte[] byteFile) {
        super(userName, requestType, fileName, byteFile);
    }

}
