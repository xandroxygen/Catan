package server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * This is an ensample I give unto you.
 */
public class ExampleHandler implements HttpHandler {

	private String message;

	public ExampleHandler(String message) {
		this.message = message;
	}
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		String response = this.message;
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
