package jmotyka.requests;

public class IntroductionRequest extends Request implements HandleableRequest{

    public IntroductionRequest(String userName, RequestType requestType) {
        super(userName, requestType);
    }
}
