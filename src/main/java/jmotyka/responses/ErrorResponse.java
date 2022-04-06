package jmotyka.responses;

import lombok.Getter;

public class ErrorResponse extends Response{

    @Getter
    private String message;

    public ErrorResponse(ResponseType responseType, String message) {
        super(responseType);
        this.message = message;
    }
}
