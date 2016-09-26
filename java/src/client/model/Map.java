package client.model;

import java.util.List;

/**
 * Map class.
 */
public class Map {
	
	private List<Hex> hexList;
	private int radius;
	private List<Port> portList;

	public List<Port> getPortList() {
		return portList;
	}

	/**
	 * Checks whether there are resources to be offered to a player.
	 * @param diceRoll the value rolled by the dice.
	 * @param playerId the playerId of the player looking for resources around the rolled value.
	 * @return
	 */
	public boolean canGetRolledResourses(int diceRoll, int playerId ) {
		return false;
	}
}
