package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.DiscardCardsCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.DiscardRequest;

/**
 * Handles requests to /moves/discardCards
 */
public class DiscardHandler extends MoveHandler {
	public DiscardHandler(IServerFacade server) {
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
		DiscardRequest request = (new Gson()).fromJson(body, DiscardRequest.class);

		DiscardCardsCommand command = new DiscardCardsCommand(server, gameID, request.getPlayerIndex(), request.getDiscardedCards());

		return executeCommand(command);
	}
}