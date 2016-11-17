package shared.model;

import client.model.Model;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import shared.definitions.HexType;
import shared.definitions.ResourceType;
import shared.locations.*;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Robber robber;

	public HashMap<HexLocation, Hex> getHexes() {
		return hexes;
	}

	public int getRadius() {
		return radius;
	}

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

	public HashMap<EdgeLocation, Port> getPorts() {
		return ports;
	}

	/**
     * Constructor for creating Map with JsonObject
     * @param mapJSON
     */
	public Map(JsonObject mapJSON) {
		try {
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
				Road road = new Road(roadElement.getAsJsonObject());
				roads.put(road.getLocation().getNormalizedLocation(), road);
			}
			JsonArray settlementsJSON = mapJSON.getAsJsonArray("settlements");
			for (JsonElement settlementElement : settlementsJSON) {
				Settlement settlement = new Settlement(settlementElement.getAsJsonObject());
				settlements.put(settlement.getLocation().getNormalizedLocation(), settlement);
			}
			JsonArray citiesJSON = mapJSON.getAsJsonArray("cities");
			for (JsonElement cityElement : citiesJSON) {
				City city = new City(cityElement.getAsJsonObject());
				cities.put(city.getLocation().getNormalizedLocation(), city);
			}
			JsonArray portsJSON = mapJSON.getAsJsonArray("ports");
			for (JsonElement portElement : portsJSON) {
				Port port = new Port(portElement.getAsJsonObject());
				ports.put(port.getLocation(), port);
			}
			JsonObject robberJSON = mapJSON.getAsJsonObject("robber");
			robber = new Robber(robberJSON.get("x").getAsInt(),robberJSON.get("y").getAsInt());
			radius = mapJSON.get("radius").getAsInt();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Construct a new Map from scratch
	public Map(java.util.Map<HexLocation, Hex> hexes, java.util.Map<EdgeLocation, Port> finalPorts) {
		this.hexes = (HashMap<HexLocation, Hex>) hexes;
		this.ports = (HashMap<EdgeLocation, Port>) finalPorts;
		cities = new HashMap<>();
		settlements = new HashMap<>();
		roads = new HashMap<>();
		radius = 3;
	}

	public boolean hasCityAtLocation(VertexLocation location) {
		return cities.get(location) != null;
	}

	public boolean hasSettlementAtLocation(VertexLocation location) {
		return settlements.get(location) != null;
	}

	public boolean hasRoadAtLocation(EdgeLocation location) {
		return roads.get(location) != null;
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
			if (Math.abs(newY) <= Math.abs(radius)) {
				HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(), newY);
				VertexLocation vertexH = new VertexLocation(aboveHex,VertexDirection.West);
				return hasSettlementAtLocation(vertexH.getNormalizedLocation()) || hasSettlementAtLocation(vertexNE.getNormalizedLocation()) ||
						hasSettlementAtLocation(vertexW.getNormalizedLocation());
			}
			// Otherwise, if a hex exists to the left of it, get the vertex from that hex
			else if (Math.abs(newX) <= Math.abs(radius)) {
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
			int newY = location.getHexLoc().getY() - 1;
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
			int newY = location.getHexLoc().getY() - 1;
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
		return roads.get(location) != null && roads.get(location).getOwnerIndex() == player.getPlayerIndex();
	}

	/**
	 * Checks if player owns a municipality (city or settlement) on a given vertex
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean playerHasMunicipalityAtLocation(VertexLocation location, Player player) {
		return playerHasSettlementAtLocation(location, player) || 
				(cities.get(location) != null && cities.get(location).getOwnerIndex() == player.getPlayerIndex());
	}
	
	/**
	 * Checks if player owns a Settlement on a given vertex
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean playerHasSettlementAtLocation(VertexLocation location, Player player) {
		return (settlements.get(location) != null && settlements.get(location).getOwnerIndex() == player.getPlayerIndex());
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
			HexLocation rightHex = new HexLocation(location.getHexLoc().getX() + 1,location.getHexLoc().getY() - 1);
			if (hexes.get(rightHex) != null) {
				// Create Edges for this hex to the right to check for player roads
				EdgeLocation edgeNW = new EdgeLocation(rightHex,EdgeDirection.NorthWest);
				EdgeLocation edgeS = new EdgeLocation(rightHex,EdgeDirection.South);
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
	 * checks if two roads are next to each other
	 * @return
	 */
	public boolean roadsAreNextToEachOther(EdgeLocation firstLocation, EdgeLocation location) {
		firstLocation = firstLocation.getNormalizedLocation();
		location = location.getNormalizedLocation();
		if(firstLocation == location){
			return false;
		}
		if (firstLocation.getDir() == EdgeDirection.NorthWest) {
			EdgeLocation edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setDir(EdgeDirection.North);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setHexLoc(new HexLocation(firstLocation.getHexLoc().getX() - 1, firstLocation.getHexLoc().getY()));
			edgeLocation.setDir(EdgeDirection.NorthEast);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setHexLoc(new HexLocation(firstLocation.getHexLoc().getX() - 1, firstLocation.getHexLoc().getY() + 1));
			edgeLocation.setDir(EdgeDirection.North);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation.setDir(EdgeDirection.NorthEast);
			if(location.equals(edgeLocation)){
				return true;
			}
		}
		else if (firstLocation.getDir() == EdgeDirection.North) {
			EdgeLocation edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setDir(EdgeDirection.NorthEast);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation.setDir(EdgeDirection.NorthWest);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setHexLoc(new HexLocation(firstLocation.getHexLoc().getX() - 1, firstLocation.getHexLoc().getY()));
			edgeLocation.setDir(EdgeDirection.NorthEast);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setHexLoc(new HexLocation(firstLocation.getHexLoc().getX() + 1, firstLocation.getHexLoc().getY() + 1));
			edgeLocation.setDir(EdgeDirection.NorthWest);
			if(location.equals(edgeLocation)){
				return true;
			}
		}
		else if (firstLocation.getDir() == EdgeDirection.NorthEast) {
			EdgeLocation edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setDir(EdgeDirection.North);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setHexLoc(new HexLocation(firstLocation.getHexLoc().getX() + 1, firstLocation.getHexLoc().getY()));
			edgeLocation.setDir(EdgeDirection.NorthWest);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation.setDir(EdgeDirection.North);
			if(location.equals(edgeLocation)){
				return true;
			}
			edgeLocation = new EdgeLocation(firstLocation);
			edgeLocation.setHexLoc(new HexLocation(firstLocation.getHexLoc().getX() + 1, firstLocation.getHexLoc().getY() - 1));
			edgeLocation.setDir(EdgeDirection.NorthWest);
			if(location.equals(edgeLocation)){
				return true;
			}
		}
		return false;
	}
	
	public boolean edgeIsOnWater(EdgeLocation edge) {
		EdgeLocation location = edge.getNormalizedLocation();
		HexLocation hex1 = location.getHexLoc();
		HexLocation hex2 = new HexLocation(location.getHexLoc().getNeighborLoc(location.getDir()).getX(),
				location.getHexLoc().getNeighborLoc(location.getDir()).getY());
		return !hexes.containsKey(hex1) && !hexes.containsKey(hex2);
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

	public int[] getPlayersWithMunicipalityOn(HexLocation hexLoc) {
		// Check add the owner of each municipality if it does not already exist in the array
		int[] indicies = new int[4];
		
		// Create vertices and check in city and settlement arrays
		VertexLocation vertexW = new VertexLocation(hexLoc,VertexDirection.West);
		
		// If municipality exists, set the array at the player index to 1;
		if (cities.get(vertexW.getNormalizedLocation()) != null) {
			indicies[cities.get(vertexW.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		else if (settlements.get(vertexW.getNormalizedLocation()) != null) {
			indicies[settlements.get(vertexW.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		
		VertexLocation vertexE = new VertexLocation(hexLoc,VertexDirection.East);
		if (cities.get(vertexE.getNormalizedLocation()) != null) {
			indicies[cities.get(vertexE.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		else if (settlements.get(vertexE.getNormalizedLocation()) != null) {
			indicies[settlements.get(vertexE.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		
		VertexLocation vertexNE = new VertexLocation(hexLoc,VertexDirection.NorthEast);
		if (cities.get(vertexNE.getNormalizedLocation()) != null) {
			indicies[cities.get(vertexNE.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		else if (settlements.get(vertexNE.getNormalizedLocation()) != null) {
			indicies[settlements.get(vertexNE.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		
		VertexLocation vertexNW = new VertexLocation(hexLoc,VertexDirection.NorthWest);
		if (cities.get(vertexNW.getNormalizedLocation()) != null) {
			indicies[cities.get(vertexNW.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		else if (settlements.get(vertexNW.getNormalizedLocation()) != null) {
			indicies[settlements.get(vertexNW.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		
		VertexLocation vertexSE = new VertexLocation(hexLoc,VertexDirection.SouthEast);
		if (cities.get(vertexSE.getNormalizedLocation()) != null) {
			indicies[cities.get(vertexSE.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		else if (settlements.get(vertexSE.getNormalizedLocation()) != null) {
			indicies[settlements.get(vertexSE.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		
		VertexLocation vertexSW = new VertexLocation(hexLoc,VertexDirection.SouthWest);
		if (cities.get(vertexSW.getNormalizedLocation()) != null) {
			indicies[cities.get(vertexSW.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		else if (settlements.get(vertexSW.getNormalizedLocation()) != null) {
			indicies[settlements.get(vertexSW.getNormalizedLocation()).getOwnerIndex()] = 1;
		}
		
		// ensure that current player can't rob himself
		indicies[Model.getInstance().getCurrentPlayer().getPlayerIndex()] = 0;
		return indicies;
		
	}
	
	public boolean futureCanPlaceSettlement(VertexLocation location){
    	return !hasSettlementAtLocation(location) && !hasAdjacentSettlement(location);
    }

	public boolean roadIsNextToSettlement(){
//		edgeHasPlayerMunicipality()
		return false;
	}
	
	
	// SETTER METHODS
	
	public void addRoad(int playerIndex, EdgeLocation location) {
		Road road = new Road(location,playerIndex);
		roads.put(road.getLocation().getNormalizedLocation(), road);
	}
	
	public void addCity(int playerIndex, VertexLocation location) {
		City city = new City(location,playerIndex);
		cities.put(city.getLocation().getNormalizedLocation(), city);
	}
	
	public void addSettlement(int playerIndex, VertexLocation location) {
		Settlement settlement = new Settlement(location,playerIndex);
		settlements.put(settlement.getLocation().getNormalizedLocation(), settlement);
	}

	public void setRobber(Robber robber2) {
		this.robber = robber2;
	}

	public void rewardPlayerAtHex(Player p, int hexNumber) {
		for (HexLocation key : hexes.keySet()) {
			if (hexes.get(key).getNumber() == hexNumber && hexes.get(key).getResource() != null) {
				Hex hex = hexes.get(key);
				
				// Create vertices for each point around the hex
				ArrayList<VertexLocation> vertices = new ArrayList<VertexLocation>(
		    		    Arrays.asList(new VertexLocation(hex.getLocation(), VertexDirection.East),
		    					new VertexLocation(hex.getLocation(), VertexDirection.NorthEast),
		    					new VertexLocation(hex.getLocation(), VertexDirection.NorthWest),
		    					new VertexLocation(hex.getLocation(), VertexDirection.SouthEast),
		    					new VertexLocation(hex.getLocation(), VertexDirection.SouthWest),
		    					new VertexLocation(hex.getLocation(), VertexDirection.West)));
				
				for (VertexLocation vertex : vertices) {
					// Add 1 resource for settlement
					if (settlements.get(vertex.getNormalizedLocation()) != null && settlements.get(vertex.getNormalizedLocation()).getOwnerIndex() == p.getPlayerIndex()) {
						p.addToResourceHand(ResourceType.valueOf(hex.getResource().toString()), 1);
					}
					// Add 2 resource for city
					if (cities.get(vertex.getNormalizedLocation()) != null && cities.get(vertex.getNormalizedLocation()).getOwnerIndex() == p.getPlayerIndex()) {
						p.addToResourceHand(ResourceType.valueOf(hex.getResource().toString()), 2);
					}
				}
				
				
			}
		}
		
	}

	public void rewardPlayerAtSecondRound(Player player, VertexLocation location) {
		VertexLocation loc = location.getNormalizedLocation();
		if (loc.getDir() == VertexDirection.NorthEast) {

			HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(),location.getHexLoc().getY() - 1);
			HexLocation rightHex = new HexLocation(location.getHexLoc().getX() + 1,location.getHexLoc().getY() - 1);
			if (hexes.get(aboveHex) != null && hexes.get(aboveHex).getResource() != null) {
				player.addToResourceHand(ResourceType.valueOf(hexes.get(aboveHex).getResource().toString()), 1);

			}
			if (hexes.get(rightHex) != null && hexes.get(rightHex).getResource() != null) {
				player.addToResourceHand(ResourceType.valueOf(hexes.get(rightHex).getResource().toString()), 1);
			}
		}
		
		else if (loc.getDir() == VertexDirection.NorthWest) {
			
			HexLocation aboveHex = new HexLocation(location.getHexLoc().getX(),location.getHexLoc().getY() - 1);
			HexLocation leftHex = new HexLocation(location.getHexLoc().getX() - 1,location.getHexLoc().getY());
			if (hexes.get(aboveHex) != null && hexes.get(aboveHex).getResource() != null) {
				player.addToResourceHand(ResourceType.valueOf(hexes.get(aboveHex).getResource().toString()), 1);

			}
			if (hexes.get(leftHex) != null && hexes.get(leftHex).getResource() != null) {
				player.addToResourceHand(ResourceType.valueOf(hexes.get(leftHex).getResource().toString()), 1);
			}
		}
		
		if (hexes.get(loc.getHexLoc()) != null) {
			player.addToResourceHand(ResourceType.valueOf(hexes.get(loc.getHexLoc()).getResource().toString()), 1);
		}
	}
}
