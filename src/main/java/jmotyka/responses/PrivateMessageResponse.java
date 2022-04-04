package jmotyka.responses;

import jmotyka.entities.PrivateChannel;
import lombok.Getter;
import lombok.Setter;

public class PrivateMessageResponse extends MessageResponse {

    @Getter @Setter
    private PrivateChannel privateChannel;
    public PrivateMessageResponse(String username, PrivateChannel privateChannel,  String text) {
        super(username, text);
        this.privateChannel = privateChannel;
    }
/*
    @Override
    public String toString() {
        return "[" + super.getUsername() + " | " + privateChannel.getChannelName() + "] " + '\'' +
        super.getText() + '\'';;
    }*/
}
