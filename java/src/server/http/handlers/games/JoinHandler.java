package server.http.handlers.games;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.org.apache.bcel.internal.generic.FADD;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.games.JoinGameRequest;
import shared.definitions.CatanColor;

/**
 * Handles requests for /games/join
 */
public class JoinHandler extends BaseHandler {
	public JoinHandler(IServerFacade server) {
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
		JoinGameRequest request = new Gson().fromJson(this.body, JoinGameRequest.class);

		// verify player cookie is set
		if (this.user != null) {
			try {
				server.gamesJoin(request.getId(), user.getPlayerID(), CatanColor.valueOf(request.getColor().toUpperCase()));

				// set game cookie
				this.gameID = request.getId();
				return "Success";

			} catch (Exception e) {
				responseCode = RESPONSE_FAIL;
				return "Invalid Request";
			}
		}
		else {
			responseCode = RESPONSE_FAIL;
			return "The catan.user HTTP cookie is missing.  You must login before calling this method.";
		}
	}
}
