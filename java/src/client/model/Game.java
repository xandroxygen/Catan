package client.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import client.admin.GameAdministrator;
import client.communication.LogEntry;
import client.data.RobPlayerInfo;
import client.server.IServerProxy;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Game class.
 */
public class Game {

    private ArrayList<Player> playerList;
    private client.model.Map theMap;
    private Bank bank;
    private int currentTurnIndex;
    private TurnTracker turnTracker;
    private List<LogEntry> log;
    private List<LogEntry> chat;
    private int winner;
    private int version;
    private TradeOffer tradeOffer;
    private Player currentPlayer;
	private IServerProxy server;

    public boolean isTurn(int playerId){
        //look for implementation
        return getPlayerIndex(playerId) == turnTracker.getCurrentTurn();
    }
    
    public boolean isMyTurn() {
    	return currentPlayer.getPlayerIndex() == turnTracker.getCurrentTurn();
    }

    public int getPlayerIndex(int playerId){
        int i = 0;
        for (Player tempPlayer  : playerList) {
            if(tempPlayer.getPlayerID() == playerId){
                return i;
            }
            i++;
        }
        //throw invalidExceptionError();
        return -1;
    }
    
    public Game() {
    	this.version = -1;
    }

    public Game(ArrayList<Player> players, Map theMap, Bank bank, JsonObject modelJSON, IServerProxy server) {

    	// Initialize players, map and bank
    	playerList = players;
    	this.bank = bank;
    	this.theMap = theMap;
		this.server = server;
    	
    	// Init the current player
    	for (Player player : players) {
    		if (player.getPlayerID() == GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId()) {
    			this.currentPlayer = player;
    			server.setPlayerIndex(player.getPlayerIndex());
    		}
    	}

    	// Create Turn Tracker
    	turnTracker = new Gson().fromJson(modelJSON.getAsJsonObject("turnTracker"), TurnTracker.class);
    	currentTurnIndex = turnTracker.getCurrentTurn();

    	// Create Log
    	log = new ArrayList<>();
    	JsonObject logJSON = modelJSON.getAsJsonObject("log");
    	JsonArray logLines = logJSON.getAsJsonArray("lines");
    	for (JsonElement line : logLines) {
    		CatanColor color = getPlayerColorByName(line.getAsJsonObject().get("source").getAsString());
    		log.add(new LogEntry(color,line.getAsJsonObject().get("message").getAsString()));
    	}

    	// Create Chat
    	chat = new ArrayList<>();
    	JsonObject chatJSON = modelJSON.getAsJsonObject("chat");
    	JsonArray chatLines = chatJSON.getAsJsonArray("lines");
    	for (JsonElement line : chatLines) {
    		CatanColor color = getPlayerColorByName(line.getAsJsonObject().get("source").getAsString());
    		chat.add(new LogEntry(color,line.getAsJsonObject().get("message").getAsString()));
    	}

    	// Initialize remaining variables
    	winner = modelJSON.get("winner").getAsInt();
		version = modelJSON.get("version").getAsInt();

    }

	public IServerProxy getServer() {
		return server;
	}

	/**
     * checks to see if the game can create a new user
     *
     * @pre <pre>
     *      There are less than 4 players
     *      The game has not started
     * 	</pre>
     *
     * @post <pre>
     *      returns true to add a new player
     * </pre>
     *
     * @return
     */
    public boolean canCreatePlayer(){
        return playerList.size() < 4;
    }

    /**
     * Authenticates the user
     *
     * @pre <pre>
     *      The player must be an authenticated user
     * 	</pre>
     *
     * @post <pre>
     *      returns true to authenticate the user.
     * </pre>
     *
     * @return
     */
    public boolean canAuthenticateUser(){
        return false;
    }
    
    /**
     * Checks whether the player can place a city.
     * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city), The city is on the map at the specified location, You got a settlement back
     * @param playerId the ID of the player who is requesting the move
     * @param location The location of the city.
     * @return result
     */
    boolean canPlaceCity(int playerId, VertexLocation location){
    	Player player = this.getPlayerById(playerId);
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
				theMap.playerHasSettlementAtLocation(location, player) && (player.getResources().get(ResourceType.WHEAT) >= 2) &&
				(player.getResources().get(ResourceType.ORE) >= 3) && (player.getCities() >= 1));
    }

    /**
     * Checks whether the player can place a settlement.
     * @pre <pre>
     * 		It's your turn
     * 		The settlement location is open
     * 		The settlement location is not on water
     * 		The settlement location is connected to one of your roads except during setup
     * 		You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement)
     * 		The settlement cannot be placed adjacent to another settlement
     * 		</pre>
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param playerId the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     * @return result
     */
    boolean canPlaceSettlement(int playerId, boolean free, VertexLocation location){
    	Player player = this.getPlayerById(playerId);
    	if (free) {
    		return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
    				!theMap.hasSettlementAtLocation(location) && !theMap.hasAdjacentSettlement(location) && 
    				theMap.vertexIsOnPlayerRoad(location, player));
    	}
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && !theMap.hasAdjacentSettlement(location) &&
				!theMap.hasSettlementAtLocation(location) && theMap.vertexIsOnPlayerRoad(location, player) && 
				(player.getResources().get(ResourceType.WOOD) >= 1) && (player.getResources().get(ResourceType.BRICK) >= 1) && 
				(player.getResources().get(ResourceType.WHEAT) >= 1) && (player.getResources().get(ResourceType.SHEEP) >= 1) && 
				(player.getSettlements() >= 1));
    }

    /**
     * Checks whether the player can place a road.
     * @pre <pre>
     * 		It's your turn
     * 		The road location is open
     * 		The road location is connected to another road owned by the player
     * 		The road location is not on water
     * 		You have the required resources (1 wood, 1 brick; 1 road)
     * 		Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * 		</pre>
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param playerId the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     * @return result
     */
    boolean canPlaceRoad(int playerId, boolean free, EdgeLocation location) {
    	Player player = this.getPlayerById(playerId);
    	if (free) {
    		return (turnTracker.getCurrentTurn() == player.getPlayerIndex() &&
    				!theMap.hasRoadAtLocation(location) && 
    				!theMap.edgeHasPlayerMunicipality(location, playerList.get(0)) &&
    				!theMap.edgeHasPlayerMunicipality(location, playerList.get(1)) && 
    				!theMap.edgeHasPlayerMunicipality(location, playerList.get(2)) &&
    				!theMap.edgeHasPlayerMunicipality(location, playerList.get(3)) &&
    				!theMap.edgeHasAdjacentPlayerRoad(location, player) &&
    				!theMap.edgeIsOnWater(location));
    	}
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) &&
    			!theMap.edgeIsOnWater(location) &&
				!theMap.hasRoadAtLocation(location) &&
				(theMap.edgeHasPlayerMunicipality(location, player) || theMap.edgeHasAdjacentPlayerRoad(location, player)) &&
				(player.getResources().get(ResourceType.WOOD) >= 1) && 
				(player.getResources().get(ResourceType.BRICK) >= 1) && (player.getRoads() >= 1));
    }

	boolean canPlaceRoad(int playerId, EdgeLocation firstLocation, EdgeLocation location) {
		Player player = this.getPlayerById(playerId);
		if (firstLocation == null) {
			return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) &&
					!theMap.edgeIsOnWater(location) &&
					!theMap.hasRoadAtLocation(location) &&
					(theMap.edgeHasPlayerMunicipality(location, player) || theMap.edgeHasAdjacentPlayerRoad(location, player)) &&
					(player.getRoads() >= 1));
		}
		else{
			return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) &&
					!theMap.edgeIsOnWater(location) &&
					!theMap.hasRoadAtLocation(location) && firstLocation != location &&
					(theMap.edgeHasPlayerMunicipality(location, player) || theMap.edgeHasAdjacentPlayerRoad(location, player)) &&
					(player.getRoads() >= 1));
		}
	}

    /**
     * Checks whether the player can trade with another player
     * @pre It's your turn, You have the resources you are offering.
     * @post The trade is offered to the other player (stored in the server model)
     * @param offer list of Resources, Negative numbers mean you get those cards
     * @param recieverPlayerId the playerIndex of the offer recipient.
     * @return result
     */
    boolean canTradeWithPlayer(int senderPlayerId, int recieverPlayerId, HashMap<ResourceType, Integer> offer){
    	Player player = this.getPlayerById(senderPlayerId);
    	return ((turnTracker.getCurrentTurn() == player.getPlayerIndex()) && 
				player.hasOfferResources(offer));
    }
    
    boolean canRollDice(int playerId){
    	Player player = this.getPlayerById(playerId);
    	return turnTracker.getCurrentTurn() == player.getPlayerIndex();
    }
    
    public void  placeRoad(boolean isFree, EdgeLocation roadLocation) {
    	try {
			server.buildRoad(isFree, roadLocation);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
    }

    public void placeSettlement(boolean isFree, VertexLocation vertexLocation) {
    	try {
			server.buildSettlement(isFree, vertexLocation);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
    }

	public void placeCity(VertexLocation vertexLocation) {
		try {
			server.buildCity(vertexLocation);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
    
    
    /**
     * Sends a message from the logged in user.
     * @post  The chat contains your message at the end.
     * @param message the message the player wishes to send.
     * @return
     */
    void sendMessage(String message){
    	try {
			server.sendChat(message);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
    }

    /**
     * Checks whether the player can end the turn.
     * @post  The cards in your new dev card hand have been transferred to your old dev card hand, It is the next player’s turn
     * @return result
     */
    boolean endTurn(int playerId){
        return false; //ME
    }
    
    /**
     * Checks whether the player can get rolled resources.
     * @param diceRoll the number that was rolled
     * @return true if there are resources to receiver
     */
    boolean canGetRolledResources(int diceRoll){
        return diceRoll <= 12 && diceRoll >= 2;
    }
    
    // MARK: HELPER METHODS
    private Player getPlayerById(int playerId) {
    	for (Player player : playerList) {
    		if (player.getPlayerID() == playerId) {
    			return player;
    		}
    	}
    	return null;
    }
    
    private CatanColor getPlayerColorByName(String name) {
    	for (Player player : playerList) {
    		if (player.getName().equals(name)) {
    			return player.getColor();
    		}
    	}
    	return null;
    }
    
    // GETTERS
    public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public client.model.Map getTheMap() {
		return theMap;
	}

	public Bank getBank() {
		return bank;
	}

	public int getCurrentTurnIndex() {
		return currentTurnIndex;
	}

	public TurnTracker getTurnTracker() {
		return turnTracker;
	}

	public int getWinner() {
		return winner;
	}

	public int getVersion() {
		return version;
	}

	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}
	
	public List<LogEntry> getChat() {
		return chat;
	}
	
	public List<LogEntry> getLog() {
		return log;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setVersion(int i) {
		this.version = i;
		
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return !hexLoc.equals(theMap.getRobber().getLocation()) &&
				(theMap.getHexes().get(hexLoc) != null);
	}

	public RobPlayerInfo[] getCandidateVictims(HexLocation hexLoc) {
		List<RobPlayerInfo> infoList = new ArrayList<>();
		int[] victimIndicies = theMap.getPlayersWithMunicipalityOn(hexLoc);
		
		// Create array of Robbing Info
		for (int i = 0; i < victimIndicies.length; i++) {
			if (victimIndicies[i] == 1) {
				infoList.add(new RobPlayerInfo(playerList.get(i)));
			}
		}
		
		if (infoList.size() != 0) {
			RobPlayerInfo[] result = new RobPlayerInfo[infoList.size()];
			infoList.toArray(result);
			return result;
		}
		else {
			return null;
		}
	}
    
}
