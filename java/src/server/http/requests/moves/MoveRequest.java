package server.http.requests.moves;

/**
 * Base class for all move requests.
 */
public class MoveRequest {

	private String type;
	private int playerIndex;

	public String getType() {
		return type;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}
}
