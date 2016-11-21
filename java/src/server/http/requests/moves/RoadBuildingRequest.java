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

	public void setSpot1(EdgeLocation spot1) {
		this.spot1 = spot1;
	}

	public void setSpot2(EdgeLocation spot2) {
		this.spot2 = spot2;
	}
}
