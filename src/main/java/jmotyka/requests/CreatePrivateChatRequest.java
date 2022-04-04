package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class CreatePrivateChatRequest extends Request {

    @Getter
    private PrivateChannel privateChannel;

    public CreatePrivateChatRequest(String userName, PrivateChannel privateChannel) {
        super(userName);
        this.privateChannel = privateChannel;
    }
}
