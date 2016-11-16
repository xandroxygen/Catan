package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.MonopolyCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.MonopolyRequest;
import shared.model.InvalidActionException;

/**
 * Handles requests to /moves/Monopoly
 */
public class MonopolyHandler extends MoveHandler {
	public MonopolyHandler(IServerFacade server) {
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
		MonopolyRequest request = (new Gson()).fromJson(body, MonopolyRequest.class);

		try {
			MonopolyCommand command = new MonopolyCommand(server, gameID, request.getPlayerIndex(),
					request.findResource(request.getResource()));
			return executeCommand(command);
		}
		catch (InvalidActionException e) {
			responseCode = RESPONSE_FAIL;
			return e.message;
		}
	}
}
