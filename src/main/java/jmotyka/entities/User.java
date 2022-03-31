package jmotyka.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

public class User {

    private static final AtomicInteger count = new AtomicInteger(0);
    @Getter
    private final int id;
    @Getter
    private String username;
    @Getter @Setter
    private Channel channel;

    public User(String username) {
        this.id = count.incrementAndGet();
        this.username = username;
    }

    public User(int id, String username, Channel channel) {
        this.id = id;
        this.username = username;
        this.channel = channel;
    }

}
