package jmotyka.responses;

import jmotyka.requests.MessageRequest;
import lombok.Getter;

import java.util.List;

public class GetChannelHistoryResponse extends Response {

    @Getter
    private List<MessageRequest> chatHistory;

    public GetChannelHistoryResponse(ResponseType responseType, List<MessageRequest> chatHistory) {
        super(responseType);
        this.chatHistory = chatHistory;
    }

}