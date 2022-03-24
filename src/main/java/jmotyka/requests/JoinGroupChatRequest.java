package jmotyka.requests;

import lombok.Getter;

public class JoinGroupChatRequest extends Request {

    @Getter
    private String channelName;

    public JoinGroupChatRequest(String userName, String channelName) {
        super(userName);
        this.channelName = channelName;
    }
}
