package client.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Takes care of post and get http requests.
 * Currently, also handles serialization requests. That may change.
 * Uses Java's HTTPURLConnection to connect to the server.
 * Server details are (for now) hard coded into the constructor of the ServerProxy class.
 */
public class HTTPOperations {
    private String host;
    private String port;
    private String baseUrl;

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";

    /**
     * Constructor for the HTTPOperations class.
     */
    public HTTPOperations() {
        host = "localhost";
        port = "8081";
        baseUrl = "http://" + host + ":" + port;
    }

    /**
     * GETs data from the server.
     *
     * @param url     REST url to call
     * @param headers any headers to pass, including cookies
     * @return a RequestResponse, which is either data or an error
     */
    public RequestResponse get(String url, Map<String, String> headers) throws MalformedURLException {
        try {
            URL requestURL = new URL(baseUrl + url);

            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            connection.setRequestMethod(HTTP_GET);

            for (String header : headers.keySet()) {
                connection.setRequestProperty(header, headers.get(header));
            }
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();

                List<String> responseCookies = connection.getHeaderFields().get("Set-Cookie");
                if (responseCookies != null && responseCookies.size() > 0) {
                    String[] cookieParts = responseCookies.get(0).split(";");
                    String cookie = cookieParts[0];
                    return new RequestResponse(false, responseToString(responseBody), cookie); // response OK
                }
                else {
                    return new RequestResponse(false, responseToString(responseBody)); // response OK
                }
            } else {
                InputStream responseBody = connection.getInputStream();
                return new RequestResponse(true, responseToString(responseBody)); // response not 200
            }
        } catch (IOException e) {
            return new RequestResponse(true, e);
        }
    }

    /**
     * POSTs data to the server.
     *
     * @param url     REST url to call
     * @param headers any headers to pass, including cookies
     * @param body    the body of the data
     * @return returns a RequestResponse, which is either data or an error
     */
    public RequestResponse post(String url, Map<String, String> headers, String body) throws MalformedURLException {
        try {
            URL requestURL = new URL(baseUrl + url);

            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            connection.setRequestMethod(HTTP_POST);

            for (String header : headers.keySet()) {
                connection.setRequestProperty(header, headers.get(header));
            }
            connection.setDoOutput(true);
            connection.connect();


            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(body.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();

                List<String> responseCookies = connection.getHeaderFields().get("Set-cookie");
                if (responseCookies != null && responseCookies.size() > 0) {
                    String[] cookieParts = responseCookies.get(0).split(";");
                    String cookie = cookieParts[0];
                    return new RequestResponse(false, responseToString(responseBody), cookie); // response OK
                }
                else {
                    return new RequestResponse(false, responseToString(responseBody)); // response OK
                }
            } else {
                InputStream responseBody = connection.getInputStream();
                return new RequestResponse(true, responseToString(responseBody)); // response not 200
            }
        } catch (IOException e) {
            return new RequestResponse(true, e); // other error
        }
    }

    /*
        Helper function: changes input stream response to a string for returning.
     */
    private String responseToString(InputStream response) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = response.read(buffer)) != -1) {
            byteStream.write(buffer, 0, length);
        }
        return byteStream.toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        baseUrl = "http://" + this.host + ":" + port;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
        baseUrl = "http://" + host + ":" + this.port;
    }
}



