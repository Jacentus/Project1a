package jmotyka.requests;

import lombok.Getter;

public class MessageRequest extends Request {

    @Getter
    private String text;
    @Getter
    private String channelName;

    public MessageRequest(String userName, String channelName, RequestType requestType, String text) {
        super(userName, requestType);
        this.text = text;
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "[" + super.getUserName() + " | " + channelName + "] " + text;
    }

}