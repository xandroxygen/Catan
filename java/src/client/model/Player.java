package client.model;
import java.util.List;

import shared.definitions.*;

/**
 * Created by Jonathan Skaggs on 9/14/2016.
 */
public class Player {
	
	private String name;
	private CatanColor color;
	private int playerIndex;
	private boolean developmentCardPlayed;
	private int victoryPoints;
	private int numberofBrickResourceCards;
	private int numberofWoodResourceCards;
	private int numberofOreResourceCards;
	private int numberofSheepResourceCards;
	private int numberofWheatResourceCards;
	private List<Object> cityList;
	private List<Object> settlementList;
	private List<Object> roadList;
	
	/**
	 * Checks whether the player can place a city.
	 * @return 
	 */
	public boolean canPlaceCity() {
		return false;
	}
	
	/**
	 * Checks whether the player can place a settlement.
	 * @return 
	 */
	public boolean canPlaceSettlement() {
		return false;
	}
	
	/**
	 * Checks whether the player can place a road.
	 * @return 
	 */
	public boolean canPlaceRoad() {
		return false;
	}
	
	/**
	 * Checks whether the player can buy a development card.
	 * @return 
	 */
	public boolean canBuyDevelopmentCard() {
		return false;
	}
	
	/**
	 * Checks whether the player can trade with another player
	 * @param playerId the id of the player to be traded with.
	 * @return
	 */
	public boolean canTradeWithPlayer(int playerId) {
		return false;
	}
	
	/**
	 * Checks whether the player can trade with the Bank.
	 * @return 
	 */
	public boolean canTradeWithBank() {
		return false;
	}
	
	/**
	 * Checks whether the player can play a development card
	 * @param type the type of development card to be played.
	 * @return
	 */
	public boolean canPlayDevelopmentCard(DevCardType type) {
		return false;
	}
	
	/**
	 * Checks whether the player can roll the dice.
	 * @return 
	 */
	public boolean canRollDice() {
		return false;
	}
	
	
	/**
	 * Checks whether the player can send a message.
	 * @param message the message the player wishes to send.
	 * @return 
	 */
	public boolean canSendMessage(String message) {
		return false;
	}
	
	/**
	 * Checks whether the player can end the turn.
	 * @return 
	 */
	public boolean canEndTurn() {
		return false;
	}
	
}
