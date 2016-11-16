package server.http.handlers.games;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.games.CreateGameRequest;
import server.http.requests.user.UserRequest;
import server.model.ServerGame;
import shared.model.InvalidActionException;

/**
 * Handles requests for /games/create
 */
public class CreateHandler extends BaseHandler {
	public CreateHandler(IServerFacade server) {
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
		CreateGameRequest request = new Gson().fromJson(this.body, CreateGameRequest.class);

		try {
			ServerGame game = server.gamesCreate(request.getName(), request.isRandomTiles(), request.isRandomNumbers(), request.isRandomPorts());
			JsonObject jo = new JsonObject();
			jo.addProperty("title", game.getGameName());
			jo.addProperty("id", game.getGameId());
			JsonArray players = new JsonArray();
			players.add(new JsonObject());
			jo.add("players", players);
			return jo.toString();
		}
		catch (InvalidActionException e) {
			responseCode = RESPONSE_FAIL;
			return "Invalid Request";
		}
	}
}
