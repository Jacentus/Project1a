package jmotyka.requests;

import lombok.Getter;

public class GetPublicChannelHistoryRequest extends GetChannelHistoryRequest {

    @Getter
    private String channel;

    public GetPublicChannelHistoryRequest(String userName, String channel) {
        super(userName);
        this.channel = channel;
    }

}
