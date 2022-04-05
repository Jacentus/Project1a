package jmotyka.responses;

import lombok.Getter;

import java.util.List;

public class CreatePrivateChatResponse extends Response {

    @Getter
    private List<String> permittedUsers;
    @Getter
    private Boolean isPermitted;

    public CreatePrivateChatResponse(List<String> permittedUsers, Boolean isPermitted) {
    this.permittedUsers = permittedUsers;
    this.isPermitted = isPermitted;
    }

}
