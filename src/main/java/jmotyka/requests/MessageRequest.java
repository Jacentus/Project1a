package jmotyka.requests;

import lombok.Getter;
import lombok.Setter;

public class MessageRequest extends Request{

    @Getter @Setter
    private String channelName;
    @Getter
    private String text;

    public MessageRequest(String userName, String channelName, String text) {
        super(userName);
        this.text = text;
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "[" + super.getUserName() + " | " + channelName + "] " + '\'' +
                text + '\'';
    }
}
