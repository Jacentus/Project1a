package jmotyka.requests;

import lombok.Getter;

public class RemoveFromPublicChannelRequest extends Request {

    @Getter
    private String channelName;

    public RemoveFromPublicChannelRequest(String userName, RequestType requestType, String channelName) {
        super(userName, requestType);
        this.channelName = channelName;
    }

}
