package jmotyka.exceptions;

import lombok.Getter;

public class NoAccessToChannelException extends Exception {

    @Getter
    private final String message;

    public NoAccessToChannelException(String message) {
        this.message = message;
    }

}