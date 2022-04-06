package jmotyka.requests;

public abstract class GetChannelHistoryRequest extends Request {

    public GetChannelHistoryRequest(String userName, RequestType requestType) {
        super(userName, requestType);
    }

}
