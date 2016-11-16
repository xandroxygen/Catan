package server.http.handlers.moves;

import com.google.gson.Gson;
//import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.BuildSettlementCommand;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.BuildSettlementRequest;

/**
 * Handles requests to /moves/buildSettlement
 */
public class BuildSettlementHandler extends MoveHandler {
	public BuildSettlementHandler(IServerFacade server) {
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
		BuildSettlementRequest request = (new Gson()).fromJson(body, BuildSettlementRequest.class);

		BuildSettlementCommand command = new BuildSettlementCommand(server, gameID,
				request.getPlayerIndex(), request.getVertexLocation(), request.isFree());

		return executeCommand(command);

	}
}
