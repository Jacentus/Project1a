package jmotyka.responses;

import lombok.Getter;

import java.util.List;

public class CreatePrivateChannelResponse extends Response {

    @Getter
    private List<String> permittedUsers;
    @Getter
    private Boolean isPermitted;

    public CreatePrivateChannelResponse(ResponseType responseType, List<String> permittedUsers, Boolean isPermitted) {
        super(responseType);
        this.permittedUsers = permittedUsers;
        this.isPermitted = isPermitted;
    }

    public CreatePrivateChannelResponse(ResponseType responseType, Boolean isPermitted) {
        super(responseType);
        this.isPermitted = isPermitted;
    }
}
