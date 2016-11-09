package server.http.requests.moves;

/**
 * Helper object for accepting a trade offer request.
 */
public class AcceptTradeRequest extends MoveRequest {

	private boolean willAccept;

	public boolean isWillAccept() {
		return willAccept;
	}
}
