package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class CreatePrivateChannelRequest extends Request {

    @Getter
    private PrivateChannel privateChannel;

    public CreatePrivateChannelRequest(String userName, RequestType requestType, PrivateChannel privateChannel) {
        super(userName, requestType);
        this.privateChannel = privateChannel;
    }
    
}
