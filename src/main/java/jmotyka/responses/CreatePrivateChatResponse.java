package jmotyka.responses;

import lombok.Getter;

import java.util.List;

public class CreatePrivateChatResponse extends Response {

    @Getter
    private List<String> permittedUsers;

    public CreatePrivateChatResponse(List<String> permittedUsers) {
    this.permittedUsers = permittedUsers;
    }

}
