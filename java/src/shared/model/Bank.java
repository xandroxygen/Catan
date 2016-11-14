package shared.model;

import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

import java.util.HashMap;

/**
 * Bank Class
 */
public class Bank {

    private HashMap<ResourceType, Integer> resourceDeck;
    private HashMap<DevCardType, Integer> developmentCards;

	public HashMap<DevCardType, Integer> getDevelopmentCards() {
		return developmentCards;
	}

	public Bank(JsonObject modelJSON) {
    	// Parse resources
    	resourceDeck = new HashMap<>();
		JsonObject resourcesJSON = modelJSON.getAsJsonObject("bank");
		resourceDeck.put(ResourceType.BRICK, resourcesJSON.get("brick").getAsInt());
		resourceDeck.put(ResourceType.ORE, resourcesJSON.get("ore").getAsInt());
		resourceDeck.put(ResourceType.SHEEP, resourcesJSON.get("sheep").getAsInt());
		resourceDeck.put(ResourceType.WHEAT, resourcesJSON.get("wheat").getAsInt());
		resourceDeck.put(ResourceType.WOOD, resourcesJSON.get("wood").getAsInt());


		// Parse old dev cards
		developmentCards = new HashMap<>();
		JsonObject oldDevCardsJSON = modelJSON.getAsJsonObject("deck");
		developmentCards.put(DevCardType.MONOPOLY, oldDevCardsJSON.get("monopoly").getAsInt());
		developmentCards.put(DevCardType.MONUMENT, oldDevCardsJSON.get("monument").getAsInt());
		developmentCards.put(DevCardType.ROAD_BUILD, oldDevCardsJSON.get("roadBuilding").getAsInt());
		developmentCards.put(DevCardType.SOLDIER, oldDevCardsJSON.get("soldier").getAsInt());
		developmentCards.put(DevCardType.YEAR_OF_PLENTY, oldDevCardsJSON.get("yearOfPlenty").getAsInt());

    }
	
	// Create new default Bank for new game
	public Bank() {

		// Initialize the resource deck
		resourceDeck = new HashMap<>();
		resourceDeck.put(ResourceType.BRICK,19);
		resourceDeck.put(ResourceType.ORE,19);
		resourceDeck.put(ResourceType.SHEEP,19);
		resourceDeck.put(ResourceType.WHEAT,19);
		resourceDeck.put(ResourceType.WOOD,19);
		
		
		// Initialize the dev card deck
		developmentCards = new HashMap<>();
		developmentCards.put(DevCardType.MONOPOLY, 2);
		developmentCards.put(DevCardType.SOLDIER, 14);
		developmentCards.put(DevCardType.MONUMENT, 5);
		developmentCards.put(DevCardType.ROAD_BUILD, 2);
		developmentCards.put(DevCardType.YEAR_OF_PLENTY, 2);
	}

    public HashMap<ResourceType, Integer> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * checks whether the player can buy a development card
     *
     * @pre <pre>
     *      It is the players turn
     *      The player has not played a development card already this turn
     *      There are development cards in the bank
     * 	</pre>
     *
     * @post <pre>
     *      It will return true if there is a development card left in the bank to buy.
     * </pre>
     *
     * @return
     */
    public boolean canBuyDevelopmentCard(){
		return developmentCards.get(DevCardType.SOLDIER) > 0 &&
				developmentCards.get(DevCardType.YEAR_OF_PLENTY) > 0 &&
				developmentCards.get(DevCardType.MONOPOLY) > 0 &&
				developmentCards.get(DevCardType.ROAD_BUILD) > 0 &&
				developmentCards.get(DevCardType.MONUMENT) > 0;
    }

	public void setResourceDeck(HashMap<ResourceType, Integer> resourceDeck) {
		this.resourceDeck = resourceDeck;
	}

	public void setDevelopmentCards(HashMap<DevCardType, Integer> developmentCards) {
		this.developmentCards = developmentCards;
	}

	public void purchaseCity(Player player) {
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WHEAT, -2);
		addToResourceDeck(ResourceType.WHEAT,2);
		
		player.addToResourceHand(ResourceType.ORE, -3);
		addToResourceDeck(ResourceType.ORE, 3);
	}
	
	public void purchaseRoad(Player player) {
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WOOD, -1);
		addToResourceDeck(ResourceType.WOOD, 1);
		
		player.addToResourceHand(ResourceType.BRICK, -1);
		addToResourceDeck(ResourceType.BRICK, 1);
	}
	
	public void purchaseSettlement(Player player) {
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WOOD, -1);
		addToResourceDeck(ResourceType.WOOD,1);
		
		player.addToResourceHand(ResourceType.BRICK, -1);
		addToResourceDeck(ResourceType.BRICK,1);
		
		player.addToResourceHand(ResourceType.WHEAT, -1);
		addToResourceDeck(ResourceType.WHEAT,1);
		
		player.addToResourceHand(ResourceType.SHEEP, -1);
		addToResourceDeck(ResourceType.SHEEP,1);
	}
	
	public void addToResourceDeck(ResourceType type, int value) {
		int count = resourceDeck.containsKey(type) ? resourceDeck.get(type) : 0;
		resourceDeck.put(type, count + value);
	}
}
