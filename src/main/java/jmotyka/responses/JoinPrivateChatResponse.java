package jmotyka.responses;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class JoinPrivateChatResponse extends Response{

    @Getter
    private PrivateChannel channel;
    @Getter
    private Boolean isPermitted;

    public JoinPrivateChatResponse(PrivateChannel channel, Boolean isPermitted) {
        this.channel = channel;
        this.isPermitted = isPermitted;
    }

}


