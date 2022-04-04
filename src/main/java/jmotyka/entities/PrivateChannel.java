package jmotyka.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrivateChannel implements Serializable {

    @Getter
    private String channelName;

    @Getter
    private List<String> permittedUsers;

    @Getter @Setter
    private Boolean clientPermittedToChat = false;

    public PrivateChannel(String channelName) {
        this.channelName = channelName;
        this.permittedUsers = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateChannel)) return false;
        PrivateChannel that = (PrivateChannel) o;
        return Objects.equals(getChannelName(), that.getChannelName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannelName());
    }

}
