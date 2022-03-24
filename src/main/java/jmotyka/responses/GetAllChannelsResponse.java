package jmotyka.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GetAllChannelsResponse extends Response {

    @Getter @Setter
    List<String> allChannelsNames;

    public GetAllChannelsResponse(List<String> keyList) {
        this.allChannelsNames = keyList;
    }

}
