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
		int count = resourceDeck.containsKey(ResourceType.WHEAT) ? resourceDeck.get(ResourceType.WHEAT) : 0;
		resourceDeck.put(ResourceType.WHEAT, count + 2);
		
		player.addToResourceHand(ResourceType.ORE, -3);
		int count2 = resourceDeck.containsKey(ResourceType.ORE) ? resourceDeck.get(ResourceType.ORE) : 0;
		resourceDeck.put(ResourceType.ORE, count2 + 3);
	}
	
	public void purchaseRoad(Player player) {
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WOOD, -1);
		int count = resourceDeck.containsKey(ResourceType.WOOD) ? resourceDeck.get(ResourceType.WOOD) : 0;
		resourceDeck.put(ResourceType.WOOD, count + 1);
		
		player.addToResourceHand(ResourceType.BRICK, -1);
		int count2 = resourceDeck.containsKey(ResourceType.BRICK) ? resourceDeck.get(ResourceType.BRICK) : 0;
		resourceDeck.put(ResourceType.BRICK, count2 + 1);
	}
	
	public void purchaseSettlement(Player player) {
		//(1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement)
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WOOD, -1);
		int count = resourceDeck.containsKey(ResourceType.WOOD) ? resourceDeck.get(ResourceType.WOOD) : 0;
		resourceDeck.put(ResourceType.WOOD, count + 1);
		
		player.addToResourceHand(ResourceType.BRICK, -1);
		int count2 = resourceDeck.containsKey(ResourceType.BRICK) ? resourceDeck.get(ResourceType.BRICK) : 0;
		resourceDeck.put(ResourceType.BRICK, count2 + 1);
		
		player.addToResourceHand(ResourceType.WHEAT, -1);
		int count3 = resourceDeck.containsKey(ResourceType.WHEAT) ? resourceDeck.get(ResourceType.WHEAT) : 0;
		resourceDeck.put(ResourceType.WHEAT, count3 + 1);
		
		player.addToResourceHand(ResourceType.SHEEP, -1);
		int count4 = resourceDeck.containsKey(ResourceType.SHEEP) ? resourceDeck.get(ResourceType.SHEEP) : 0;
		resourceDeck.put(ResourceType.SHEEP, count4 + 1);
	}
}
