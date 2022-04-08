package jmotyka.requests;

import lombok.Getter;

public class JoinPrivateChannelRequest extends Request {

    @Getter
    private String channelName;

    public JoinPrivateChannelRequest(String userName, RequestType requestType, String channelName) {
        super(userName, requestType);
        this.channelName = channelName;
    }

}