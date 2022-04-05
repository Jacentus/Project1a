package jmotyka.responses;

import lombok.Getter;
import lombok.Setter;

public abstract class MessageResponse extends Response{

    @Getter
    private String username;
    @Getter
    private String text;

    public MessageResponse(String username, String text) {
        this.username = username;
        this.text = text;
    }

    @Override
    public String toString() {
        return
                "[" + username +  "] " + '\'' +
                text + '\'';
    }
}
