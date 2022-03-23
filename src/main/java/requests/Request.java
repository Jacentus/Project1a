package requests;

import lombok.Getter;

import java.io.Serializable;

public abstract class Request implements Serializable {

    @Getter
    private String userName;

    public Request(String userName) {
        this.userName = userName;
    }
}
