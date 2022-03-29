package jmotyka.requests;

import lombok.Getter;

public class GetChatHistoryRequest extends Request {

    @Getter
    private String channelName;

    public GetChatHistoryRequest(String userName, String channelName) {
        super(userName);
        this.channelName = channelName;
    }
}
