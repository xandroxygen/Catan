package server.http.handlers.moves;

import client.server.RequestResponse;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.Command;
import server.facade.IServerFacade;
import server.http.ModelSerializer;
import server.http.handlers.BaseHandler;

/**
 * Base class for all Move Handlers, since they are all the same.
 */
public abstract class MoveHandler extends BaseHandler {
	public MoveHandler(IServerFacade server) {
		super(server);
	}

	/**
	 * Checks if both cookies are set, executes command. Catches exceptions and returns.
	 * @param command the Command object for this request
	 * @return the request body - the serialized model
	 */
	public String executeCommand(Command command) {

		// both cookies must be set
		if (gameID > -1 && user != null) {

			try {
				command.execute();
				return ModelSerializer.serializeGame(server.gameGetModel(gameID));
			}
			catch (Exception e) {
				responseCode = RESPONSE_FAIL;
				return e.getMessage();
			}
		}
		else {
			responseCode = RESPONSE_FAIL;
			return "Both catan.game and catan.user cookies must be set.";
		}
	}
}
