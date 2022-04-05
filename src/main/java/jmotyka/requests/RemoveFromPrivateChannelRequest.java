package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class RemoveFromPrivateChannelRequest extends Request{

    @Getter
    private PrivateChannel channel;
    public RemoveFromPrivateChannelRequest(String userName, PrivateChannel channel) {
        super(userName);
        this.channel = channel;
    }
}
