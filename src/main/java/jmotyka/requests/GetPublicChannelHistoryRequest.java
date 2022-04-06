package jmotyka.requests;

import lombok.Getter;

public class GetPublicChannelHistoryRequest extends GetChannelHistoryRequest {

    @Getter
    private String channel;

    public GetPublicChannelHistoryRequest(String userName, RequestType requestType, String channel) {
        super(userName, requestType);
        this.channel = channel;
    }

}
