package jmotyka.requests;

public class GetAllChannelsRequest extends Request{  // dostaję listę z nazwami wszytkich kanałów

    public GetAllChannelsRequest(String userName) {
        super(userName);
    }

}