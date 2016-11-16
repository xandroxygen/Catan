package server.http.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.facade.IServerFacade;
import server.http.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Base handler helps to deserialize request.
 * Also reads and sets cookies.
 * Takes care of most HTTP details,
 * and leaves one method for subclasses to worry about
 */
public abstract class BaseHandler implements HttpHandler {

	protected String body;
	protected int responseCode;
	protected IServerFacade server;

	protected String playerCookie;
	protected String gameCookie;

	protected UserInfo user;
	protected int gameID;

	private String response;




	protected static final int RESPONSE_OK = 200;
	protected static final int RESPONSE_FAIL = 400;
	protected static final int RESPONSE_NOT_FOUND = 404;
	protected static final int RESPONSE_SERVER_FAIL = 500;
	protected static final int NOT_SET = -1;

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

		try {

			// reset variables - there is only one instance of each Handler class
			playerCookie = null;
			gameCookie = null;
			body = null;
			user = null;
			gameID = NOT_SET;
			responseCode = NOT_SET;

			readHeaders(httpExchange);

			decodeCookies();

			// deserialize requestBody to this.body
			InputStream is = httpExchange.getRequestBody();
			Scanner s = new Scanner(is).useDelimiter("\\A");
			body = s.hasNext() ? s.next() : "";

			// delegated to child classes
			response = respondToRequest(httpExchange);

			// set cookies - only sets if gameID or user is set
			generateGameCookie();
			generatePlayerCookie();
			writeCookies(httpExchange);

			// send response with code
			responseCode = (responseCode == NOT_SET) ? 200 : responseCode;
			httpExchange.sendResponseHeaders(responseCode, response.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		catch (Exception e) {
			httpExchange.sendResponseHeaders(RESPONSE_FAIL,e.getMessage().length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(e.getMessage().getBytes());
			os.close();
		}
	}

	/**
	 * Creates UserInfo object and sets gameID using the cookies that were sent with the request.
	 * @throws UnsupportedEncodingException
	 */
	private void decodeCookies() throws UnsupportedEncodingException {
		if (playerCookie != null) {
			String decodedCookie = URLDecoder.decode(playerCookie, "UTF-8");
			String[] parts = decodedCookie.split("=");
			decodedCookie = parts[1];
			JsonObject jsonCookie = new JsonParser().parse(decodedCookie).getAsJsonObject();

			String u = jsonCookie.get("name").getAsString();
			String p = jsonCookie.get("password").getAsString();
			int id = jsonCookie.get("playerID").getAsInt();

			user = new UserInfo();
			user.setUsername(u);
			user.setPassword(p);
			user.setPlayerID(id);
		}
		if (gameCookie != null) {
			String[] parts = gameCookie.split("=");
			gameID = Integer.parseInt(parts[1]);
		}
	}

	/**
	 * Sets player and game cookie attributes if those cookies are sent in the request.
	 * @param httpExchange
	 */
	private void readHeaders(HttpExchange httpExchange) {

		Headers headers = httpExchange.getRequestHeaders();
		String cookies = headers.getFirst("Cookie");
		if (cookies != null) {
			for (String c : cookies.split(";")) {
				if (c.contains("catan.user")) {
					playerCookie = c;
				} else if (c.contains("catan.game")) {
					gameCookie = c;
				}
			}
		}
	}

	/**
	 * Takes the current game and player cookies and adds them to the
	 * headers on the response.
	 * URL-encodes JSON representation of UserInfo.
	 * @param e the HTTPExchange passed into handle
	 */
	private void writeCookies(HttpExchange e) {

		String cookies = "";

		if (gameCookie != null) {
			cookies += gameCookie;
			if (playerCookie != null) {
				cookies += "; ";
			}
		}
		if (playerCookie != null) {
			cookies += playerCookie;
		}
		e.getResponseHeaders().add("Set-cookie", cookies);
	}

	/**
	 * Transforms this.gameID into this.gameCookie.
	 * Only sets gameCookie if gameID is not -1
	 */
	protected void generateGameCookie() {
		if (gameID != -1) {
			gameCookie = "catan.game=" + Integer.toString(gameID);
		}
	}

	/**
	 * Transforms this.user into this.playerCookie.
	 * Only sets cookies if user is not null
	 * @throws UnsupportedEncodingException
	 */
	protected void generatePlayerCookie() throws UnsupportedEncodingException {
		if (user != null) {
			playerCookie = "catan.user=" + URLEncoder.encode(new Gson().toJson(user), "UTF-8");
		}
	}

	public IServerFacade getServer() {
		return server;
	}

	public void setServer(IServerFacade server) {
		this.server = server;
	}

	/**
	 * Overridden by child handlers. This specifies what each request should do.
	 * eg. for buildRoad, this would construct a command to build a road and execute it.
	 *
	 * This method must:
	 * - construct the proper request object from the body using gson
	 * - set this.gameID, if it wants catan.game cookie returned
	 * - set this.user, if it wants catan.user cookie returned
	 * - set this.response code, if not 200
	 * - return the serialized response
	 * @return the response from the Model, serialized as a String.
	 */
	public abstract String respondToRequest(HttpExchange exchange) throws Exception;
}
