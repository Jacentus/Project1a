package jmotyka.requests;

import lombok.Getter;

public class RemoveFromChannelRequest extends Request {

    @Getter
    private String channelName;

    public RemoveFromChannelRequest(String userName, String channelName) {
        super(userName);
        this.channelName = channelName;
    }

}
