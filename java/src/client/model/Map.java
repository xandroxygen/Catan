package client.model;

import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Map class.
 */
public class Map {
	
	private HashMap<HexLocation, Hex> hexes;
	private HashMap<VertexLocation, Municipality> municipalities;
	private HashMap<EdgeLocation, Road> roads;
	private int radius;
	private HashMap<HexLocation, Port> ports;
	private Robber robber;
	
	
	public Map(JsonObject mapJSON) {
		JsonArray hexJSONArray = mapJSON.getAsJsonArray("hexes");
		for (JsonElement hexElement: hexJSONArray) {
			Hex hex = new Gson().fromJson(hexElement, Hex.class);
			hexes.put(hex.getLocation(), hex);
		}
		JsonArray roadsJSON = mapJSON.getAsJsonArray("roads");
		for (JsonElement roadElement : roadsJSON) {
			Road road = new Gson().fromJson(roadElement, Road.class);
			roads.put(road.getLocation(), road);
		}
		JsonArray settlementsJSON = mapJSON.getAsJsonArray("settlements");
		for (JsonElement settlementElement : settlementsJSON) {
			Settlement settlement = new Gson().fromJson(settlementElement, Settlement.class);
			municipalities.put(settlement.getLocation(), settlement);
		}
		JsonArray citiesJSON = mapJSON.getAsJsonArray("cities");
		for (JsonElement cityElement : citiesJSON) {
			City city = new Gson().fromJson(cityElement, City.class);
			municipalities.put(city.getLocation(), city);
		}
		JsonArray portsJSON = mapJSON.getAsJsonArray("ports");
		for (JsonElement portElement : portsJSON) {
			Port port = new Gson().fromJson(portElement, Port.class);
			ports.put(port.getLocation(), port);
		}
		radius = mapJSON.getAsJsonObject("radius").getAsInt();
		robber = new Gson().fromJson(mapJSON.getAsJsonObject("robber"), Robber.class);
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
