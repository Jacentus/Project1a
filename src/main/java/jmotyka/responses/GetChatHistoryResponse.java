package jmotyka.responses;

import jmotyka.requests.MessageRequest;
import lombok.Getter;

import java.util.List;

public class GetChatHistoryResponse extends Response {

    @Getter
    private List<MessageRequest> chatHistory;

    public GetChatHistoryResponse(List<MessageRequest> chatHistory) {
        this.chatHistory = chatHistory;
    }

}
