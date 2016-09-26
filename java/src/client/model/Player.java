package client.model;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.HashMap;

/**
 * Player class
 */
public class Player {
	
	private String name;
	private CatanColor color;
	private int playerIndex;
	private int playerId;
	private boolean developmentCardPlayed;
	private int victoryPoints;
	private HashMap<ResourceType, Integer> resourceHand;
	private HashMap<DevCardType, Integer> playableDevCardHand;
	private HashMap<DevCardType, Integer> unplayableDevCardHand;
	private HashMap<PieceType, Integer> piecesAvailable;

	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Checks whether the player can place a city.
	 * @pre It's your turn, The city location is where you currently have a settlement, You have the required resources (2 wheat, 3 ore; 1 city)
	 * @post You lost the resources required to build a city (2 wheat, 3 ore; 1 city), The city is on the map at the specified location, You got a settlement back
	 * @param location The location of the city.
	 * @return result
	 */
	public boolean canPlaceCity(VertexLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the player can place a settlement.
	 * @pre It's your turn, The settlement location is open, The settlement location is not on water, The settlement location is connected to one of your roads except during setup, You have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement cannot be placed adjacent to another settlement
	 * @post You lost the resources required to build a settlement (1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement), The settlement is on the map at the specified location
	 * @param free Whether or not piece can be built for free.
	 * @param location The location of the settlement.
	 * @return result
	 */
	public boolean canPlaceSettlement(boolean free, VertexLocation location) {
		return false;
	}
	
	/**
	 * Checks whether the player can place a road.
	 * @pre It's your turn, The road location is open, The road location is connected to another road owned by the player, The road location is not on water, You have the required resources (1 wood, 1 brick; 1 road), Setup round: Must be placed by settlement owned by the player with no adjacent road.
	 * @post You lost the resources required to build a road (1 wood, 1 brick - 1 road), The road is on the map at the specified location, If applicable, longest road has been awarded to the player with the longest road
	 * @param free Whether or not piece can be built for free.
	 * @param location The location of the road.
	 * @return result 
	 */
	public boolean canPlaceRoad(boolean free, VertexLocation location) {
		return false;
	}

	/**
	 * Checks whether you can play a development card
	 * @pre <pre>
	 * 		It's your turn
	 * 		The client model status is 'Playing'
	 * 		You have the specific card you want to play in your old dev card hand
	 * 		You have not yet played a non­monument dev card this turn
	 * 		</pre>
	 * @param devCardType the type of dev card the player wants to play
	 * @return result
	 */
	public boolean canPlayDevCard(DevCardType devCardType) {
		boolean bool = playableDevCardHand.containsKey(devCardType);
		if(bool){
			bool = !developmentCardPlayed;
		}
		return bool;
	}
	
//	/**
//	 * Checks whether you can play a soldier development card
//	 * @pre <pre>
//	 * 		It's your turn
//	 * 		The client model status is 'Playing'
//	 * 		You have the specific card you want to play in your old dev card hand
//	 * 		You have not yet played a non­monument dev card this turn
//	 * 		The robber is not being kept in the same location
//	 * 		If a player is being robbed (i.e., victimIndex != ­1)
//	 * 		The player being robbed has resource cards
//	 * 		</pre>
//	 * @post <pre>
//	 * 		The robber is in the new location
//	 * 		The player being robbed (if any) gave you one of his resource cards (randomly selected)
//	 * 		If applicable, largest army has been awarded to the player who has played the most soldier cards
//	 * 		You are not allowed to play other development cards during this turn (except for monument cards, which may still be played)
//	 * 		</pre>
//	 * @param location New robber location.
//	 * @param victimIndex The playerIndex of the player you wish to rob, or -1 to rob no one.
//	 * @return result
//	 */
//	public boolean canPlaySoldier(HexLocation location, int victimIndex) {
//		return false;
//	}
//
//	/**
//	 * Checks whether you can play a year of plenty card.
//	 * @pre <pre>
//	 * 		It's your turn
//	 * 		The client model status is 'Playing'
//	 * 		You have the specific card you want to play in your old dev card hand
//	 * 		You have not yet played a non­monument dev card this turn
//	 * 		Two specified resources are in the bank.
//	 * 		</pre>
//	 * @post <pre>
//	 * 		You gained the two specified resources
//	 * 		</pre>
//	 * @param resource1 The type of the first resource you'd like to receive
//	 * @param resource2 The type of the second resource you'd like to receive
//	 * @return result
//	 */
//	public boolean canPlayYearOfPlenty(ResourceType resource1, ResourceType resource2) {
//		return false;
//	}
//
//	/**
//	 * Checks whether a Road card can be played.
//	 * @pre <pre>
//	 * 		It's your turn
//	 * 		The client model status is 'Playing'
//	 * 		You have the specific card you want to play in your old dev card hand
//	 * 		You have not yet played a non­monument dev card this turn
//	 * 		The first road location (spot1) is connected to one of your roads.
//	 * 		The second road location (spot2) is connected to one of your roads or to the first road location (spot1)
//	 * 		Neither road location is on water
//	 * 		You have at least two unused roads
//	 * 		</pre>
//	 * @post <pre>
//	 * 		You have two fewer unused roads
//	 * 		Two new roads appear on the map at the specified locations
//	 * 		If applicable, longest road has been awarded to the player with the longest road
//	 * 		</pre>
//	 * @param spot1 first edge location of road.
//	 * @param spot2 second edge location of road.
//	 * @return result
//	 */
//	public boolean canPlayRoadCard(EdgeLocation spot1, EdgeLocation spot2) {
//		return false;
//	}
//
//	/**
//	 * Checks whether a Monopoly card can be played.
//	 * @pre <pre>
//	 * 		It's your turn
//	 * 		The client model status is 'Playing'
//	 * 		You have the specific card you want to play in your old dev card hand
//	 * 		You have not yet played a non­monument dev card this turn
//	 * 		</pre>
//	 * @post <pre>
//	 * 		All of the other players have given you all of their resource cards of the specified type
//	 * 		</pre>
//	 * @param resource The type of resource desired from other players.
//	 * @return result
//	 */
//	public boolean canPlayMonopolyCard(ResourceType resource) {
//		return false;
//	}
//
//	/**
//	 * Checks whether a Monument card can be played.
//	 * @pre <pre>
//	 * 		It's your turn
//	 * 		The client model status is 'Playing'
//	 * 		You have the specific card you want to play in your old dev card hand
//	 * 		You have not yet played a non­monument dev card this turn
//	 * 		You have enough monument cards to win the game (i.e., reach 10 victory points)
//	 * 		</pre>
//	 * @post <pre>
//	 * 		You gained a victory point
//	 * 		</pre>
//	 * @return result
//	 */
//	public boolean canPlayMonumentCard() {
//		return false;
//	}
	
	/**
	 * Checks whether the player can buy a development card.
	 * @pre It's your turn, You have the required resources (1 ore, 1 wheat, 1 sheep), There are dev cards left in the deck.
	 * @post You have a new card; If it is a monument card, it has been added to your old devCard hand, If it is a non­monument card, it has been added to your new devcard hand (unplayable this turn)
	 * @return result
	 */
	public boolean canBuyDevelopmentCard() {
		return resourceHand.containsKey(ResourceType.ORE) &&
				resourceHand.containsKey(ResourceType.WHEAT) &&
				resourceHand.containsKey(ResourceType.SHEEP);
	}
	
	/**
	 * Checks whether the player can trade with another player
	 * @pre It's your turn, You have the resources you are offering.
	 * @post The trade is offered to the other player (stored in the server model)
	 * @param offer list of Resources, Negative numbers mean you get those cards
	 * @param recipient the playerIndex of the offer recipient.
	 * @return result
	 */
	public boolean canTradeWithPlayer(HashMap<ResourceType, Integer> offer, int recipient) {
		return false;
	}


	/**
	 * Checks whether the player can trade using a port or directly to the bank.
	 * @pre It's your turn, You have the resources you are giving, For ratios less than 4, you have the correct port for the trade
	 * @post The trade has been executed (the offered resources are in the bank, and the requested resource has been received)
	 * @param ratio It must be a 2, 3, or 4.
	 * @param inputResource Type of resource you are giving.
	 * @return result
	 */
	boolean canTradeWithBank(int ratio, ResourceType inputResource){
		return resourceHand.get(inputResource) >= ratio;
	}
	
	/**
	 * Checks whether the player can trade with the maritime.
	 * @pre It's your turn, You have the resources you are giving, For ratios less than 4, you have the correct port for the trade
	 * @post The trade has been executed (the offered resources are in the bank, and the requested resource has been received)
	 * @param ratio It must be a 2, 3, or 4.
	 * @param inputResource Type of resource you are giving.
	 * @param outputResource Type of resource you are receiving.
	 * @return result
	 */
	public boolean canTradeWithMaritime(int ratio, ResourceType inputResource, ResourceType outputResource) {
		return false;
	}
	
	/**
	 * Checks whether the robber can be moved and a player robbed.
	 * @pre The robber is not being kept in the same location, If a player is being robbed (i.e., victimIndex != ­1), the player being robbed has resource cards
	 * @post The robber is in the new location, The player being robbed (if any) gave you one of his resource cards (randomly selected)
	 * @param location New location for robber.
	 * @param playerIndex Index of player to be robbed or -1 if no is to be robbed.
	 * @return result
	 */
	public boolean canRobPlayer(HexLocation location, int playerIndex) {
		return false;
	}
	
	/**
	 * Checks whether the player can roll the dice.
	 * @pre It is your turn, The client model’s status is ‘Rolling’
	 * @post The client model’s status is now in ‘Discarding’ or ‘Robbing’ or ‘Playing’
	 * @return 
	 */
	public boolean canRollDice() {
		return false;
	}
	
	
	/**
	 * Checks whether the player can send a message.
	 * @post  The chat contains your message at the end.
	 * @param message the message the player wishes to send.
	 * @return 
	 */
	public boolean canSendMessage(String message) {
		return false;
	}
	
	/**
	 * Checks whether the player can end the turn.
	 * @post  The cards in your new dev card hand have been transferred to your old dev card hand, It is the next player’s turn
	 * @return result
	 */
	public boolean canEndTurn() {
		return false;
	}
	
}
