package client.server;

import java.util.Map;

/**
 * Takes care of post and get http requests.
 * Runs asynchronously once called.
 * To receive request responses, implement CallbackProxy.
 * TODO: Confirm how to do async I/O and write this in.
 * Created by Xander on 9/13/2016.
 */
public class HTTPOperations {
    private String host;
    private String port;
    private String baseUrl;

    public HTTPOperations() {
        host = new String();
        port = new String();
        baseUrl = new String();
    }

    /**
     * Constructor for the HTTPOperations class.
     * @param h host of server to talk to
     * @param p port to talk on (usually 8081)
     * @param b the base URL to call
     */
    public HTTPOperations(String h, String p, String b) {
        host = h;
        port = p;
        baseUrl = b;
    }

    /**
     * POSTs data to the server.
     * @param url REST url to call
     * @param headers any headers to pass, including cookies
     * @param body the body of the data
     * @return returns a RequestResponse, which is either data or an error
     */
    public RequestResponse post(String url, Map<String, String> headers, String body) {
        return null;
    }

    /**
     * GETs data from the server.
     * @param url REST url to call
     * @param headers any headers to pass, including cookies
     * @return a RequestResponse, which is either data or an error
     */
    public RequestResponse get(String url, Map<String, String> headers) {
        return null;
    }
}

