package server.http.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.http.handlers.BaseHandler;

/**
 * Handles requests to /moves/offerTrade
 */
public class OfferTradeHandler extends BaseHandler {
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
		return null;
	}
}
