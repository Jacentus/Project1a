package jmotyka.exceptions;

import lombok.Getter;

public class NoAccesToChannelException extends Exception {

    @Getter
    private String message;

    public NoAccesToChannelException(String message) {
        this.message = message;
    }

}
