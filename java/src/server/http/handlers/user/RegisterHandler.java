package server.http.handlers.user;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.facade.IServerFacade;
import server.http.RegisterRequest;
import server.http.handlers.BaseHandler;

/**
 * Handles register requests.
 */
public class RegisterHandler extends BaseHandler {

	public RegisterHandler(IServerFacade server) {
		super(server);
	}

	/**
	 * Overridden by child handlers. This specifies what each request should do.
	 * eg. for buildRoad, this would construct a command to build a road and execute it.
	 * This method must also set a response code, and can set the cookies attributes.
	 * This method is in charge of writing needed cookies.
	 *
	 * @param exchange
	 * @return the response from the Model, serialized as a String.
	 */
	@Override
	public String respondToRequest(HttpExchange exchange) {
		RegisterRequest request = new Gson().fromJson(this.body, RegisterRequest.class);
		return null;
	}
}
