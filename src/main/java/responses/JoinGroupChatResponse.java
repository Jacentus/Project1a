package responses;

import lombok.Getter;

public class JoinGroupChatResponse extends Response{

    @Getter
    private String channelName;

    public JoinGroupChatResponse(String channelName) {
        this.channelName = channelName;
    }
}
