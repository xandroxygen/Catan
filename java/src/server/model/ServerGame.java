package server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import client.communication.LogEntry;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.*;

import static java.lang.Boolean.TRUE;

import java.io.Serializable;

/**
 * ServerModelFacade
 */
public class ServerGame extends Game implements Serializable {
	
	private String gameName;
	private int gameId;
	private List<String> availableAIs;
	private List<CatanColor> availableColors;
	private List<EdgeLocation> availableRoadLocations;

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
    	
    	// Set Robber
    	this.getTheMap().setRobber(new Robber());
    	
    	// Initialize Logs
    	this.initLogs();
    	
    	// Set winner and version
    	this.setWinner(-1);
    	this.setVersion(0);
    	
    	// Set List of available AIs with colors and locations
    	availableAIs = new ArrayList<String>(
    		    Arrays.asList("Will","Mike","Eleven"));
    	availableColors = new ArrayList<CatanColor>(
    			Arrays.asList(CatanColor.BLUE,CatanColor.BROWN,CatanColor.GREEN,CatanColor.ORANGE,CatanColor.PUCE,
    					CatanColor.PURPLE,CatanColor.RED,CatanColor.WHITE,CatanColor.YELLOW));
    	availableRoadLocations = new ArrayList<EdgeLocation>(
    			Arrays.asList(new EdgeLocation(new HexLocation(0,0),EdgeDirection.North),
    					new EdgeLocation(new HexLocation(2,2),EdgeDirection.South),
    					new EdgeLocation(new HexLocation(0,-2),EdgeDirection.North),
    					new EdgeLocation(new HexLocation(2,0),EdgeDirection.South),
    					new EdgeLocation(new HexLocation(0,2),EdgeDirection.South),
    					new EdgeLocation(new HexLocation(2,-2),EdgeDirection.North),
    					new EdgeLocation(new HexLocation(-2,2),EdgeDirection.South),
    					new EdgeLocation(new HexLocation(1,0),EdgeDirection.North),
    					new EdgeLocation(new HexLocation(-1,0),EdgeDirection.South)));
    	
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
     * @param playerIndex the ID of the player who is requesting the move.
     * @param location The location of the city.
     */
    public void placeCity(int playerIndex, VertexLocation location) {
    	
    	// Add to the Map
    	getTheMap().addCity(playerIndex,location);
    	
    	// Adjust the player and bank resources
    	getBank().purchaseCity(getPlayerList().get(playerIndex));
    	
    	// Adjust player piece inventory
    	getPlayerList().get(playerIndex).addToPlayerPieces(PieceType.CITY, -1);
    	getPlayerList().get(playerIndex).addToPlayerPieces(PieceType.SETTLEMENT, 1);
    	
    	getPlayerList().get(playerIndex).setVictoryPoints(getPlayerList().get(playerIndex).getVictoryPoints() + 1);
    	String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " built a city",name));
		setVersion(getVersion() + 1);
    }

    /**
     * Places a Settlement in the Game for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param playerIndex the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     */
    public void placeSettlement(int playerIndex, boolean free, VertexLocation location) {
    	// Add to the Map
    	getTheMap().addSettlement(playerIndex,location);
    	if (!free) {
	    	// Adjust the player and bank resources
	    	getBank().purchaseRoad(getPlayerList().get(playerIndex));
    	}
    	if (getTurnTracker().getStatus() == GameStatus.SecondRound) {
    		getTheMap().rewardPlayerAtSecondRound(getPlayerList().get(playerIndex), location);
    	}
		// Adjust player piece inventory
		getPlayerList().get(playerIndex).addToPlayerPieces(PieceType.SETTLEMENT, -1);
		// Adjust victory points
    	getPlayerList().get(playerIndex).setVictoryPoints(getPlayerList().get(playerIndex).getVictoryPoints() + 1);
    	
    	String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " built a settlement",name));
		setVersion(getVersion() + 1);
	}

    /**
     * Places a Road in the Game for the player specified in the given playerID, at the given location.
     * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param playerIndex the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     */
    public void placeRoad(int playerIndex, boolean free, EdgeLocation location) {
    	// Add to the Map
    	getTheMap().addRoad(playerIndex,location);
    	if (!free) {
	    	// Adjust the player and bank resources
	    	getBank().purchaseRoad(getPlayerList().get(playerIndex));
	    	// Adjust player piece inventory
	    	getPlayerList().get(playerIndex).addToPlayerPieces(PieceType.ROAD, -1);
            this.longestRoad();
        }
    	
    	String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " built a road",name));
		setVersion(getVersion() + 1);
	}

    /**
     * Buys a development card if the given player.
     * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
     * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devCard hand (unplayable this turn)
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void buyDevelopmentCard(int playerIndex){
        Player current_player = getPlayerList().get(playerIndex);
        current_player.getResources().put(ResourceType.ORE, current_player.getResources().get(ResourceType.ORE) - 1);
        current_player.getResources().put(ResourceType.WHEAT, current_player.getResources().get(ResourceType.WHEAT) - 1);
        current_player.getResources().put(ResourceType.SHEEP, current_player.getResources().get(ResourceType.SHEEP) - 1);
        
        //THIS PART CALCULATES THE PROBABILITY THAT YOU WILL PICK UP EACH CARD
        //THE REASON THAT THE NUMBER OF THE PREVIOUS CARD IS ADDED IS SO WHEN GENERATING A RANDOM NUMBER IT ALL WORKS OUT
        int numOfSoldierCards, numOfYearOfPlentyCards, numOfMonopolyCards, numOfRoadBuildCards, numOfMonumentCards;
        numOfSoldierCards = getBank().getDevelopmentCards().get(DevCardType.SOLDIER);
        numOfYearOfPlentyCards = getBank().getDevelopmentCards().get(DevCardType.YEAR_OF_PLENTY) + numOfSoldierCards;
        numOfMonopolyCards = getBank().getDevelopmentCards().get(DevCardType.MONOPOLY) + numOfYearOfPlentyCards;
        numOfRoadBuildCards = getBank().getDevelopmentCards().get(DevCardType.ROAD_BUILD) + numOfMonopolyCards;
        numOfMonumentCards = getBank().getDevelopmentCards().get(DevCardType.MONUMENT) + numOfRoadBuildCards;

        if(numOfMonumentCards == 0){
            return;
        }

        int randomNumber = (int )(Math. random() * numOfMonumentCards + 1);

        if(randomNumber < numOfSoldierCards){
            getBank().getDevelopmentCards().put(DevCardType.SOLDIER,
                    getBank().getDevelopmentCards().get(DevCardType.SOLDIER) - 1);
            current_player.getNewDevCards().put(DevCardType.SOLDIER,
                    current_player.getNewDevCards().get(DevCardType.SOLDIER) + 1);
        }
        else if(randomNumber < numOfYearOfPlentyCards){
            getBank().getDevelopmentCards().put(DevCardType.YEAR_OF_PLENTY,
                    getBank().getDevelopmentCards().get(DevCardType.YEAR_OF_PLENTY) - 1);
            current_player.getNewDevCards().put(DevCardType.YEAR_OF_PLENTY,
                    current_player.getNewDevCards().get(DevCardType.YEAR_OF_PLENTY) + 1);
        }
        else if(randomNumber < numOfMonopolyCards){
            getBank().getDevelopmentCards().put(DevCardType.MONOPOLY,
                    getBank().getDevelopmentCards().get(DevCardType.MONOPOLY) - 1);
            current_player.getNewDevCards().put(DevCardType.MONOPOLY,
                    current_player.getNewDevCards().get(DevCardType.MONOPOLY) + 1);
        }
        else if(randomNumber < numOfRoadBuildCards){
            getBank().getDevelopmentCards().put(DevCardType.ROAD_BUILD,
                    getBank().getDevelopmentCards().get(DevCardType.ROAD_BUILD) - 1);
            current_player.getNewDevCards().put(DevCardType.ROAD_BUILD,
                    current_player.getNewDevCards().get(DevCardType.ROAD_BUILD) + 1);
        }
        else if(randomNumber < numOfMonumentCards){
            getBank().getDevelopmentCards().put(DevCardType.MONUMENT,
                    getBank().getDevelopmentCards().get(DevCardType.MONUMENT) - 1);
            current_player.getOldDevCards().put(DevCardType.MONUMENT,
                    current_player.getOldDevCards().get(DevCardType.MONUMENT) + 1);
        }
		setVersion(getVersion() + 1);
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
     * @param playerIndex the ID of the player who is requesting the move
     * @param location the new robber location.
     * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
     */
    public void playSoldierCard(int playerIndex, HexLocation location, int victimIndex){
    	getPlayerList().get(playerIndex).getOldDevCards().put(DevCardType.SOLDIER, getPlayerList().get(playerIndex).getOldDevCards().get(DevCardType.SOLDIER) - 1);
    	getPlayerList().get(playerIndex).setPlayedDevCard(true);
        //add a soldier to player
        getPlayerList().get(playerIndex).addSoldier();
        //rob the victim and add it to the player who played the card
        robPlayer(playerIndex, victimIndex, location);
        largestArmy();
        
        String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " played a soldier card",name));
		setVersion(getVersion() + 1);
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
     * @param playerIndex the ID of the player who is requesting the move
     * @param resource1 The type of the first resource you'd like to receive
     * @param resource2 The type of the second resource you'd like to receive
     */
    public void playYearOfPlenty(int playerIndex, ResourceType resource1, ResourceType resource2){
    	getPlayerList().get(playerIndex).getOldDevCards().put(DevCardType.YEAR_OF_PLENTY, getPlayerList().get(playerIndex).getOldDevCards().get(DevCardType.YEAR_OF_PLENTY) - 1);
    	getPlayerList().get(playerIndex).setPlayedDevCard(true);
        if(getBank().getResourceDeck().get(resource1) > 0){
            getBank().getResourceDeck().put(resource1, getBank().getResourceDeck().get(resource1) - 1);
            getPlayerList().get(playerIndex).getResources().put(resource1,
                    getPlayerList().get(playerIndex).getResources().get(resource1) + 1);
        }
        if(getBank().getResourceDeck().get(resource2) > 0){
            getBank().getResourceDeck().put(resource2, getBank().getResourceDeck().get(resource2) - 1);
            getPlayerList().get(playerIndex).getResources().put(resource2,
                    getPlayerList().get(playerIndex).getResources().get(resource2) + 1);
        }
        
        String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " played a year of plenty card",name));
		setVersion(getVersion() + 1);
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
     * @param playerIndex the ID of the player who is requesting the move
     * @param spot1 first edge location of road.
     * @param spot2 second edge location of road.
     */
    public void playRoadCard(int playerIndex, EdgeLocation spot1, EdgeLocation spot2){
    	getPlayerList().get(playerIndex).getOldDevCards().put(DevCardType.ROAD_BUILD, getPlayerList().get(playerIndex).getOldDevCards().get(DevCardType.ROAD_BUILD) - 1);
        getPlayerList().get(playerIndex).setPlayedDevCard(true);
        placeRoad(playerIndex, true, spot1);
        placeRoad(playerIndex, true, spot2);
        
        String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " played a road card",name));
		setVersion(getVersion() + 1);
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
     * @param playerIndex the ID of the player who is requesting the move
     * @param resource The type of resource desired from other players.
     */
    public void playMonopolyCard(int playerIndex, ResourceType resource){
    	getPlayerList().get(playerIndex).getOldDevCards().put(DevCardType.MONOPOLY, getPlayerList().get(playerIndex).getOldDevCards().get(DevCardType.MONOPOLY) - 1);
        getPlayerList().get(playerIndex).setPlayedDevCard(true);
        int totalCountOfResource = 0;
        for (Player tempPlayer : getPlayerList()) {
            totalCountOfResource += tempPlayer.getResources().get(resource);
            tempPlayer.getResources().put(resource, 0);
        }
        getPlayerList().get(playerIndex).getResources().put(resource, totalCountOfResource);
        String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " played a monoply card",name));
		setVersion(getVersion() + 1);
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
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void playMonumentCard(int playerIndex){
    	getPlayerList().get(playerIndex).getOldDevCards().put(DevCardType.MONUMENT, getPlayerList().get(playerIndex).getOldDevCards().get(DevCardType.MONUMENT) - 1);
        getPlayerList().get(playerIndex).setVictoryPoints(getPlayerList().get(playerIndex).getVictoryPoints() + 1);
        
        String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " played a monument card",name));
		setVersion(getVersion() + 1);
	}

    /**
     * Checks whether the player can roll the dice
     * @pre It's their turn, and the model status is ROLLING
     * @post distributes the correct amount of resources corresponding to the roll for every player
     * @param playerIndex the ID of the player who is requesting the move
     * @param rollValue the value that was rolled
	 */
    public void rollDice(int playerIndex,  int rollValue) {
    	// Award resources to players
    	for (Player p : getPlayerList()) {
    		getTheMap().rewardPlayerAtHex(p,rollValue, getBank());
    	}
    	
    	// If any player has more than 7 resource cards, change game status to discarding
    	if (rollValue == 7 && (getPlayerList().get(0).getTotalOfResources() > 7 ||
    			getPlayerList().get(1).getTotalOfResources() > 7 ||
    			getPlayerList().get(2).getTotalOfResources() > 7 ||
    			getPlayerList().get(3).getTotalOfResources() > 7)) {
    		getTurnTracker().setStatus(GameStatus.Discarding);
    		for (Player p: getPlayerList()) {
    			if (p.getTotalOfResources() > 7 && p.getPlayerID() < 0) {
    	    		this.discardCards(p.getPlayerIndex(), p.getDiscardingResources());
    			}
    		}
    	}
    	else if (rollValue == 7) {
    		getTurnTracker().setStatus(GameStatus.Robbing);
    	}
    	
    	// Else set to playing
    	else {
    		getTurnTracker().setStatus(GameStatus.Playing);
    	}
    	
    	String name = getPlayerList().get(playerIndex).getName();
    	getLog().add(new LogEntry(name+ " rolled a " + rollValue ,name));
        setVersion(getVersion() + 1);
    }

    /**
     * Sends a chat message.
     * @post  The chat contains your message at the end.
     * @param playerIndex the ID of the player who is requesting the move
     * @param message the message the player wishes to send.
     */
    public void sendMessage(int playerIndex, String message) {
    	String name = getPlayerList().get(playerIndex).getName();
    	getChat().add(new LogEntry(message,name));
    	if (message.equals("t")) {
			getPlayerList().get(playerIndex).addToResourceHand(ResourceType.BRICK, 2);
			getPlayerList().get(playerIndex).addToResourceHand(ResourceType.ORE, 2);
			getPlayerList().get(playerIndex).addToResourceHand(ResourceType.SHEEP, 2);
			getPlayerList().get(playerIndex).addToResourceHand(ResourceType.WHEAT, 2);
			getPlayerList().get(playerIndex).addToResourceHand(ResourceType.WOOD, 2);
		}
        setVersion(getVersion() + 1);
    }

    /**
     * Make a trade offer to another player.
     * @param senderPlayerIndex Player offering the trade
     * @param receiverPlayerIndex Player being offered the trade
     * @param offer hand of cards to trade
     */
    public void makeTradeOffer(int senderPlayerIndex, int receiverPlayerIndex, Map<ResourceType, Integer> offer){
		setTradeOffer(new TradeOffer(senderPlayerIndex, receiverPlayerIndex, offer));
		
		// If trading with an AI, reject >=]
		if (getPlayerList().get(receiverPlayerIndex).getPlayerID() < 0) {
			this.acceptTradeOffer(false);
		}
		
        setVersion(getVersion() + 1);
	}

    /**
     * Accept the TradeOffer currently on the table.
	 * Offer is positive for sending, negative for receiving.
	 * Even though that doesn't make sense, that's the spec?
     */
    public void acceptTradeOffer(boolean willAccept){
        if(willAccept){
            int senderID = getTradeOffer().getSender();
            int receiverID =  getTradeOffer().getReceiver();
            Map<ResourceType, Integer> offer = getTradeOffer().getOffer();
            for (Map.Entry<ResourceType, Integer> playerResource : getPlayerList().get(getPlayerIndex(senderID)).getResources().entrySet()) {
                playerResource.setValue(playerResource.getValue() - offer.get(playerResource.getKey()));
            }
            for (Map.Entry<ResourceType, Integer> playerResource : getPlayerList().get(getPlayerIndex(receiverID)).getResources().entrySet()) {
                playerResource.setValue(playerResource.getValue() + offer.get(playerResource.getKey()));
            }
            String name = getPlayerList().get(senderID).getName();
        	getLog().add(new LogEntry("The trade was accepted!",name));
        }
        else {
        	int senderID = getTradeOffer().getSender();
        	String name = getPlayerList().get(senderID).getName();
        	getLog().add(new LogEntry("The trade was rejected",name));
        }
        setTradeOffer(null);
        setVersion(getVersion() + 1);
    }

    /**
     * Accept the TradeOffer currently on the table.
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void makeMaritimeTrade(int playerIndex, int ratio, ResourceType inputResource, ResourceType outputResource) {
    	// Adjust the player and bank resources
    	getBank().addToResourceDeck(inputResource, ratio);
    	// Adjust player piece inventory
    	getPlayerList().get(playerIndex).addToResourceHand(outputResource, 1);
    	getPlayerList().get(playerIndex).addToResourceHand(inputResource, -ratio);
        setVersion(getVersion() + 1);
    }

    /**
     * Adds a player to the game.
     * @param playerID the ID of the player who is requesting the move
     */
    public void addPlayer(int playerID, String username, CatanColor color) {
    	if (getPlayerIndex(playerID) == -1) {
    		this.addPlayer(new Player(playerID,username,color,getPlayerList().size()));
    		availableColors.remove(color);
    	}
    	else{
            availableColors.add(getPlayerList().get(getPlayerIndex(playerID)).getColor());
    		getPlayerList().get(getPlayerIndex(playerID)).setColor(color);
            availableColors.remove(color);
        }
        setVersion(getVersion() + 1);
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
        setVersion(getVersion() + 1);
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
    	String name = getPlayerList().get(getTurnTracker().getCurrentTurn()).getName();
    	getLog().add(new LogEntry(name+ "'s turn ended",name));
    	
    	getTurnTracker().nextTurn();
    	
    	// If AI player exists, perform turn
    	while (getPlayerList().get(getTurnTracker().getCurrentTurn()).getPlayerID() < 0) {
        	performAITurn(getTurnTracker().getCurrentTurn());
        }
    	
    	for (Player p : getPlayerList()) {
    		p.setPlayedDevCard(false);
    	}
    	//move the newDevCards to oldDevCards
        for (Player tempPlayer : getPlayerList()) {
            tempPlayer.getOldDevCards().put(DevCardType.SOLDIER, tempPlayer.getOldDevCards().get(DevCardType.SOLDIER) +
                    tempPlayer.getNewDevCards().get(DevCardType.SOLDIER));
            tempPlayer.getOldDevCards().put(DevCardType.YEAR_OF_PLENTY, tempPlayer.getOldDevCards().get(DevCardType.YEAR_OF_PLENTY) +
                    tempPlayer.getNewDevCards().get(DevCardType.YEAR_OF_PLENTY));
            tempPlayer.getOldDevCards().put(DevCardType.MONOPOLY, tempPlayer.getOldDevCards().get(DevCardType.MONOPOLY) +
                    tempPlayer.getNewDevCards().get(DevCardType.MONOPOLY));
            tempPlayer.getOldDevCards().put(DevCardType.ROAD_BUILD, tempPlayer.getOldDevCards().get(DevCardType.ROAD_BUILD) +
                    tempPlayer.getNewDevCards().get(DevCardType.ROAD_BUILD));
            
            tempPlayer.getNewDevCards().put(DevCardType.SOLDIER, 0);
            tempPlayer.getNewDevCards().put(DevCardType.YEAR_OF_PLENTY, 0);
            tempPlayer.getNewDevCards().put(DevCardType.MONOPOLY, 0);
            tempPlayer.getNewDevCards().put(DevCardType.ROAD_BUILD, 0);
        }
        
        setVersion(getVersion() + 1);
    }
    
    private void performAITurn(int playerIndex) {
    	int id = getPlayerList().get(playerIndex).getPlayerID();
    	if (getTurnTracker().getStatus() == GameStatus.FirstRound || getTurnTracker().getStatus() == GameStatus.SecondRound) {
    		// Place Road and settlement in random location
    		EdgeLocation e = null;
    		VertexLocation v = null;
    		for (EdgeLocation edge : availableRoadLocations) {
    			if (canPlaceRoad(id, true, edge)) {
    				e = edge;
    				this.placeRoad(playerIndex, true, edge);
    				break;
    			}
    		}
    		for (VertexLocation vertex : e.getNormalizedVertices()) {
    			if (canPlaceSettlement(id, true, vertex)) {
    				v = vertex;
    				this.placeSettlement(playerIndex, true, vertex);
    				break;
    			}
    		}
    		availableRoadLocations.remove(e);
    		getTurnTracker().nextTurn();
    	}
    	if (getTurnTracker().getStatus() == GameStatus.Rolling && (getTurnTracker().getCurrentTurn() == playerIndex)) {
    		int dice = ThreadLocalRandom.current().nextInt(1, 7) + ThreadLocalRandom.current().nextInt(1, 7);
    		//int dice = 7;
    		this.rollDice(getTurnTracker().getCurrentTurn(), dice);
    	}
    	if (getTurnTracker().getStatus() == GameStatus.Discarding) {
    		Player p = getPlayerList().get(playerIndex);
    		this.discardCards(playerIndex, p.getDiscardingResources());
    	}
    	if (getTurnTracker().getStatus() == GameStatus.Robbing && (getTurnTracker().getCurrentTurn() == playerIndex)) {
    		HexLocation hex = new HexLocation(0,0);
    		if (getTheMap().getRobber().getLocation().equals(hex)) {
    			hex = new HexLocation(0,-2);
    		}
    		int[] indices = getTheMap().getPlayersWithMunicipalityOn(hex);
    		indices[playerIndex] = 0;
    		int victim = -1;
			for (int i = 0; i < indices.length; i++) {
				if (indices[i] != 0 && getPlayerList().get(indices[i]).getTotalOfResources() > 0) {
					victim = indices[i];
				}
			}
			this.robPlayer(playerIndex, victim, hex);
    	}
    	if (getTurnTracker().getStatus() == GameStatus.Playing && (getTurnTracker().getCurrentTurn() == playerIndex)) {
    		// Purchase Dev card if sufficient resources exist
    		if (canBuyDevelopmentCard(getTurnTracker().getCurrentTurn())) {
        		buyDevelopmentCard(getTurnTracker().getCurrentTurn());
        	}
        	getTurnTracker().nextTurn();
    	}
    	setVersion(getVersion() + 1);
    }

    /**
     * Robs a player
     * * * @pre <pre>
     *
     * 		</pre>
     * * @post <pre>
     *      Robs 1 resource from the victim player
     * 		</pre>
     * @param playerIndex the ID of the player who is requesting the move
     * @param victimIndex .
     */
    public void robPlayer(int playerIndex, int victimIndex, HexLocation location){

		Player current_player = getPlayerList().get(playerIndex);

		if (victimIndex > -1) { // this is true when user clicks NONE
			Player victim_player = getPlayerList().get(victimIndex);
			if (victim_player.getResources().size() > 0) {
				Boolean hasRobbedPlayer = false;
				while (!hasRobbedPlayer) {
					int randomNumber = (int) (Math.random() * 4 + 0);
					if(victim_player.getResources().get(ResourceType.BRICK) == 0 &&
							victim_player.getResources().get(ResourceType.WOOD) == 0 &&
							victim_player.getResources().get(ResourceType.WHEAT) == 0 &&
							victim_player.getResources().get(ResourceType.SHEEP) == 0 &&
							victim_player.getResources().get(ResourceType.ORE) == 0){
						return;
					}
					else if (randomNumber == 0
							&& victim_player.getResources().get(ResourceType.BRICK) > 0) {
						giveUpAResource(current_player, victim_player, ResourceType.BRICK);
						hasRobbedPlayer = TRUE;
					} else if (randomNumber == 1
							&& victim_player.getResources().get(ResourceType.WOOD) > 0) {
						giveUpAResource(current_player, victim_player, ResourceType.WOOD);
						hasRobbedPlayer = TRUE;
					} else if (randomNumber == 2
							&& victim_player.getResources().get(ResourceType.WHEAT) > 0) {
						giveUpAResource(current_player, victim_player, ResourceType.WHEAT);
						hasRobbedPlayer = TRUE;
					} else if (randomNumber == 3
							&& victim_player.getResources().get(ResourceType.SHEEP) > 0) {
						giveUpAResource(current_player, victim_player, ResourceType.SHEEP);
						hasRobbedPlayer = TRUE;
					} else if (randomNumber == 4
							&& victim_player.getResources().get(ResourceType.ORE) > 0) {

						giveUpAResource(current_player, victim_player, ResourceType.ORE);
						hasRobbedPlayer = TRUE;
					}
				}
			}
			String name = getPlayerList().get(playerIndex).getName();
			String victim = getPlayerList().get(victimIndex).getName();
	    	getLog().add(new LogEntry(name+ " moved the robber and robbed " + victim,name));
    	}
		else {
			String name = getPlayerList().get(playerIndex).getName();
	    	getLog().add(new LogEntry(name+ " moved the robber but couldn't rob anyone!",name));
		}
		if (location != null) {
			getTheMap().getRobber().setLocation(location);
		}
		getTurnTracker().setStatus(GameStatus.Playing);
        setVersion(getVersion() + 1);
	}

	private void giveUpAResource(Player current_player, Player victim_player, ResourceType resourceType){
		victim_player.getResources().put(resourceType,
				victim_player.getResources().get(resourceType) - 1);
		current_player.getResources().put(resourceType,
				current_player.getResources().get(resourceType) + 1);
        setVersion(getVersion() + 1);
	}

    /**
     * Discards the given resources from the given player
     * * * @pre <pre>
     *      The player has sufficient resources to be able to discard
     * 		</pre>
     * * @post <pre>
     *      the given resources are discarded
     * 		</pre>
     * @param playerIndex the ID of the player who is requesting the move
     */
    public void discardCards(int playerIndex, Map<ResourceType, Integer> discardCards){
        getPlayerList().get(playerIndex).setDiscarded(true);
		discardResource(playerIndex, discardCards, ResourceType.BRICK);
		discardResource(playerIndex, discardCards, ResourceType.WOOD);
		discardResource(playerIndex, discardCards, ResourceType.SHEEP);
		discardResource(playerIndex, discardCards, ResourceType.ORE);
		discardResource(playerIndex, discardCards, ResourceType.WHEAT);
        for (Player tempPlayer : getPlayerList()) {
            if(!(tempPlayer.isDiscarded() || tempPlayer.getTotalOfResources() <= 7)){
                return;
            }
        }

        for (Player tempPlayer : getPlayerList()) {
            tempPlayer.setDiscarded(false);
        }
        getTurnTracker().setStatus(GameStatus.Robbing);
        setVersion(getVersion() + 1);
    }

	private void discardResource(int playerIndex, Map<ResourceType, Integer> discardCards, ResourceType resourceType){
		Player current_player = getPlayerList().get(playerIndex);
		current_player.getResources().put(resourceType,
				current_player.getResources().get(resourceType) - discardCards.get(resourceType));
	}

    /**
     * Lists out all types of AI Players for a particular game
     * * @post <pre>
     *      If there are AI players it will return an array of their corresponding types
     * 		</pre>
     */
    public String[] listAIPlayers(){
		String[] types = { "LONGEST_ARMY" };
		return types;
    }
    
    /**
     * Adds an AI player to the game
     */
    public void addAIPlayer() {
    	if (getPlayerList().size() < 4) {
    		String player = availableAIs.get(0);
    		CatanColor color = availableColors.get(0);
    		this.addPlayer((-1)*(getPlayerList().size()+1), player, color);
    		
    		// Remove from lists
    		availableAIs.remove(0);
    		availableColors.remove(0);
    	}
    	setVersion(getVersion() + 1);
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
	private void longestRoad() {
		int[] playerRoads = new int[4];
		// Populate array with number of roads built by each player
		for (EdgeLocation key : getTheMap().getRoads().keySet()) {
			playerRoads[getTheMap().getRoads().get(key).getOwnerIndex()]++;
		}
		// Get the current largest amount of roads
		int max;
		int index = -1;
		if (getTurnTracker().getLongestRoad() != -1) {
			max = playerRoads[getTurnTracker().getLongestRoad()];
			index = getTurnTracker().getLongestRoad();
		}
		else {
			max = 4;
		}

		// If greater than current max, replace
		for (int i = 0; i < playerRoads.length; i++) {
			if (playerRoads[i] > max) {
				if (index != -1) {
					// Adjust victory points from previous user
					getPlayerList().get(index).setVictoryPoints(getPlayerList().get(index).getVictoryPoints()-2);
				}
				max = playerRoads[i];
				index = i;
				// Add victory points to new user
				getPlayerList().get(index).setVictoryPoints(getPlayerList().get(index).getVictoryPoints()+2);
			}
		}
		getTurnTracker().setLongestRoad(index);
	}

	/**
	 * Calculates and sets the player with the largest army.
	 * @pre <pre>
	 * 		There are four players in the current game
	 * 		</pre>
	 * * @post <pre>
	 *      The player with the largest army is determined, and set within the model.
	 * 		</pre>
	 */
	private void largestArmy() {
		if(getTurnTracker().getLargestArmy() != -1){
			getPlayerList().get(getTurnTracker().getLargestArmy()).setVictoryPoints(
					getPlayerList().get(getTurnTracker().getLargestArmy()).getVictoryPoints() - 2);
		}
		Player playerWithLargestArmy = null;
		for (Player tempPlayer: getPlayerList()) {
			if(tempPlayer.getSoldiers() > 2 && (playerWithLargestArmy == null || tempPlayer.getSoldiers() > playerWithLargestArmy.getSoldiers())){
				playerWithLargestArmy = tempPlayer;
			}
		}
		if(playerWithLargestArmy != null){
			getTurnTracker().setLargestArmy(playerWithLargestArmy.getPlayerIndex());
		}
		if(getTurnTracker().getLargestArmy() != -1){
			getPlayerList().get(getTurnTracker().getLargestArmy()).setVictoryPoints(
					getPlayerList().get(getTurnTracker().getLargestArmy()).getVictoryPoints() + 2);
		}
	}
	
	public boolean canBuyDevelopmentCard(int playerIndex){
        // Verifies that the Bank has Dev Cards
        boolean bool = getBank().canBuyDevelopmentCard();
        // Verifies that the Player has enough resources
        if (bool){
            bool = getPlayerList().get(playerIndex).canBuyDevelopmentCard();
        }
        return bool;
    }
	
	private shared.model.Map createMap(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
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
    			

		Map<HexLocation,Hex> hexes = new HashMap<>();
		while (!locations.isEmpty()) {
			Hex hex = null;
			HexType type = resources.get(0);
			HexLocation location = locations.get(0);
			int number = numbers.get(0);
			
			// If random tiles, take from list then remove it
			if (randomTiles) {
				int locIndex = ThreadLocalRandom.current().nextInt(0, locations.size());
				location = locations.get(locIndex);
			}
			
			// If random numbers, get number from a list then remove it
			if (randomNumbers) {
				int numIndex = ThreadLocalRandom.current().nextInt(0, numbers.size());
				if (type == null) {
					number = 0;
				}
				else {
					number = numbers.get(numIndex);
				}
				hex = new Hex(type,location,number);
				if (number == 0) {
					numbers.remove(Integer.valueOf(0));
				}
				else {
					numbers.remove(Integer.valueOf(number));
				}
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
    	
		Map<EdgeLocation,Port> finalPorts = new HashMap<>();
    	for (Port port: ports) {
    		EdgeLocation loc = portLocations.get(0);
    		if (randomPorts) {
    			int index = ThreadLocalRandom.current().nextInt(0, portLocations.size());
    			loc = portLocations.get(index);
    			port.setLocation(loc);
    		}
    		else {
    			port.setLocation(loc);
    		}
    		finalPorts.put(loc.getNormalizedLocation(), port);
    		portLocations.remove(loc);
    	}
    	
    	return new shared.model.Map(hexes,finalPorts);
	}
	
}
