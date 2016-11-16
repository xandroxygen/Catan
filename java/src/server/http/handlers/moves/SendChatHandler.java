package server.http.handlers.moves;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.command.moves.SendChatCommand;
import server.facade.IServerFacade;
import server.http.handlers.BaseHandler;
import server.http.requests.moves.ChatRequest;
import server.http.requests.moves.MoveRequest;

/**
 * Handles requests to /moves/sendChat
 */
public class SendChatHandler extends MoveHandler {
	public SendChatHandler(IServerFacade server) {
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
		ChatRequest request = new Gson().fromJson(body, ChatRequest.class);

		SendChatCommand command = new SendChatCommand(server, gameID, request.getPlayerIndex(), request.getContent());
		return executeCommand(command);
	}
}
