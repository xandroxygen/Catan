package server.http.requests.moves;

/**
 * Helper object for maritime trade requests.
 */
public class MaritimeTradeRequest extends MoveRequest {

	private int ratio;
	private String inputResource;
	private String outputResource;

	public int getRatio() {
		return ratio;
	}

	public String getInputResource() {
		return inputResource;
	}

	public String getOutputResource() {
		return outputResource;
	}
}
