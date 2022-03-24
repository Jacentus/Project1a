package jmotyka.responses;

import lombok.Getter;

import java.util.List;

public class GetAllChannelsResponse extends Response {

    @Getter
    List<String> allChannelsNames;

    public GetAllChannelsResponse(List<String> keyList) {
        this.allChannelsNames = keyList;
    }

}
