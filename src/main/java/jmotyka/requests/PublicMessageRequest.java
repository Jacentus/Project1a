package jmotyka.requests;

import lombok.Getter;
import lombok.Setter;

public class PublicMessageRequest extends MessageRequest {

    @Getter @Setter
    private String channel;

    public PublicMessageRequest(String userName, RequestType requestType, String channel, String text) {
        super(userName, requestType, text);
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "[" + super.getUserName() + " | " + channel + "] " + '\'' +
                super.getText() + '\'';
    }

}

