package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.RollNumberCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.RollRequest;

/**
 * Handles requests to /moves/rollNumber
 */
public class RollHandler extends MoveHandler {
	public RollHandler(IServerFacade server) {
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
		RollRequest request = new Gson().fromJson(body, RollRequest.class);

		RollNumberCommand command = new RollNumberCommand(server, gameID, request.getPlayerIndex(), request.getNumber());
		return executeCommand(command);
	}
}
