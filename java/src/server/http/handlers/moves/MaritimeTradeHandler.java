package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.MaritimeTradeCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.MaritimeTradeRequest;
import shared.model.InvalidActionException;

/**
 * Handles requests to /moves/maritimeTrade
 */
public class MaritimeTradeHandler extends MoveHandler {
	public MaritimeTradeHandler(IServerFacade server) {
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
		MaritimeTradeRequest request = (new Gson()).fromJson(body, MaritimeTradeRequest.class);

		try {
			MaritimeTradeCommand command = new MaritimeTradeCommand(server, gameID, request.getPlayerIndex(), request.getRatio(),
					request.findResource(request.getInputResource()), request.findResource(request.getOutputResource()));
			return executeCommand(command);
		}
		catch (InvalidActionException e) {
			responseCode = RESPONSE_FAIL;
			return e.message;
		}

	}
}
