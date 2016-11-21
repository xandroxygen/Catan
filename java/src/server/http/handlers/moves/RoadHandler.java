package server.http.handlers.moves;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.RoadBuildingCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.RoadBuildingRequest;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

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

		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		JsonObject spot1 = json.getAsJsonObject("spot1");
		EdgeDirection dir = EdgeDirection.getEnumFromAbbrev(spot1.get("direction").getAsString().toUpperCase());
		HexLocation hex = new HexLocation(spot1.get("x").getAsInt(),spot1.get("y").getAsInt());
		request.setSpot1(new EdgeLocation(hex,dir));

		JsonObject spot2 = json.getAsJsonObject("spot2");
		dir = EdgeDirection.getEnumFromAbbrev(spot2.get("direction").getAsString().toUpperCase());
		hex = new HexLocation(spot2.get("x").getAsInt(),spot2.get("y").getAsInt());
		request.setSpot2(new EdgeLocation(hex,dir));

		RoadBuildingCommand command = new RoadBuildingCommand(server, gameID, request.getPlayerIndex(),
				request.getSpot1(), request.getSpot2());
		return executeCommand(command);
	}
}
