package jmotyka.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PrivateChannel {

    @Getter
    private String channelName;

    @Getter
    private List<String> permittedUsers;

    public PrivateChannel(String channelName) {
        this.channelName = channelName;
        this.permittedUsers = new ArrayList<>();
    }

}
