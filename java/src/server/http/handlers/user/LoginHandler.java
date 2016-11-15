package server.http.handlers.user;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.facade.IServerFacade;
import server.http.UserInfo;
import server.http.handlers.BaseHandler;
import server.http.requests.user.UserRequest;
import shared.model.InvalidActionException;

/**
 * Handles requests to login.
 */
public class LoginHandler extends BaseHandler {

	public LoginHandler(IServerFacade server) {
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
		UserRequest request = new Gson().fromJson(this.body, UserRequest.class);

		int playerID;
		try {
			playerID = server.userLogin(request.getUsername(), request.getPassword());
		}
		catch (InvalidActionException e) {
			responseCode = RESPONSE_FAIL;
				return "Error";
		}

		user = new UserInfo();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setPlayerID(playerID);
		return "Success";
	}
}
