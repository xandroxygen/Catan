package server.http.handlers.games;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.model.ServerGame;
import shared.model.Player;

import java.util.ArrayList;

/**
 *Handles requests to /games/list (GET)
 */
public class ListHandler extends BaseHandler {
	public ListHandler(IServerFacade server) {
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

		try {
			ArrayList<ServerGame> games = server.gamesList();

			// serialize parts for response
			JsonArray jsonArray = new JsonArray();
			for (ServerGame game : games) {

				JsonObject jGame = new JsonObject();
				jGame.addProperty("title", game.getGameName());
				jGame.addProperty("id", game.getGameId());
				JsonArray players = new JsonArray();
				for (Player player : game.getPlayerList()) {

					JsonObject jPlayer = new JsonObject();
					jPlayer.addProperty("color", player.getColor().toString());
					jPlayer.addProperty("name", player.getName());
					jPlayer.addProperty("id", player.getPlayerID());
					players.add(jPlayer);
				}
				while (players.size() < 4) {
					players.add(new JsonObject());
				}
				jGame.add("players", players);
				jsonArray.add(jGame);
			}

			return jsonArray.toString();
		}
		catch (Exception e) {
			responseCode = 400;
			return "Invalid Request";
		}

	}
}
