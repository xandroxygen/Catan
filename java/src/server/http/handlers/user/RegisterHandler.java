package server.http.handlers.user;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.user.RegisterCommand;
import server.facade.IServerFacade;
import server.http.UserInfo;
import server.http.requests.user.UserRequest;
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
	 * <p>
	 * This method must:
	 * - construct the proper request object from the body using gson
	 * - set this.gameID, if it wants catan.game cookie returned
	 * - set this.user, if it wants catan.user cookie returned
	 * - set this.response code, if not 200
	 * - return the serialized response
	 *
	 * @param exchange
	 * @return the response from the Model, serialized as a String.
	 */
	@Override
	public String respondToRequest(HttpExchange exchange) throws Exception {
		UserRequest request = new Gson().fromJson(this.body, UserRequest.class);

		// do command here with username, password

		this.user = new UserInfo();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setPlayerID(0); // will be set with playerID from server.

		return "Success";
	}

}
