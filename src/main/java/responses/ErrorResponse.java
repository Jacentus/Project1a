package responses;

import lombok.Getter;

public class ErrorResponse extends Response{

    @Getter
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
