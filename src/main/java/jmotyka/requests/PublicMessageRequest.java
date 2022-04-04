package jmotyka.requests;

import lombok.Getter;
import lombok.Setter;

public class PublicMessageRequest extends MessageRequest {

    @Getter @Setter
    private String channel;

    public PublicMessageRequest(String userName, String channel, String text) {
        super(userName, text);
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "[" + super.getUserName() + " | " + channel + "] " + '\'' +
                super.getText() + '\'';
    }

}

