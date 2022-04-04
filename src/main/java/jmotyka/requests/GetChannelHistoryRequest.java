package jmotyka.requests;

import lombok.Getter;

public abstract class GetChannelHistoryRequest extends Request {

    public GetChannelHistoryRequest(String userName) {
        super(userName);
    }

}
