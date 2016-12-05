package server.http.handlers.games;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import server.command.games.JoinCommand;
import server.command.moves.AcceptTradeCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.games.JoinGameRequest;
import server.http.requests.moves.AcceptTradeRequest;
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
				// new code so that we can store the command
				JoinCommand command = new JoinCommand(server, request.getId(), user.getPlayerID(), CatanColor.valueOf(request.getColor().toUpperCase()));
				command.execute();
				
				// old code
				//server.gamesJoin(request.getId(), user.getPlayerID(), CatanColor.valueOf(request.getColor().toUpperCase()));

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
