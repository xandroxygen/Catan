package client.server;

/**
 * Wrapper class for HTTP request response.
 * Contains either response data or error.
 * Created by Xander on 9/13/2016.
 */
public class RequestResponse {
    private boolean hasError;
    private Exception error;
    private Object data;

    public RequestResponse(boolean hasError, Exception error) {
        this.hasError = hasError;
        this.error = error;
    }

    public RequestResponse(boolean hasError, Object data) {
        this.hasError = hasError;
        this.data = data;
    }

    public boolean hasError() {
        return hasError;
    }

    public Exception getError() {
        return error;
    }

    public Object getData() {
        return data;
    }
}
