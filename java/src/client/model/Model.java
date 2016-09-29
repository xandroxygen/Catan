package client.model;


import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.HashMap;
import java.util.Map;

import static shared.definitions.ResourceType.*;


/**
 * Model Facade Class
 */
public class Model {
	private Game game;
	private ModelUpdater modelUpdater;
	private static Model model;
	
	private Model() {
		modelUpdater = new ModelUpdater();
	}
	
	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}
	
	
	public int getVersion() {
		return game.getVersion();
	}
	
	public Game getGame() {
		return game;
	}
	
    /**
     * Updates model class.
     *
     * @pre <pre>
     *     json is not null
     *     json should look like this ...
     *
     *     ClientModel {
     *     	bank (ResourceList): The cards available to be distributed to the players.
     *     	chat (MessageList): All the chat messages.,
     *     	log (MessageList): All the log messages.,
     *     	map (Map),
     *     	players (array[Player]),
     *     	tradeOffer (TradeOffer, optional): The current trade offer, if there is one.,
     *     	turnTracker (TurnTracker): This tracks who's turn it is and what action's being done.,
     *     	version (index): The version of the model. This is incremented whenever anyone makes a move.,
     *     	winner (index): This is -1 when nobody's won yet. When they have, it's their order index [0-3]
     *     }
     *     ResourceList {
     *     	brick (integer),
     *     	ore (integer),
     *     	sheep (integer),
     *     	wheat (integer),
     *     	wood (integer)
     *     }
     *     MessageList {
     *     	lines (array[MessageLine])
     *     }
     *     MessageLine {
     *     	message (string),
     *     	source (string)
     *     }
     *     Map {
     *     	hexes (array[Hex]): A list of all the hexes on the grid - it's only land tiles,
     *     	ports (array[Port]),
     *     	roads (array[Road]),
     *     	settlements (array[VertexObject]),
     *     	cities (array[VertexObject]),
     *     	radius (integer): The radius of the map (it includes the center hex, and the ocean hexes; pass	this into the hexgrid constructor),
     *     	robber (HexLocation): The current location of the robber
     *     }
     *     Hex {
     *     	location (HexLocation),
     *     	resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What resource
     *     	this tile gives - it's only here if the tile is not desert.,
     *     	number (integer, optional): What number is on this tile. It's omitted if this is a desert hex.
     *     }
     *     HexLocation {
     *     	x (integer),
     *     	y (integer)
     *     }
     *     Port {
     *      resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What type
     *     	resource this port trades for. If it's omitted, then it's for any resource.,
     *     	location (HexLocation): Which hex this port is on. This shows the (ocean/non-existent) hex to
     *     	draw the port on.,
     *     	direction (string) = ['NW' or 'N' or 'NE' or 'E' or 'SE' or 'SW']: Which edge this port is on.,
     *     	ratio (integer): The ratio for trade in (ie, if this is 2, then it's a 2:1 port.
     *     }
     *     EdgeValue {
     *     	owner (index): The index (not id) of the player who owns this piece (0-3),
     *     	location (EdgeLocation): The location of this road.
     *     }
     *     EdgeLocation {
     *     	x (integer),
     *     	y (integer),
     *     	direction (string) = ['NW' or 'N' or 'NE' or 'SW' or 'S' or 'SE']
     *     }
     *     VertexObject {
     *     	owner (index): The index (not id) of the player who owns thie piece (0-3),
     *     	location (EdgeLocation): The location of this road.
     *     }
     *     Player {
     *     	cities (number): How many cities this player has left to play,
     *     	color (string): The color of this player.,
     *     	discarded (boolean): Whether this player has discarded or not already this discard phase.,
     *     	monuments (number): How many monuments this player has played.,
     *     	name (string),
     *     	newDevCards (DevCardList): The dev cards the player bought this turn.,
     *     	oldDevCards (DevCardList): The dev cards the player had when the turn started.,
     *		playerIndex (index): What place in the array is this player? 0-3. It determines their turn order. This is used often everywhere.,
     *  	playedDevCard (boolean): Whether the player has played a dev card this turn.,
     *  	playerID (integer): The unique playerID. This is used to pick the client player apart from the
     *  	others. This is only used here and in your cookie.,
     *  	resources (ResourceList): The resource cards this player has.,
     *  	roads (number),
     *  	settlements (integer),
     *		soldier(integer),
     *		victoryPoints (integer)
     * 	   }
     *	   DevCardList {
     * 		monopoly (number),
     * 		monument (number),
     * 		roadBuilding (number),
     * 		soldier (number),
     * 		yearOfPlenty (number)
     * 		}
     * 	   TradeOffer {
     *		sender (integer): The index of the person offering the trade
     * 		receiver (integer): The index of the person the trade was offered to.,
     * 		offer (ResourceList): Positive numbers are resources being offered. Negative are resources
     * 		being asked for.
     *	   }
     * 	   TurnTracker {
     * 		currentTurn (index): Who's turn it is (0-3),
     * 		status (string) = ['Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or
     * 		'SecondRound']: What's happening now,
     * 		longestRoad (index): The index of who has the longest road, -1 if no one has it
     * 		largestArmy (index): The index of who has the biggest army (3 or more), -1 if no one has it
     * 	   }
     * 	</pre>
     *
     * @post <pre>
     * 		the model will be updated according to the json passed into the function
     * </pre>
     *
     * @param json the Json String being passed in
     */
    public void updateModel(JsonObject json){
    	game = modelUpdater.updateModel(json);
    }

    /**
     * Checks whether the player can place a city.
     * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
     * @post You lost the resources required to build a city (2 wheat, 3 ore, 1 city), The city is on the map at the specified location, You got a settlement back
     * @param playerId the ID of the player who is requesting the move
     * @param location The location of the city.
     * @return result
     */
    boolean canPlaceCity(int playerId, VertexLocation location){
        return game.canPlaceCity(playerId, location);
    }

    /**
     * Checks whether the player can place a settlement.
     * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
     * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
     * @param playerId the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the settlement.
     * @return result
     */
    boolean canPlaceSettlement(int playerId, boolean free, VertexLocation location){
        return canPlaceSettlement(playerId, free, location);
    }

    /**
     * Checks whether the player can place a road.
     * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
     * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
     * @param playerId the ID of the player who is requesting the move
     * @param free Whether or not piece can be built for free.
     * @param location The location of the road.
     * @return result
     */
    boolean canPlaceRoad(int playerId, boolean free, EdgeLocation location) {
        return game.canPlaceRoad(playerId, free, location);
    }

    /**
     * Checks whether the player can buy a development card.
     * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
     * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devcard hand (unplayable this turn)
     * @return result
     */
    boolean canBuyDevelopmentCard(int playerId){
        int playerIndex = game.getPlayerIndex(playerId);
        // Verifies that the Bank has Dev Cards
        boolean bool = game.bank.canBuyDevelopmentCard();
        // Verifies that the Player has enough resources
        if (bool){
            bool = game.playerList.get(playerIndex).canBuyDevelopmentCard();
        }
        return bool;
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
        return game.canTradeWithPlayer(senderPlayerId, recieverPlayerId, offer);
    }

    /**
     * Checks whether the player can trade using a port or directly to the bank.
     * @pre It's your turn, You have the resources you are giving, For ratios less than 4, you have the correct port for the trade
     * @post The trade has been executed (the offered resources are in the bank, and the requested resource has been received)
     * @param ratio It must be a 2, 3, or 4.
     * @param inputResource Type of resource you are giving.
     * @param outputResource Type of resource you are receiving.
     * @return result
     */
    boolean canTradeWithBank(int playerId, int ratio, ResourceType inputResource, ResourceType outputResource){
        int playerIndex = game.getPlayerIndex(playerId);
        boolean bool;
        //Verifies that it is the turn of the PlayerId
        bool = game.turnTracker.getCurrentTurn() == playerIndex;
        //Verifies that the player has the resources that he wants to trade
        if(bool) {
            bool = game.playerList.get(playerIndex).canTradeWithBank(ratio, inputResource);
        }
        //Verifies that the ratios are correct
        if(bool) {
            if (inputResource == WOOD) {
                bool = false;
                //iterate through the ports
                for (Map.Entry<EdgeLocation, Port> tempPort : game.theMap.getPorts().entrySet()) {
                    //verifies the player has a municipality at the port
                    if (tempPort.getValue().getResource() == WOOD && !bool) {
                        bool = game.theMap.edgeHasPlayerMunicipality(tempPort.getValue().getLocation(),
                                game.playerList.get(playerIndex));
                    }
                }
            } else if (inputResource == BRICK) {
                bool = false;
                //iterate through the ports
                for (Map.Entry<EdgeLocation, Port> tempPort : game.theMap.getPorts().entrySet()) {
                    //verifies the player has a municipality at the port
                    if (tempPort.getValue().getResource() == BRICK && !bool) {
                        bool = game.theMap.edgeHasPlayerMunicipality(tempPort.getValue().getLocation(),
                                game.playerList.get(playerIndex));
                    }
                }
            } else if (inputResource == SHEEP) {
                bool = false;
                //iterate through the ports
                for (Map.Entry<EdgeLocation, Port> tempPort : game.theMap.getPorts().entrySet()) {
                    //verifies the player has a municipality at the port
                    if (tempPort.getValue().getResource() == SHEEP && !bool) {
                        bool = game.theMap.edgeHasPlayerMunicipality(tempPort.getValue().getLocation(),
                                game.playerList.get(playerIndex));
                    }
                }
            } else if (inputResource == WHEAT) {
                bool = false;
                //iterate through the ports
                for (Map.Entry<EdgeLocation, Port> tempPort : game.theMap.getPorts().entrySet()) {
                    //verifies the player has a municipality at the port
                    if (tempPort.getValue().getResource() == WHEAT && !bool) {
                        bool = game.theMap.edgeHasPlayerMunicipality(tempPort.getValue().getLocation(),
                                game.playerList.get(playerIndex));
                    }
                }
            } else if (inputResource == ORE) {
                bool = false;
                //iterate through the ports
                for (Map.Entry<EdgeLocation, Port> tempPort : game.theMap.getPorts().entrySet()) {
                    //verifies the player has a municipality at the port
                    if (tempPort.getValue().getResource() == ORE && !bool) {
                        bool = game.theMap.edgeHasPlayerMunicipality(tempPort.getValue().getLocation(),
                                game.playerList.get(playerIndex));
                    }
                }
            }
        }
        return bool;
    }

    /**
     * Checks whether you can play a soldier development card
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
     * 		</pre>
     * @param location New robber location.
     * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
     * @return result
     */
    public boolean canPlaySoldier(int playerId, HexLocation location, int victimIndex) {
        int playerIndex = game.getPlayerIndex(playerId);
        //verify it is the players turn
        boolean bool = game.isTurn(playerId);
        //verify the client model status is 'Playing'
        if (bool){
            bool = game.turnTracker.getStatus() == GameStatus.Playing;
        }
        //verify you have the specific card you want to play in your old dev card hand
        //and verify you have not yet played a non­monument dev card this turn
        if(bool){
            bool = game.playerList.get(playerIndex).canPlayDevCard(DevCardType.SOLDIER);
        }
        //verify The robber is not being kept in the same location
        if(bool){
            bool = game.theMap.getRobber().getLocation() != location;
        }
        return bool;
    }

    /**
     * Checks whether you can play a year of plenty card.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		Two specified resources are in the bank.
     * 		</pre>
     * @post <pre>
     * 		You gained the two specified resources
     * 		</pre>
     * @param resource1 The type of the first resource you'd like to receive
     * @param resource2 The type of the second resource you'd like to receive
     * @return result
     */
    public boolean canPlayYearOfPlenty(int playerId, ResourceType resource1, ResourceType resource2) {
        int playerIndex = game.getPlayerIndex(playerId);
        //verify it is the players turn
        boolean bool = game.isTurn(playerId);
        //verify the client model status is 'Playing'
        if (bool){
            bool = game.turnTracker.getStatus() == GameStatus.Playing;
        }
        //verify you have the specific card you want to play in your old dev card hand
        //and verify you have not yet played a non­monument dev card this turn
        if(bool){
            bool = game.playerList.get(playerIndex).canPlayDevCard(DevCardType.YEAR_OF_PLENTY);
        }
        //verify two specified resources are in the bank.
        if(bool){
            if(resource1 == resource2){
                bool = game.bank.getResourceDeck().get(resource1) >= 2;
            }else{
                bool = game.bank.getResourceDeck().get(resource1) >= 1 &&
                        game.bank.getResourceDeck().get(resource2) >= 1;
            }
        }
        return bool;
    }

    /**
     * Checks whether a Road card can be played.
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
     * 		</pre>
     * @param spot1 first edge location of road.
     * @param spot2 second edge location of road.
     * @return result
     */
    public boolean canPlayRoadCard(int playerId, EdgeLocation spot1, EdgeLocation spot2) {
        int playerIndex = game.getPlayerIndex(playerId);
        //verify it is the players turn
        boolean bool = game.isTurn(playerId);
        //verify the client model status is 'Playing'
        if (bool){
            bool = game.turnTracker.getStatus() == GameStatus.Playing;
        }
        //verify you have the specific card you want to play in your old dev card hand
        //and verify you have not yet played a non­monument dev card this turn
        if(bool){
            bool = game.playerList.get(playerIndex).canPlayDevCard(DevCardType.ROAD_BUILD);
        }
        //verify the first road location (spot1) is connected to one of your roads.
        //The second road location (spot2) is connected to one of your roads or to the first road location (spot1)
        //Neither road location is on water
        //You have at least two unused roads
        if(bool){
            bool = !game.theMap.hasRoadAtLocation(spot1) && !game.theMap.hasRoadAtLocation(spot2) &&
                    game.theMap.edgeHasAdjacentPlayerRoad(spot1, game.playerList.get(playerIndex));
            if(bool){
                game.theMap.getRoads().put(spot1, new Road(spot1, playerIndex));
                game.theMap.edgeHasAdjacentPlayerRoad(spot2, game.playerList.get(playerIndex));
                game.theMap.getRoads().remove(spot1);
            }
        }
        if(bool){
            bool = game.playerList.get(playerIndex).getRoads() >= 2;
        }
        return bool;
    }

    /**
     * Checks whether a Monopoly card can be played.
     * @pre <pre>
     * 		It's your turn
     * 		The client model status is 'Playing'
     * 		You have the specific card you want to play in your old dev card hand
     * 		You have not yet played a non­monument dev card this turn
     * 		</pre>
     * @post <pre>
     * 		All of the other players have given you all of their resource cards of the specified type
     * 		</pre>
     * @param resource The type of resource desired from other players.
     * @return result
     */
    public boolean canPlayMonopolyCard(int playerId, ResourceType resource) {
        int playerIndex = game.getPlayerIndex(playerId);
        //verify it is the players turn
        boolean bool = game.isTurn(playerId);
        //verify the client model status is 'Playing'
        if (bool){
            bool = game.turnTracker.getStatus() == GameStatus.Playing;
        }
        //verify you have the specific card you want to play in your old dev card hand
        //and verify you have not yet played a non­monument dev card this turn
        if(bool){
            bool = game.playerList.get(playerIndex).canPlayDevCard(DevCardType.MONOPOLY);
        }
        return bool;
    }

    /**
     * Checks whether a Monument card can be played.
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
     * @return result
     */
    public boolean canPlayMonumentCard(int playerId) {
        int playerIndex = game.getPlayerIndex(playerId);
        //verify it is the players turn
        boolean bool = game.isTurn(playerId);
        //verify the client model status is 'Playing'
        if (bool){
            bool = game.turnTracker.getStatus() == GameStatus.Playing;
        }
        //verify you have the specific card you want to play in your old dev card hand
        //and verify you have not yet played a non­monument dev card this turn
        if(bool){
            bool = game.playerList.get(playerIndex).canPlayDevCard(DevCardType.MONUMENT);
        }
        return bool;
    }

    /**
     * Checks whether the player can roll the dice
     * @pre It's their turn, and the model status is ROLLING
     * @return
     */
    boolean canRollDice(int playerId){
        return game.canRollDice(playerId);
    }

    /**
     * Checks whether the player can send a message.
     * @post  The chat contains your message at the end.
     * @param message the message the player wishes to send.
     * @return
     */
    boolean canSendMessage(int playerId, String message){
        return canSendMessage(playerId, message);
    }

    /**
     * Checks whether the player can end the turn.
     * @post  The cards in your new dev card hand have been transferred to your old dev card hand, It is the next player’s turn
     * @return result
     */
    boolean canEndTurn(int playerId){
        return false;
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
        return game.playerList.size() < 4;
    }

    /**
     * Checks whether the player can get rolled resources.
     * @param diceRoll the number that was rolled
     * @return true if there are resources to receive
     */
    boolean canGetRolledResourses(int diceRoll){
        return game.canGetRolledResources(diceRoll);
    }

    /**
     * Make a trade offer to another player. Corresponds to canMakeTradeWithPlayer
     * @param senderPlayerId Player offering the trade
     * @param receiverPlayerId Player being offered the trade
     * @param offer hand of cards to trade
     */
    boolean canMakeTradeOffer(int senderPlayerId, int receiverPlayerId, Map<ResourceType, Integer> offer){
        int senderPlayerIndex = game.getPlayerIndex(senderPlayerId);
        int receiverPlayerIndex = game.getPlayerIndex(receiverPlayerId);
        //Checks to see if it is the players turn
        boolean bool = game.isTurn(senderPlayerId);
        //Checks to see if the sender has all resources he is offering
        if (bool && offer.get(ResourceType.BRICK) > 0){
            bool = offer.get(ResourceType.BRICK) <= game.playerList.get(senderPlayerIndex).getResourceHand().get(ResourceType.BRICK);
        }
        if (bool && offer.get(ResourceType.WOOD) > 0){
            bool = offer.get(ResourceType.WOOD) <= game.playerList.get(senderPlayerIndex).getResourceHand().get(ResourceType.WOOD);
        }
        if (bool && offer.get(ResourceType.WHEAT) > 0){
            bool = offer.get(ResourceType.WHEAT) <= game.playerList.get(senderPlayerIndex).getResourceHand().get(ResourceType.WHEAT);
        }
        if (bool && offer.get(ResourceType.SHEEP) > 0){
            bool = offer.get(ResourceType.SHEEP) <= game.playerList.get(senderPlayerIndex).getResourceHand().get(ResourceType.SHEEP);
        }
        if (bool && offer.get(ResourceType.ORE) > 0){
            bool = offer.get(ResourceType.ORE) <= game.playerList.get(senderPlayerIndex).getResourceHand().get(ResourceType.ORE);
        }
        //Checks to see if the receiver has all resources he is offering
        if (bool && offer.get(ResourceType.BRICK) < 0){
            bool = offer.get(ResourceType.BRICK) <= game.playerList.get(receiverPlayerIndex).getResourceHand().get(ResourceType.BRICK);
        }
        if (bool && offer.get(ResourceType.WOOD) < 0){
            bool = offer.get(ResourceType.WOOD) <= game.playerList.get(receiverPlayerIndex).getResourceHand().get(ResourceType.WOOD);
        }
        if (bool && offer.get(ResourceType.WHEAT) < 0){
            bool = offer.get(ResourceType.WHEAT) <= game.playerList.get(receiverPlayerIndex).getResourceHand().get(ResourceType.WHEAT);
        }
        if (bool && offer.get(ResourceType.SHEEP) < 0){
            bool = offer.get(ResourceType.SHEEP) <= game.playerList.get(receiverPlayerIndex).getResourceHand().get(ResourceType.SHEEP);
        }
        if (bool && offer.get(ResourceType.ORE) < 0){
            bool = offer.get(ResourceType.ORE) <= game.playerList.get(receiverPlayerIndex).getResourceHand().get(ResourceType.ORE);
        }
        return bool;
    }

    /**
     * Sent by player who has been offered a trade.
     */
    boolean canAcceptTradeOffer(int receiverPlayerId){
        return game.tradeOffer != null && game.tradeOffer.getOffer() != null && game.tradeOffer.getReceiver() == receiverPlayerId;
    }
}
