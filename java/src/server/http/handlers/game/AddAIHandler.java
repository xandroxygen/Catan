package server.http.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.game.AddAIRequest;
import shared.model.InvalidActionException;

/**
 * Handles requests to /game/addAI
 */
public class AddAIHandler extends BaseHandler {
	public AddAIHandler(IServerFacade server) {
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
		AddAIRequest request = (new Gson()).fromJson(this.body, AddAIRequest.class);

		// catan.game must be set
		if (gameID > -1) {

			if (request.getAIType().equals("LARGEST_ARMY")) {

				try {
					server.gameAddAI(gameID, request.getAIType());
				} catch (InvalidActionException e) {
					responseCode = RESPONSE_FAIL;
					return "Invalid Request";
				}
			} else {
				responseCode = RESPONSE_FAIL;
				return "Could not add AI player [" + request.getAIType() + "]";
			}
		}
		else {
			responseCode = RESPONSE_FAIL;
			return "The catan.game cookie must be set before calling this.";
		}
		return null;
	}
}
