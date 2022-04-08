package jmotyka.responses;

import lombok.Getter;

import java.io.Serializable;

public abstract class Response implements Serializable, HandleableResponse {

    @Getter
    private ResponseType responseType;

    public Response(ResponseType responseType) {
        this.responseType = responseType;
    }

    public enum ResponseType {
        CREATE_PRIVATE_CHANNEL_RESPONSE, ERROR, GET_ALL_CHANNELS_RESPONSE, GET_CHANNEL_HISTORY_RESPONSE, JOIN_PRIVATE_CHANNEL_RESPONSE, JOIN_PUBLIC_CHANNEL_RESPONSE,
        MESSAGE_RESPONSE, SEND_FILE_RESPONSE;
    }

}