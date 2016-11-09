package server.http.requests.games;

/**
 * Helper object for create game requests.
 */
public class CreateGameRequest {

	private boolean randomTiles;
	private boolean randomNumbers;
	private boolean randomPorts;
	private String name;

	public boolean isRandomTiles() {
		return randomTiles;
	}

	public boolean isRandomNumbers() {
		return randomNumbers;
	}

	public boolean isRandomPorts() {
		return randomPorts;
	}

	public String getName() {
		return name;
	}
}
