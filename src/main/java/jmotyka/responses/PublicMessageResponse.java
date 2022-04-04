package jmotyka.responses;

import lombok.Getter;
import lombok.Setter;

public class PublicMessageResponse extends MessageResponse {

    @Getter @Setter
    private String channelName;

    public PublicMessageResponse(String username, String channelName, String text) {
        super(username, text);
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return
                "[" + super.getUsername() + " | " + channelName + "] " + '\'' +
                        super.getText() + '\'';
    }

}
