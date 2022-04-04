package jmotyka.responses;

import lombok.Getter;

public class JoinPublicChatResponse extends Response{

    @Getter
    private String channelName;

    public JoinPublicChatResponse(String channelName) {
        this.channelName = channelName;
    }
}
