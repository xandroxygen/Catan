package server.http.requests.moves;

import shared.locations.VertexLocation;

/**
 * Helper object for building city requests.
 */
public class BuildCityRequest extends MoveRequest {

	private VertexLocation vertexLocation;
	private boolean isFree;

	public VertexLocation getVertexLocation() {
		return vertexLocation;
	}

	public boolean isFree() {
		return isFree;
	}
	
	public void setCityLocation(VertexLocation loc) {
		this.vertexLocation = loc;
	}
}
