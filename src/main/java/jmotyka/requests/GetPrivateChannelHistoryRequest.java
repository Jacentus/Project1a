package jmotyka.requests;

import lombok.Getter;

public class GetPrivateChannelHistoryRequest extends GetChannelHistoryRequest {

    public GetPrivateChannelHistoryRequest(String userName, RequestType requestType) {
        super(userName, requestType);
    }

}
