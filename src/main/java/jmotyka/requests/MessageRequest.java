package jmotyka.requests;

import lombok.Getter;

public abstract class MessageRequest extends Request{

    @Getter
    private String text;

    public MessageRequest(String userName, RequestType requestType, String text) {
        super(userName, requestType);
        this.text = text;
    }

}
