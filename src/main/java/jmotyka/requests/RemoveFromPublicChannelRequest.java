package jmotyka.requests;

import lombok.Getter;

public class RemoveFromPublicChannelRequest extends Request {

    @Getter
    private String channelName;

    public RemoveFromPublicChannelRequest(String userName, String channelName) {
        super(userName);
        this.channelName = channelName;
    }

}
