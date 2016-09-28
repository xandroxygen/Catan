package client.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import shared.locations.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Map class.
 */
public class Map {
	
	private HashMap<HexLocation, Hex> hexes;
	private HashMap<VertexLocation, City> cities;
	private HashMap<VertexLocation, Settlement> settlements;
    private HashMap<EdgeLocation, Road> roads;
    private int radius;
    private HashMap<EdgeLocation, Port> ports;

//    private List<Port> portList;

    private Robber robber;

	public HashMap<VertexLocation, City> getCities() {
		return cities;
	}

	public HashMap<VertexLocation, Settlement> getSettlements() {
		return settlements;
	}

	public HashMap<EdgeLocation, Road> getRoads() {
		return roads;
	}

	public Robber getRobber() {
		return robber;
	}

//	public List<Port> getPortList() {
//		return portList;
//	}


	public HashMap<EdgeLocation, Port> getPorts() {
		return ports;
	}

	/**
     * Constructor for creating Map with JsonObject
     * @param mapJSON
     */
	public Map(JsonObject mapJSON) {
		hexes = new HashMap<>();
		cities = new HashMap<>();
		settlements = new HashMap<>();
		roads = new HashMap<>();
		ports = new HashMap<>();
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
			settlements.put(settlement.getLocation(), settlement);
		}
		JsonArray citiesJSON = mapJSON.getAsJsonArray("cities");
		for (JsonElement cityElement : citiesJSON) {
			City city = new Gson().fromJson(cityElement, City.class);
			cities.put(city.getLocation(), city);
		}
		JsonArray portsJSON = mapJSON.getAsJsonArray("ports");
		for (JsonElement portElement : portsJSON) {
			Port port = new Gson().fromJson(portElement, Port.class);
			ports.put(port.getLocation(), port);
		}
		radius = mapJSON.getAsJsonObject("radius").getAsInt();
		robber = new Gson().fromJson(mapJSON.getAsJsonObject("robber"), Robber.class);
	}

	public boolean hasCityAtLocation(VertexLocation location) {
		if (cities.get(location) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean hasSettlementAtLocation(VertexLocation location) {
		if (settlements.get(location) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean hasRoadAtLocation(EdgeLocation location) {
		if (roads.get(location) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean hasAdjacentSettlement(VertexLocation location) {
		location = location.getNormalizedLocation();
		if (location.getDir() == VertexDirection.NorthWest) {
			// Create a vertex location for each of the 2 points around the point
			VertexLocation vertexNE = new VertexLocation(location.getHexLoc(),VertexDirection.NorthEast);
			VertexLocation vertexW = new VertexLocation(location.getHexLoc(),VertexDirection.West);
			int newY = location.getHexLoc().getY() - 1;
			int newX = location.getHexLoc().getX() - 1;
			// If a hex exists above it, get the vertex from that hex
			if (newY >= radius) {
				HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(), newY);
				VertexLocation vertexH = new VertexLocation(aboveHex,VertexDirection.West);
				return hasSettlementAtLocation(vertexH.getNormalizedLocation()) || hasSettlementAtLocation(vertexNE.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexW.getNormalizedLocation());
			}
			// Otherwise, if a hex exists to the left of it, get the vertex from that hex
			else if (newX >= radius) {
				HexLocation sideHex = new HexLocation(newX,location.getHexLoc().getY());
				VertexLocation vertexH = new VertexLocation(sideHex,VertexDirection.NorthEast);
				return hasSettlementAtLocation(vertexH.getNormalizedLocation()) || hasSettlementAtLocation(vertexNE.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexW.getNormalizedLocation());
			}
			// Otherwise, there are no hexes above it and only the surrounding vertices on the same hex need to be checked.
			else {
				return hasSettlementAtLocation(vertexNE.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexW.getNormalizedLocation());
			}

		}
		else if (location.getDir() == VertexDirection.NorthEast) {
			// Create a vertex location for each of the 2 points around the point
			VertexLocation vertexNW = new VertexLocation(location.getHexLoc(),VertexDirection.NorthWest);
			VertexLocation vertexE = new VertexLocation(location.getHexLoc(),VertexDirection.East);
			int newY = location.getHexLoc().getY() + 1;
			int newX = location.getHexLoc().getX() + 1;
			// If a hex exists above it, get the vertex from that hex
			if (newY <= radius) {
				HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(), newY);
				VertexLocation vertexH = new VertexLocation(aboveHex,VertexDirection.East);
				return hasSettlementAtLocation(vertexH.getNormalizedLocation()) || hasSettlementAtLocation(vertexNW.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexE.getNormalizedLocation());
			}
			// Otherwise, if a hex exists to the right of it, get the vertex from that hex
			else if (newX <= radius) {
				HexLocation sideHex = new HexLocation(newX,location.getHexLoc().getY());
				VertexLocation vertexH = new VertexLocation(sideHex,VertexDirection.NorthWest);
				return hasSettlementAtLocation(vertexH.getNormalizedLocation()) || hasSettlementAtLocation(vertexNW.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexE.getNormalizedLocation());
			}
			// Otherwise, there are no hexes above it and only the surrounding vertices on the same hex need to be checked.
			else {
				return hasSettlementAtLocation(vertexNW.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexE.getNormalizedLocation());
			}

		}
		return false;
	}

	public boolean vertexIsOnPlayerRoad(VertexLocation location, Player player) {
		location = location.getNormalizedLocation();
		if (location.getDir() == VertexDirection.NorthWest) {
			// Create edges for that share the same hex as the vertex
			EdgeLocation edgeN = new EdgeLocation(location.getHexLoc(),EdgeDirection.North);
			EdgeLocation edgeNW = new EdgeLocation(location.getHexLoc(),EdgeDirection.NorthWest);
			int newY = location.getHexLoc().getY() - 1;
			int newX = location.getHexLoc().getX() - 1;
			// If a hex exists above it, get the edge from that hex
			if (Math.abs(newY) <= radius) {
				HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(), newY);
				EdgeLocation edgeH = new EdgeLocation(aboveHex,EdgeDirection.SouthWest);
				return playerHasRoadAtLocation(edgeH.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeN.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeNW.getNormalizedLocation(),player);
			}
			// Otherwise, if there is a hex to the left of it, get the edge from that hex
			else if (Math.abs(newX) <= radius) {
				HexLocation sideHex = new HexLocation(newX,location.getHexLoc().getY());
				EdgeLocation edgeH = new EdgeLocation(sideHex,EdgeDirection.NorthEast);
				return playerHasRoadAtLocation(edgeH.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeN.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeNW.getNormalizedLocation(),player);
			}
			// Otherwise, there are no hexes above/beside it and only the edges from the same hex need to be checked
			else {
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeNW.getNormalizedLocation(),player);
			}

		}
		else if (location.getDir() == VertexDirection.NorthEast) {
			// Create edges for that share the same hex as the vertex
			EdgeLocation edgeN = new EdgeLocation(location.getHexLoc(),EdgeDirection.North);
			EdgeLocation edgeNE = new EdgeLocation(location.getHexLoc(),EdgeDirection.NorthEast);
			int newY = location.getHexLoc().getY() + 1;
			int newX = location.getHexLoc().getX() + 1;
			// If a hex exists above it, get the edge from that hex
			if (newY <= radius) {
				HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(), newY);
				EdgeLocation edgeH = new EdgeLocation(aboveHex,EdgeDirection.SouthEast);
				return playerHasRoadAtLocation(edgeH.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeN.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(),player);
			}
			// Otherwise, if there is a hex to the right of it, get the edge from that hex
			else if (newX <= radius) {
				HexLocation sideHex = new HexLocation(newX,location.getHexLoc().getY());
				EdgeLocation edgeH = new EdgeLocation(sideHex,EdgeDirection.NorthWest);
				return playerHasRoadAtLocation(edgeH.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeN.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(),player);
			}
			// Otherwise, there are no hexes above/beside it and only the edges from the same hex need to be checked
			else {
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(),player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(),player);
			}

		}
		return false;
	}

	/**
	 * Checks if player owns a road on a given edge location
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean playerHasRoadAtLocation(EdgeLocation location, Player player) {
		if (roads.get(location) != null && roads.get(location).getOwnerIndex() == player.getPlayerIndex()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if player owns a municipality (city or settlement) on a given vertex
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean playerHasMunicipalityAtLocation(VertexLocation location, Player player) {
		if ((cities.get(location) != null && cities.get(location).getOwnerIndex() == player.getPlayerIndex()) ||
			(settlements.get(location) != null && settlements.get(location).getOwnerIndex() == player.getPlayerIndex())) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a given edge has a player's municipality (city or settlement)
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean edgeHasPlayerMunicipality(EdgeLocation location, Player player) {
		ArrayList<VertexLocation> vertices = location.getNormalizedVertices();
		return playerHasMunicipalityAtLocation(vertices.get(0), player) ||
				playerHasMunicipalityAtLocation(vertices.get(1), player);
	}

	/**
	 * Checks if an edge has an adjacent road by the same player
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean edgeHasAdjacentPlayerRoad(EdgeLocation location, Player player) {
		location = location.getNormalizedLocation();
		if (location.getDir() == EdgeDirection.NorthWest) {
			EdgeLocation edgeN = new EdgeLocation(location.getHexLoc(),EdgeDirection.North);
			EdgeLocation edgeSW = new EdgeLocation(location.getHexLoc(),EdgeDirection.SouthWest);
			int newX = location.getHexLoc().getX() - 1;
			int newY = location.getHexLoc().getY() - 1;
			if (Math.abs(newX) <= radius) {
				HexLocation aboveHex = new HexLocation(newX, location.getHexLoc().getY());
				EdgeLocation edgeNE = new EdgeLocation(aboveHex,EdgeDirection.NorthEast);
				EdgeLocation edgeS = new EdgeLocation(aboveHex,EdgeDirection.South);
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeS.getNormalizedLocation(), player);
			}
			else if (Math.abs(newY) <= radius) {
				HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(),newY);
				EdgeLocation edgeHSW = new EdgeLocation(aboveHex,EdgeDirection.SouthWest);
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeHSW.getNormalizedLocation(), player);
			}

			else {
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSW.getNormalizedLocation(), player);
			}
		}
		else if (location.getDir() == EdgeDirection.North) {
			// COMPLETELY DIFFERENT WAY OF DOING THINGS THEN ABOVE.  PROBABLY A LOT BETTER. DARN.

			// Edges on same hex
			EdgeLocation edgeNW = new EdgeLocation(location.getHexLoc(),EdgeDirection.NorthWest);
			EdgeLocation edgeNE = new EdgeLocation(location.getHexLoc(),EdgeDirection.NorthEast);

			// Possible hexes around this hex with adjacent edges
			HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(),location.getHexLoc().getY() - 1);
			HexLocation leftHex = new HexLocation(location.getHexLoc().getX() - 1,location.getHexLoc().getY());
			HexLocation rightHex = new HexLocation(location.getHexLoc().getX() + 1,location.getHexLoc().getY() + 1);
			if (hexes.get(aboveHex) != null) {
				// Create Edges for this hex above to check for player roads
				EdgeLocation edgeSW = new EdgeLocation(aboveHex,EdgeDirection.SouthWest);
				EdgeLocation edgeSE = new EdgeLocation(aboveHex,EdgeDirection.SouthEast);
				return playerHasRoadAtLocation(edgeNW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSE.getNormalizedLocation(), player);

			}
			else if (hexes.get(leftHex) != null) {
				// Create Edges for this hex to the left to check for player roads
				EdgeLocation edgeHNE = new EdgeLocation(aboveHex,EdgeDirection.NorthEast);
				return playerHasRoadAtLocation(edgeNW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeHNE.getNormalizedLocation(), player);
			}
			if (hexes.get(rightHex) != null) {
				// Create Edges for this hex to the right to check for player roads
				EdgeLocation edgeHNW = new EdgeLocation(aboveHex,EdgeDirection.NorthWest);
				return playerHasRoadAtLocation(edgeNW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeHNW.getNormalizedLocation(), player);
			}
			else {
				// No hexes above this hex.  Only need to check it's own sides
				return playerHasRoadAtLocation(edgeNW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeNE.getNormalizedLocation(), player);
			}
		}
		else if (location.getDir() == EdgeDirection.NorthEast) {
			// Edges on same hex
			EdgeLocation edgeN = new EdgeLocation(location.getHexLoc(),EdgeDirection.North);
			EdgeLocation edgeSE = new EdgeLocation(location.getHexLoc(),EdgeDirection.SouthEast);

			// Possible hexes around this hex with adjacent edges
			HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(),location.getHexLoc().getY() - 1);
			HexLocation rightHex = new HexLocation(location.getHexLoc().getX() + 1,location.getHexLoc().getY() + 1);
			if (hexes.get(rightHex) != null) {
				// Create Edges for this hex to the right to check for player roads
				EdgeLocation edgeNW = new EdgeLocation(aboveHex,EdgeDirection.NorthWest);
				EdgeLocation edgeS = new EdgeLocation(aboveHex,EdgeDirection.South);
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSE.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeNW.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeS.getNormalizedLocation(), player);
			}
			else if (hexes.get(aboveHex) != null) {
				// Create Edges for this hex above to check for player roads
				EdgeLocation edgeHSE = new EdgeLocation(aboveHex,EdgeDirection.SouthEast);
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSE.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeHSE.getNormalizedLocation(), player);

			}
			else {
				// No hexes above this hex.  Only need to check it's own sides
				return playerHasRoadAtLocation(edgeN.getNormalizedLocation(), player) ||
						playerHasRoadAtLocation(edgeSE.getNormalizedLocation(), player);
			}
		}
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
	
	public static void main(String[] args) {
		System.out.println("Hello");
		String json = "{\"resource\":\"wheat\",\"location\":{\"x\":-1,\"y\":2}}";
		Hex hex = new Gson().fromJson(json, Hex.class);
		System.out.println(hex.getLocation().getX());
	}



}
