package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class RemoveFromPrivateChannelRequest extends Request{

    @Getter
    private PrivateChannel channel;
    public RemoveFromPrivateChannelRequest(String userName, RequestType requestType, PrivateChannel channel) {
        super(userName, requestType);
        this.channel = channel;
    }
}
