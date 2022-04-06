package jmotyka.requests;

import lombok.Getter;

public class JoinPublicChannelRequest extends Request {

    @Getter
    private String channelName;

    public JoinPublicChannelRequest(String userName, RequestType requestType, String channelName) {
        super(userName, requestType);
        this.channelName = channelName;
    }
}
