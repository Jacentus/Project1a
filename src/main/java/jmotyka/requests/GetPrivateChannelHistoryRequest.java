package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class GetPrivateChannelHistoryRequest extends GetChannelHistoryRequest {

    @Getter
    private PrivateChannel channel;

    public GetPrivateChannelHistoryRequest(String userName, RequestType requestType, PrivateChannel channel) {
        super(userName, requestType);
        this.channel = channel;
    }

}
