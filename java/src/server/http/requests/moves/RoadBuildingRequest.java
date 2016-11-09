package server.http.requests.moves;

import shared.locations.EdgeLocation;

/**
 * Helper object for road building dev card requests.
 */
public class RoadBuildingRequest extends MoveRequest {

	private EdgeLocation spot1;
	private EdgeLocation spot2;

	public EdgeLocation getSpot1() {
		return spot1;
	}

	public EdgeLocation getSpot2() {
		return spot2;
	}
}
