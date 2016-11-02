package server.http;

import com.sun.net.httpserver.HttpServer;
import server.http.handlers.*;
import java.net.InetSocketAddress;

/**
 * This is the entry point for our server implementation.
 * Host needs to be changed at some point, probably.
 * All requests from the client route through the HttpServer contained in this class.
 * All endpoints must be registered, and handlers created.
 * Look into creating base handlers if possible, and passing in necessary data.
 */
public class CatanServer {

	public static void main(String[] args) throws Exception {

		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8081), 0); // TODO change host

		// --- ENDPOINTS ---

		server.createContext("/", new ExampleHandler("Catan is online"));

		// -----------------
		server.setExecutor(null); // uses default
		server.start();
	}
}
