package jmotyka.responses;

import lombok.Getter;

public class JoinPublicChannelResponse extends Response {

    @Getter
    private String channelName;

    public JoinPublicChannelResponse(ResponseType responseType, String channelName) {
        super(responseType);
        this.channelName = channelName;
    }
}
