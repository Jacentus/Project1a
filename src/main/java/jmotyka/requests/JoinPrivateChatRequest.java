package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class JoinPrivateChatRequest extends Request{

    @Getter
    private PrivateChannel privateChannel;

    public JoinPrivateChatRequest(String userName, PrivateChannel privateChannel) {
        super(userName);
        this.privateChannel = privateChannel;
    }

}
