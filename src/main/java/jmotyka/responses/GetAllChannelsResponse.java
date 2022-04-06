package jmotyka.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GetAllChannelsResponse extends Response {

    @Getter @Setter
    List<String> allChannelsNames;

    public GetAllChannelsResponse(ResponseType responseType, List<String> allChannelsNames) {
        super(responseType);
        this.allChannelsNames = allChannelsNames;
    }
}
