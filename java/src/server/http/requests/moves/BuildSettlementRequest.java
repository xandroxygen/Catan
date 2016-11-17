package server.http.requests.moves;

import shared.locations.VertexLocation;

/**
 * Helper object for building settlement requests.
 */
public class BuildSettlementRequest extends MoveRequest {

	private VertexLocation vertexLocation;
	private boolean free;

	public VertexLocation getVertexLocation() {
		return vertexLocation;
	}

	public boolean isFree() {
		return free;
	}
	
	public void setFree(boolean free) {
		this.free = free;
	}
	
	public void setSettlementLocation(VertexLocation v) {
		this.vertexLocation = v;
	}
}
