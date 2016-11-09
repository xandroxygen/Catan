package server.http.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.facade.IServerFacade;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Base handler helps to deserialize request.
 * Also reads and sets cookies.
 * Takes care of most HTTP details,
 * and leaves one method for subclasses to worry about
 */
public abstract class BaseHandler implements HttpHandler {

	protected String body;
	private String playerCookie;
	private String gameCookie;
	private int responseCode;
	private IServerFacade server;



	private static final int RESPONSE_OK = 200;
	private static final int RESPONSE_FAIL = 400;
	private static final int RESPONSE_NOT_FOUND = 404;
	private static final int RESPONSE_SERVER_FAIL = 500;

	public BaseHandler(IServerFacade server) {
		this.server = server;
	}

	/**
	 * The Base Handler takes care of all the HTTP stuff in the handle function.
	 * It calls all the helper functions, and sets all the needed attributes.
	 * It then calls respondToRequest, which all subclasses need to implement.
	 * After a response is readied, the response is passed back.
	 * @param httpExchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		Headers headers = httpExchange.getRequestHeaders();
		playerCookie = headers.getFirst("catan.user");
		gameCookie = headers.getFirst("catan.game");

		// deserialize requestBody to this.body
		InputStream is = httpExchange.getRequestBody();
		Scanner s = new Scanner(is).useDelimiter("\\A");
		body = s.hasNext() ? s.next() : "";
		respondToRequest(httpExchange);
	}

	/**
	 * Takes the current game cookie attribute and adds it to the
	 * headers on the response.
	 * @param e the HTTPExchange passed into handle
	 */
	public void writeGameCookie(HttpExchange e) {
		e.getResponseHeaders().put("catan.game", new ArrayList<>());
		e.getResponseHeaders().get("catan.game").add(gameCookie);
	}

	/**
	 * Takes the current game cookie attribute and adds it to the
	 * headers on the response.
	 * @param e the HTTPExchange passed into handle
	 */
	public void writePlayerCookie(HttpExchange e) {
		e.getResponseHeaders().put("catan.user", new ArrayList<>());
		e.getResponseHeaders().get("catan.user").add(playerCookie);
	}

	public IServerFacade getServer() {
		return server;
	}

	public void setServer(IServerFacade server) {
		this.server = server;
	}

	/**
	 * Overridden by child handlers. This specifies what each request should do.
	 * This must also construct a request object of the proper type using gson.
	 * eg. for buildRoad, this would construct a command to build a road and execute it.
	 * This method must also set a response code, and can set the cookies attributes.
	 * This method is in charge of writing needed cookies.
	 * @return the response from the Model, serialized as a String.
	 */
	public abstract String respondToRequest(HttpExchange exchange);
}
