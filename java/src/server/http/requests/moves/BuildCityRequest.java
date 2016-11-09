package server.http.requests.moves;

import shared.locations.VertexLocation;

/**
 * Helper object for building city requests.
 */
public class BuildCityRequest extends MoveRequest {

	private VertexLocation vertexLocation;

	public VertexLocation getVertexLocation() {
		return vertexLocation;
	}
}
