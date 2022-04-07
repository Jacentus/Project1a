package jmotyka.requests;

import lombok.Getter;

public class GetChannelHistoryRequest extends Request {

    @Getter
    private String channelName;

    public GetChannelHistoryRequest(String userName, String channelName, RequestType requestType) {
        super(userName, requestType);
        this.channelName = channelName;
    }

}
