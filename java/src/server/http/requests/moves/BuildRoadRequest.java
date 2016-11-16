package server.http.requests.moves;

import com.google.gson.*;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import java.lang.reflect.Type;

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

	public void setRoadLocation(EdgeLocation roadLocation) {
		this.roadLocation = roadLocation;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
}
