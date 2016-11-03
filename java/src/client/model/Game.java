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

    	// Create Trade offer, if any
		if (modelJSON.has("tradeOffer")) {
			tradeOffer = new TradeOffer(modelJSON.getAsJsonObject("tradeOffer"));
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
				!theMap.hasSettlementAtLocation(location) && !theMap.hasCityAtLocation(location) && theMap.vertexIsOnPlayerRoad(location, player) && 
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
//    				!theMap.edgeHasAdjacentPlayerRoad(location, player) &&
    				(theMap.futureCanPlaceSettlement(location.getNormalizedVertices().get(0)) ||
    				theMap.futureCanPlaceSettlement(location.getNormalizedVertices().get(1))) &&
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
					(theMap.edgeHasPlayerMunicipality(location, player) || theMap.edgeHasAdjacentPlayerRoad(location, player) ||
					theMap.roadsAreNextToEachOther(firstLocation, location)) &&
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
			if (victimIndicies[i] == 1 && playerList.get(i).hasResources()) {
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



	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Server Only Methods!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	/**
	 * Places a City in the Game from the given gameID for the player specified in the given playerID, at the given location.
	 * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
	 * @post You lost the resources required to build a city (2 wheat, 3 ore, 1 city), The city is on the map at the specified location, You got a settlement back on the desired location
	 * @param playerID the ID of the player who is requesting the move.
	 * @param location The location of the city.
	 * @return result
	 */
	public void placeCity(int playerID, VertexLocation location){}

	/**
	 * Places a Settlement in the Game from the given gameID for the player specified in the given playerID, at the given location.
	 * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
	 * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
	 * @param playerID the ID of the player who is requesting the move
	 * @param free Whether or not piece can be built for free.
	 * @param location The location of the settlement.
	 * @return result
	 */
	public void placeSettlement(int playerID, boolean free, VertexLocation location){}

	/**
	 * Places a Road in the Game from the given gameID for the player specified in the given playerID, at the given location.
	 * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
	 * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
	 * @param playerID the ID of the player who is requesting the move
	 * @param free Whether or not piece can be built for free.
	 * @param location The location of the road.
	 * @return result
	 */
	public void placeRoad(int playerID, boolean free, EdgeLocation location){}

	/**
	 * Buys a development card if the given player.
	 * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
	 * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devCard hand (unplayable this turn)
	 * @param playerID the ID of the player who is requesting the move
	 * @return result
	 */
	public void buyDevelopmentCard(int playerID){}

	/**
	 * Places a soldier development card and moves the robber into the given location and robs the victim player if there is one
	 * @pre <pre>
	 * 		It's your turn
	 * 		The client model status is 'Playing'
	 * 		You have the specific card you want to play in your old dev card hand
	 * 		You have not yet played a non­monument dev card this turn
	 * 		The robber is not being kept in the same location
	 * 		If a player is being robbed (i.e., victimIndex != ­1)
	 * 		The player being robbed has resource cards
	 * 		</pre>
	 * @post <pre>
	 * 		The robber is in the new location
	 * 		The player being robbed (if any) gave you one of his resource cards (randomly selected)
	 * 		If applicable, largest army has been awarded to the player who has played the most soldier cards
	 * 		You are not allowed to play other development cards during this turn (except for monument cards, which may still be played)
	 * 	    Can not play another DevCard with the exception of the Monument Card
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 * @param location the new robber location.
	 * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
	 * @return result
	 */
	public void playSoldierCard(int playerID, HexLocation location, int victimIndex){}

	/**
	 * Plays a year of plenty devCard.
	 * @pre <pre>
	 * 		It's your turn
	 * 		The client model status is 'Playing'
	 * 		You have the specific card you want to play in your old dev card hand
	 * 		You have not yet played a non­monument dev card this turn
	 * 		Two specified resources are in the bank.
	 * 		</pre>
	 * @post <pre>
	 * 		You gained the two specified resources
	 * 	    Can not play another DevCard with the exception of the Monument Card
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 * @param resource1 The type of the first resource you'd like to receive
	 * @param resource2 The type of the second resource you'd like to receive
	 * @return result
	 */
	public void playYearOfPleanty(int playerID, ResourceType resource1, ResourceType resource2){}

	/**
	 * Plays a road card.
	 * @pre <pre>
	 * 		It's your turn
	 * 		The client model status is 'Playing'
	 * 		You have the specific card you want to play in your old dev card hand
	 * 		You have not yet played a non­monument dev card this turn
	 * 		The first road location (spot1) is connected to one of your roads.
	 * 		The second road location (spot2) is connected to one of your roads or to the first road location (spot1)
	 * 		Neither road location is on water
	 * 		You have at least two unused roads
	 * 		</pre>
	 * @post <pre>
	 * 		You have two fewer unused roads
	 * 		Two new roads appear on the map at the specified locations
	 * 		If applicable, longest road has been awarded to the player with the longest road
	 * 	    Can not play another DevCard with the exception of the Monument Card
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 * @param spot1 first edge location of road.
	 * @param spot2 second edge location of road.
	 * @return result
	 */
	public void playRoadCard(int playerID, EdgeLocation spot1, EdgeLocation spot2){}

	/**
	 * Plays a monopoly devCard.
	 * @pre <pre>
	 * 		It's your turn
	 * 		The client model status is 'Playing'
	 * 		You have the specific card you want to play in your old dev card hand
	 * 		You have not yet played a non­monument dev card this turn
	 * 		</pre>
	 * @post <pre>
	 * 		All of the other players have given you all of their resource cards of the specified type
	 * 	    Can not play another DevCard with the exception of the Monument Card
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 * @param resource The type of resource desired from other players.
	 * @return result
	 */
	public void playMonopolyCard(int playerID, ResourceType resource){}

	/**
	 * Plays a monument devCard.
	 * @pre <pre>
	 * 		It's your turn
	 * 		The client model status is 'Playing'
	 * 		You have the specific card you want to play in your old dev card hand
	 * 		You have not yet played a non­monument dev card this turn
	 * 		You have enough monument cards to win the game (i.e., reach 10 victory points)
	 * 		</pre>
	 * @post <pre>
	 * 		You gained a victory point
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 * @return result
	 */
	public void playMonumentCard(int playerID){}

	/**
	 * Checks whether the player can roll the dice
	 * @pre It's their turn, and the model status is ROLLING
	 * @post distributes the correct amount of resources corresponding to the roll for every player
	 * @param playerID the ID of the player who is requesting the move
	 * @param rollValue the value that was rolled
	 * @return
	 */
	public void rollDice(int playerID,  int rollValue){}

	/**
	 * Sends a chat message.
	 * @post  The chat contains your message at the end.
	 * @param playerID the ID of the player who is requesting the move
	 * @param message the message the player wishes to send.
	 */
	public void sendMessage(int playerID, String message){}

	/**
	 * Make a trade offer to another player.
	 * @param senderPlayerID Player offering the trade
	 * @param receiverPlayerID Player being offered the trade
	 * @param offer hand of cards to trade
	 */
	public void makeTradeOffer(int senderPlayerID, int receiverPlayerID, HashMap<ResourceType, Integer> offer){}

	/**
	 * Accept the TradeOffer currently on the table.
	 */
	public void acceptTradeOffer(){}

	/**
	 * Accept the TradeOffer currently on the table.
	 * @param playerID the ID of the player who is requesting the move
	 */
	public void makeMaritimeTrade(int playerID){}

	/**
	 * Adds a player to the game.
	 * @param playerID the ID of the player who is requesting the move
	 */
	public void addPlayer(int playerID){}

	/**
	 * Adds an computer player to the game.
	 * * * @pre <pre>
	 * 		There are less then four players in the current game
	 * 		</pre>
	 * * @post <pre>
	 *      there is a new computer player added to the game given by the gameID
	 * 		</pre>
	 */
	public void addComputerPlayer(){
		//This method needs to check to see if there are four players before execution
	}

	/**
	 * Ends the current players turn
	 * * * @pre <pre>
	 *     is called by the player whose turn it is
	 *     the player is in the playing state
	 * 		</pre>
	 * * @post <pre>
	 *     the current players turn is over and the next players is starting the game
	 * 		</pre>
	 */
	public void finishTurn(){}

	/**
	 * Robs a player
	 * * * @pre <pre>
	 *
	 * 		</pre>
	 * * @post <pre>
	 *      Robs 1 resource from the victim player
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 * @param victimIndex .
	 */
	public void robPlayer(int playerID, int victimIndex){}

	/**
	 * Discards the given resources from the given player
	 * * * @pre <pre>
	 *      The player has sufficient resources to be able to discard
	 * 		</pre>
	 * * @post <pre>
	 *      the given resources are discarded
	 * 		</pre>
	 * @param playerID the ID of the player who is requesting the move
	 */
	public void discardCards(int playerID, HashMap<ResourceType, Integer> discardCards){

	}

	//TODO I am not quite sure what listAIPlayers() should do I just added it because there is a server call associated with it
	/**
	 * Lists out all AI Players for a particular game
	 * * * @pre <pre>
	 *
	 * 		</pre>
	 * * @post <pre>
	 *
	 * 		</pre>
	 */
	public void listAIPlayers(){}

	/**
	 * Gives the longestRoad Card to the appropriate player.
	 * * * @pre <pre>
	 * 		There are less then four players in the current game
	 * 		</pre>
	 * * @post <pre>
	 *      there is a new computer player added to the game given by the gameID
	 * 		</pre>
	 */
	public void longestRoad(){}

	/**
	 * Gives the longestRoad Card to the appropriate player.
	 * * * @pre <pre>
	 * 		There are less then four players in the current game
	 * 		</pre>
	 * * @post <pre>
	 *      there is a new computer player added to the game given by the gameID
	 * 		</pre>
	 */
	public void largestArmy(){}

}
