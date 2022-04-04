package jmotyka.requests;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;

public class GetPrivateChannelHistoryRequest extends GetChannelHistoryRequest {

    @Getter
    private PrivateChannel channel;

    public GetPrivateChannelHistoryRequest(String userName, PrivateChannel channel) {
        super(userName);
        this.channel = channel;
    }

}
