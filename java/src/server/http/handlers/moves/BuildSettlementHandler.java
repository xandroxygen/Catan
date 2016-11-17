package server.http.handlers.moves;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.BuildSettlementCommand;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.BuildSettlementRequest;
import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

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
		
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		request.setFree(json.get("free").getAsBoolean());
		JsonObject locationJSON = json.getAsJsonObject("vertexLocation");
		VertexDirection dir = VertexDirection.getEnumFromAbbrev(locationJSON.get("direction").getAsString().toUpperCase());
		HexLocation hex = new HexLocation(locationJSON.get("x").getAsInt(),locationJSON.get("y").getAsInt());
		request.setSettlementLocation(new VertexLocation(hex,dir));

		BuildSettlementCommand command = new BuildSettlementCommand(server, gameID,
				request.getPlayerIndex(), request.getVertexLocation(), request.isFree());

		return executeCommand(command);

	}
}
