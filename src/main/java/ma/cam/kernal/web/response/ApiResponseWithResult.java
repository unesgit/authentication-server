package ma.cam.kernal.web.response;

public class ApiResponseWithResult<T> extends ApiResponse{

    private Object result;

    public ApiResponseWithResult(int status, String message, Object result) {
    	super(status, message);
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
