package server.http.handlers.moves;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.BuildCityCommand;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.BuildCityRequest;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 * Handles requests to /moves/buildCity
 */
public class BuildCityHandler extends MoveHandler {
	public BuildCityHandler(IServerFacade server) {
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
		BuildCityRequest request = (new Gson()).fromJson(body, BuildCityRequest.class);
		
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		JsonObject locationJSON = json.getAsJsonObject("vertexLocation");
		VertexDirection dir = VertexDirection.getEnumFromAbbrev(locationJSON.get("direction").getAsString().toUpperCase());
		HexLocation hex = new HexLocation(locationJSON.get("x").getAsInt(),locationJSON.get("y").getAsInt());
		request.setCityLocation(new VertexLocation(hex,dir));

		BuildCityCommand command = new BuildCityCommand(server, gameID,
				request.getPlayerIndex(), request.getVertexLocation(), request.isFree());

		return executeCommand(command);
	}
}
