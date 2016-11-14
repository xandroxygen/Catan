package server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Bank;
import shared.model.Game;
import shared.model.Hex;
import shared.model.Map;
import shared.model.Player;
import shared.model.Port;
import shared.model.TurnTracker;

/**
 * ServerModelFacade
 */
public class ServerGame extends Game {
	
	private String gameName;
	private int gameId;

    public ServerGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String gameName, int id) {
    	
    	// Create a new Map
    	this.setMap(this.createMap(randomTiles,randomNumbers,randomPorts));
    	
    	// Set name and id
    	this.gameName = gameName;
    	this.gameId = id;
    	
    	// Create a Bank
    	this.setBank(new Bank());
    	
    	// Create a Turn Tracker
    	this.setTurnTracker(new TurnTracker());
    	
    	// Set player list to initially be empty
    	this.initPlayerList();
    	
    	// Set winner and version
    	// TODO: Should the version start at 0 or 1?
    	this.setWinner(-1);
    	this.setVersion(0);
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameId() {
		return gameId;
	}

	/**
     * Places a City in the Game for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore, 1 city), The city is on the map at the specified location, You got a settlement back on the desired location
     * @param playerID the ID of the player who is requesting the move.
     * @param location The location of the city.
     */
    public void placeCity(int playerID, VertexLocation location) {
    	int index = getPlayerIndex(playerID);
    	// Add to the Map
    	getTheMap().addCity(index,location);
    	// Adjust the player and bank resources
    	getBank().purchaseCity(getPlayerList().get(index));
    	// Adjust player piece inventory
    	getPlayerList().get(index).addToPlayerPieces(PieceType.CITY, -1);
    	getPlayerList().get(index).addToPlayerPieces(PieceType.SETTLEMENT, 1);
    }

    /**
     * Places a Settlement in the Game for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param playerID the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     */
    public void placeSettlement(int playerID, boolean free, VertexLocation location) {
    	int index = getPlayerIndex(playerID);
    	// Add to the Map
    	getTheMap().addSettlement(playerID,location);
    	if (!free) {
	    	// Adjust the player and bank resources
	    	getBank().purchaseRoad(getPlayerList().get(index));
	    	// Adjust player piece inventory
	    	getPlayerList().get(index).addToPlayerPieces(PieceType.SETTLEMENT, -1);
    	}
    	if (getTheMap().getSettlements().size() == 4 || getTheMap().getSettlements().size() == 8) {
    		getTurnTracker().nextStatus();
    	}
    }

    /**
     * Places a Road in the Game for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param playerID the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     */
    public void placeRoad(int playerID, boolean free, EdgeLocation location) {
    	int index = getPlayerIndex(playerID);
    	// Add to the Map
    	getTheMap().addRoad(playerID,location);
    	if (!free) {
	    	// Adjust the player and bank resources
	    	getBank().purchaseRoad(getPlayerList().get(index));
	    	// Adjust player piece inventory
	    	getPlayerList().get(index).addToPlayerPieces(PieceType.ROAD, -1);
    	}
    }

    /**
     * Buys a development card if the given player.
     * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
     * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devCard hand (unplayable this turn)
     * @param playerID the ID of the player who is requesting the move
     */
    public void buyDevelopmentCard(int playerID){
        int totalNumOfDevCards = 0;
        for(java.util.Map.Entry<DevCardType, Integer> tempDevCard : getBank().getDevelopmentCards().entrySet()) {
            totalNumOfDevCards += tempDevCard.getValue();
        }
        // TODO: 11/7/2016 finish method

    }

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
     */
    public void playSoldierCard(int playerID, HexLocation location, int victimIndex){
        getPlayerList().get(playerID).setPlayedDevCard(true);
        //add a soldier to player
        getPlayerList().get(playerID).addSoldier();
        //move the robber
        getTheMap().getRobber().setLocation(location);
        //rob the victim and add it to the player who played the card
        robPlayer(playerID, victimIndex);
        largestArmy();
    }

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
     */
    public void playYearOfPleanty(int playerID, ResourceType resource1, ResourceType resource2){
        getPlayerList().get(playerID).setPlayedDevCard(true);
        if(getBank().getResourceDeck().get(resource1) > 0){
            getBank().getResourceDeck().put(resource1, getBank().getResourceDeck().get(resource1) - 1);
            getPlayerList().get(playerID).getResources().put(resource1,
                    getPlayerList().get(playerID).getResources().get(resource1) + 1);
        }
        if(getBank().getResourceDeck().get(resource2) > 0){
            getBank().getResourceDeck().put(resource2, getBank().getResourceDeck().get(resource2) - 1);
            getPlayerList().get(playerID).getResources().put(resource2,
                    getPlayerList().get(playerID).getResources().get(resource2) + 1);
        }
    }

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
     */
    public void playRoadCard(int playerID, EdgeLocation spot1, EdgeLocation spot2){
        getPlayerList().get(playerID).setPlayedDevCard(true);
        placeRoad(playerID, true, spot1);
        placeRoad(playerID, true, spot2);
    }

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
     */
    public void playMonopolyCard(int playerID, ResourceType resource){
        getPlayerList().get(playerID).setPlayedDevCard(true);
        int totalCountOfResource = 0;
        for (Player tempPlayer : getPlayerList()) {
            totalCountOfResource += tempPlayer.getResources().get(resource);
            tempPlayer.getResources().put(resource, 0);
        }
        getPlayerList().get(playerID).getResources().put(resource, totalCountOfResource);
    }

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
     */
    public void playMonumentCard(int playerID){
        getPlayerList().get(playerID).setVictoryPoints(getPlayerList().get(playerID).getVictoryPoints() + 1);
    }

    /**
     * Checks whether the player can roll the dice
     * @pre It's their turn, and the model status is ROLLING
     * @post distributes the correct amount of resources corresponding to the roll for every player
     * @param playerID the ID of the player who is requesting the move
     * @param rollValue the value that was rolled
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
    public void acceptTradeOffer(boolean willAccept){}

    /**
     * Accept the TradeOffer currently on the table.
     * @param playerID the ID of the player who is requesting the move
     */
    public void makeMaritimeTrade(int playerID, int ratio, ResourceType inputResource, ResourceType outputResource) {
    	int index = getPlayerIndex(playerID);
    	// Adjust the player and bank resources
    	getBank().addToResourceDeck(inputResource, ratio);
    	// Adjust player piece inventory
    	getPlayerList().get(index).addToResourceHand(outputResource, 1);
    	getPlayerList().get(index).addToResourceHand(inputResource, -ratio);
    }

    /**
     * Adds a player to the game.
     * @param playerID the ID of the player who is requesting the move
     */
    public void addPlayer(int playerID, String username, CatanColor color) {
    	if (getPlayerIndex(playerID) != -1) {
    		this.addPlayer(new Player(playerID,username,color,getPlayerList().size()));
    	}
    }

    /**
     * Adds an computer player to the game.
     * * * @pre <pre>
     * 		There are less then four players in the current game
     * 		</pre>
     * * @post <pre>
     *      there is a new computer player added to the game
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
    public void finishTurn(){
        // TODO: 11/7/2016 be sure to include resetting played dev card to false
    }

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

    /**
     * Lists out all types of AI Players for a particular game
     * * @post <pre>
     *      If there are AI players it will return an array of their corresponding types
     * 		</pre>
     */
    public String[] listAIPlayers(){
        return null;
    }

    /**
	 * Calculates and sets the player with the longest road.
	 * @pre <pre>
	 * 		There are four players in the current game
	 * 		There is at least one road place
	 * 		</pre>
	 * @post <pre>
	 *      The player with the longest road is determined, and set within the model.
	 * 		</pre>
	 */
	public void longestRoad() {}

	/**
	 * Calculates and sets the player with the largest army.
	 * @pre <pre>
	 * 		There are four players in the current game
	 * 		</pre>
	 * * @post <pre>
	 *      The player with the largest army is determined, and set within the model.
	 * 		</pre>
	 */
	public void largestArmy() {}
	
	private Map createMap(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		ArrayList<Integer> numbers = new ArrayList<Integer>(
    		    Arrays.asList(0,4,11,8,3,9,12,5,10,11,5,6,2,9,4,10,6,3,8));
    	// BRICK, WOOL, ORE, GRAIN, WOOD
    	ArrayList<HexType> resources = new ArrayList<HexType>(
    		    Arrays.asList(null,HexType.BRICK,HexType.WOOD,HexType.BRICK,HexType.WOOD,
    		    		HexType.ORE,HexType.SHEEP,HexType.ORE,HexType.SHEEP,
    		    		HexType.WHEAT,HexType.BRICK,HexType.WHEAT,HexType.WHEAT,
    		    		HexType.SHEEP,HexType.WOOD,HexType.SHEEP,HexType.WOOD,
    		    		HexType.ORE,HexType.WHEAT));
    	
    	ArrayList<HexLocation> locations = new ArrayList<HexLocation>(
    		    Arrays.asList(new HexLocation(0,-2),new HexLocation(1,-2),new HexLocation(2,-2),new HexLocation(-1,-1),new HexLocation(0,-1),
    		    		new HexLocation(1,-1),new HexLocation(2,-1),new HexLocation(-2,0),new HexLocation(-1,0),new HexLocation(0,0),
    		    		new HexLocation(1,0),new HexLocation(2,0),new HexLocation(-2,1),new HexLocation(-1,1),new HexLocation(0,1),
    		    		new HexLocation(1,1),new HexLocation(-2,2),new HexLocation(-1,2),new HexLocation(0,2)));
    	
    	ArrayList<EdgeLocation> portLocations = new ArrayList<EdgeLocation>(
    			Arrays.asList(new EdgeLocation(new HexLocation(-2,3),EdgeDirection.NorthEast),
    					new EdgeLocation(new HexLocation(-3,0),EdgeDirection.SouthEast),
    					new EdgeLocation(new HexLocation(-3,2),EdgeDirection.NorthEast),
    					new EdgeLocation(new HexLocation(3,-1),EdgeDirection.NorthWest),
    					new EdgeLocation(new HexLocation(2,1),EdgeDirection.NorthWest),
    					new EdgeLocation(new HexLocation(3,-3),EdgeDirection.SouthWest),
    					new EdgeLocation(new HexLocation(1,-3),EdgeDirection.South),
    					new EdgeLocation(new HexLocation(-1,-2),EdgeDirection.South),
    					new EdgeLocation(new HexLocation(0,3),EdgeDirection.North)));
    	
    	ArrayList<Port> ports = new ArrayList<Port>(
    			Arrays.asList(new Port(2,ResourceType.BRICK),new Port(3,null),new Port(2,ResourceType.WOOD),
    					new Port(2,ResourceType.SHEEP),new Port(3,null),new Port(3,null),
    					new Port(2,ResourceType.ORE),new Port(2,ResourceType.WHEAT),new Port(3,null)));
    			

		java.util.Map<HexLocation,Hex> hexes = new HashMap<>();
		while (!locations.isEmpty()) {
			Hex hex = null;
			HexType type = resources.get(0);
			HexLocation location = locations.get(0);
			int number = numbers.get(0);
			
			// If random tiles, tke from list then remove it
			if (randomTiles) {
				int locIndex = ThreadLocalRandom.current().nextInt(0, locations.size() + 1);
				location = locations.get(locIndex);
			}
			
			// If random numbers, get number from a list then remove it
			if (randomNumbers) {
				int numIndex = ThreadLocalRandom.current().nextInt(0, numbers.size() + 1);
				number = numbers.get(numIndex);
				hex = new Hex(type,location,number);
				numbers.remove(Integer.valueOf(number));
			}
			
			// Else create hexes with default values
			else {
				hex = new Hex(type,location,number);
				numbers.remove(Integer.valueOf(number));
			}
			hexes.put(hex.getLocation(),hex);
			resources.remove(type);
			locations.remove(location);
    	}
    	
		java.util.Map<EdgeLocation,Port> finalPorts = new HashMap<>();
    	for (Port port: ports) {
    		EdgeLocation loc = portLocations.get(0);
    		if (randomPorts) {
    			int index = ThreadLocalRandom.current().nextInt(0, portLocations.size() + 1);
    			loc = portLocations.get(index);
    			port.setLocation(loc);
    		}
    		else {
    			port.setLocation(loc);
    		}
    		finalPorts.put(loc.getNormalizedLocation(), port);
    		portLocations.remove(loc);
    	}
    	
    	return new Map(hexes,finalPorts);
	}
	
}
