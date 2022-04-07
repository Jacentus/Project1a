package jmotyka.requests;

import lombok.Getter;

import java.io.Serializable;

public abstract class Request implements Serializable, HandleableRequest {

    @Getter
    private String userName;
    @Getter
    private RequestType requestType;

    public Request(String userName, RequestType requestType) {
        this.userName = userName;
        this.requestType = requestType;
    }

    public enum RequestType{
        CREATE_NEW_PRIVATE_CHANNEL, GET_ALL_CHANNELS, GET_CHANNEL_HISTORY, JOIN_PRIVATE_CHANNEL, JOIN_PUBLIC_CHANNEL,
        MESSAGE, REMOVE_FROM_CHANNEL, SEND_FILE_REQUEST, INTRODUCTION
    }

}
