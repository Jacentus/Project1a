package requests;

public abstract class WorkRequest extends Request {

    public WorkRequest(String userName) {
        super(userName);
    }

    public abstract Object execute();

}
