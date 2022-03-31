package jmotyka.entities;

import lombok.Getter;

public class Channel {

    @Getter
    private String channelName;

    @Getter
    private Boolean isPrivate = false;

    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public Channel(String channelName, Boolean isPrivate) {
        this.channelName = channelName;
        this.isPrivate = isPrivate;
    }

}
