package client.model;

import java.util.List;

/**
 * Map class.
 */
public class Map {
	
	private List<Hex> hexList;
	
	/**
	 * Checks whether the map is able to add a city.
	 * @param location the location of the city to place.
	 * @return
	 */
	public boolean canAddCity(MapLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the map is able to add a settlement.
	 * @param location the location of the settlement to place.
	 * @return
	 */
	public boolean canAddSettlement(MapLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the map is able to add a road.
	 * @param location the location of the road to place.
	 * @return
	 */
	public boolean canAddRoad(MapLocation location) {
		return false;
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
