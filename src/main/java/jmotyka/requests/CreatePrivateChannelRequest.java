package jmotyka.requests;

import lombok.Getter;

import java.util.List;

public class CreatePrivateChannelRequest extends Request {

    @Getter
    private String channelName;
    @Getter
    private Boolean isPrivate;
    @Getter
    private List<String> permittedUsers;

    public CreatePrivateChannelRequest(String userName, String channelName, RequestType requestType, Boolean isPrivate, List<String> permittedUsers) {
        super(userName, requestType);
        this.channelName = channelName;
        this.isPrivate = isPrivate;
        this.permittedUsers = permittedUsers;
    }
    
}
