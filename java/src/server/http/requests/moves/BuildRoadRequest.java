package server.http.requests.moves;

import shared.locations.EdgeLocation;

/**
 * Helper object for building road requests.
 */
public class BuildRoadRequest extends MoveRequest {

	private EdgeLocation roadLocation;
	private boolean free;

	public EdgeLocation getRoadLocation() {
		return roadLocation;
	}

	public boolean isFree() {
		return free;
	}
}
