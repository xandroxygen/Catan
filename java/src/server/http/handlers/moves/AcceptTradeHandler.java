package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.AcceptTradeCommand;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.AcceptTradeRequest;
import shared.model.InvalidActionException;

/**
 * Handles requests to /moves/acceptTrade
 */
public class AcceptTradeHandler extends MoveHandler {
	public AcceptTradeHandler(IServerFacade server) {
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
		AcceptTradeRequest request = (new Gson()).fromJson(body, AcceptTradeRequest.class);

		AcceptTradeCommand command = new AcceptTradeCommand(server, gameID, request.isWillAccept());

		return executeCommand(command);
	}
}
