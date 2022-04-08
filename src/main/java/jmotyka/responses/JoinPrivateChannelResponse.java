package jmotyka.responses;

import lombok.Getter;

public class JoinPrivateChannelResponse extends Response {

    @Getter
    private Boolean isPermitted;
    @Getter
    private String channelName;

    public JoinPrivateChannelResponse(ResponseType responseType, Boolean isPermitted, String channelName) {
        super(responseType);
        this.isPermitted = isPermitted;
        this.channelName = channelName;
    }

}


