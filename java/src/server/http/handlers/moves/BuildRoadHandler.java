package server.http.handlers.moves;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.BuildRoadCommand;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.BuildRoadRequest;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import java.lang.reflect.Type;

/**
 * Handles requests to /moves/buildRoad
 */
public class BuildRoadHandler extends MoveHandler {
	public BuildRoadHandler(IServerFacade server) {
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
		BuildRoadRequest request = new Gson().fromJson(body, BuildRoadRequest.class);

		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		request.setFree(json.get("free").getAsBoolean());
		JsonObject locationJSON = json.getAsJsonObject("roadLocation");
		EdgeDirection dir = EdgeDirection.getEnumFromAbbrev(locationJSON.get("direction").getAsString().toUpperCase());
		HexLocation hex = new HexLocation(locationJSON.get("x").getAsInt(),locationJSON.get("y").getAsInt());
		request.setRoadLocation(new EdgeLocation(hex,dir));


		BuildRoadCommand command = new BuildRoadCommand(server, gameID,
				request.getPlayerIndex(), request.getRoadLocation(), request.isFree());

		return executeCommand(command);
	}
}
