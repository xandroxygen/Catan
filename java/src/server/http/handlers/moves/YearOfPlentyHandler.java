package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.YearOfPlentyCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.YearOfPlentyRequest;

/**
 * Handles requests to /moves/Year_Of_Plenty
 */
public class YearOfPlentyHandler extends MoveHandler {
	public YearOfPlentyHandler(IServerFacade server) {
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
		YearOfPlentyRequest request = new Gson().fromJson(body, YearOfPlentyRequest.class);

		YearOfPlentyCommand command = new YearOfPlentyCommand(server, gameID, request.getPlayerIndex(),
				request.getResource1(), request.getResource2());
		return executeCommand(command);
	}
}
