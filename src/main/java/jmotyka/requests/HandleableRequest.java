package jmotyka.requests;

public interface HandleableRequest {

    Request.RequestType getRequestType();

    String getUserName();

}