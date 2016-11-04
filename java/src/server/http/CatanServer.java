package server.http;

import com.sun.net.httpserver.HttpServer;
import server.http.handlers.ExampleHandler;

import java.net.InetSocketAddress;

/**
 * This is the entry point for our server implementation.
 * All requests from the client route through the HttpServer contained in this class.
 * All endpoint groups must be registered, and handlers created.
 * Specific endpoints are laid out in the Handler classes.
 *
 */
public class CatanServer {

	public static void main(String[] args) throws Exception {

		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8081), 0);

		// --- ENDPOINTS ---

		server.createContext("/", new ExampleHandler("Catan is online"));


		// -----------------
		server.setExecutor(null); // uses default
		server.start();
	}
}
