package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class JoinPrivateChannelRequest extends Request{

    @Getter
    private PrivateChannel privateChannel;

    public JoinPrivateChannelRequest(String userName, RequestType requestType, PrivateChannel privateChannel) {
        super(userName, requestType);
        this.privateChannel = privateChannel;
    }

}
