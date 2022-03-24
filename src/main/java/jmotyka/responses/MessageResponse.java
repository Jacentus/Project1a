package jmotyka.responses;

import lombok.Getter;
import lombok.Setter;

public class MessageResponse extends Response{

    @Getter
    private String username;
    @Getter @Setter
    private String channelName;
    @Getter
    private String text;

    public MessageResponse(String username, String channelName, String text) {
        this.username = username;
        this.channelName = channelName;
        this.text = text;
    }

    @Override
    public String toString() {
        return
                "[" + username + " | " + channelName + "] " + '\'' +
                text + '\'';
    }
}
