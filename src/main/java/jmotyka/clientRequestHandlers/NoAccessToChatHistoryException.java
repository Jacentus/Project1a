package jmotyka.clientRequestHandlers;

import lombok.Getter;

public class NoAccessToChatHistoryException extends Exception {

    @Getter
    private String message;

    public NoAccessToChatHistoryException(String message) {
        this.message = message;
    }
}
