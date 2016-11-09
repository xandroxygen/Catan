package server.http.requests.moves;

import shared.locations.HexLocation;

/**
 * Helper object for soldier requests.
 */
public class SoldierRequest extends MoveRequest {

	private int victimIndex;
	private HexLocation location;

	public int getVictimIndex() {
		return victimIndex;
	}

	public HexLocation getLocation() {
		return location;
	}
}
