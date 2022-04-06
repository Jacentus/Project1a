package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class PrivateMessageRequest extends MessageRequest{

    @Getter
    private PrivateChannel channel;

    public PrivateMessageRequest(String userName, RequestType requestType, PrivateChannel channel, String text) {
        super(userName, requestType, text);
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "[" + super.getUserName() + " | " + channel.getChannelName() + "] " + '\'' +
        super.getText() + '\'';
    }

}
