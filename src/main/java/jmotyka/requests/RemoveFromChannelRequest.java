package jmotyka.requests;

import lombok.Getter;

public class RemoveFromChannelRequest extends Request {

    @Getter
    private String channelName;

    public RemoveFromChannelRequest(String userName, RequestType requestType, String channelName) {
        super(userName, requestType);
        this.channelName = channelName;
    }

}