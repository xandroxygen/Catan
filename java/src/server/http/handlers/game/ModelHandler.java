package server.http.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;
import server.model.ServerGame;
import server.model.ServerModel;
import shared.model.InvalidActionException;

/**
 * Handles requests to /game/model (GET)
 */
public class ModelHandler extends BaseHandler {
	public ModelHandler(IServerFacade server) {
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
		
		String query = exchange.getRequestURI().getQuery();
		String[] pair = query.split("=");
		int version = Integer.parseInt(pair[1]);

		// catan.game must be set
		if (gameID > -1) {

			try {
				ServerGame model = server.gameGetModel(gameID, version);
				if(model != null) {
					return ModelSerializer.serializeGame(model);
				}
				else {
					return "\"true\"";
				}

			} catch (Exception e) {
				responseCode = RESPONSE_FAIL;
				return e.getMessage();
			}
		}
		else {
			responseCode = RESPONSE_FAIL;
			return "The catan.game cookie must be set before calling this.";
		}
	}
}
