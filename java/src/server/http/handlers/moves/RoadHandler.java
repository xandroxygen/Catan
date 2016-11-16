package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.RoadBuildingCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.RoadBuildingRequest;

/**
 * Handles requests to /moves/RoadHandler
 */
public class RoadHandler extends MoveHandler {
	public RoadHandler(IServerFacade server) {
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
		RoadBuildingRequest request = new Gson().fromJson(body, RoadBuildingRequest.class);

		RoadBuildingCommand command = new RoadBuildingCommand(server, gameID, request.getPlayerIndex(),
				request.getSpot1(), request.getSpot2());
		return executeCommand(command);
	}
}
