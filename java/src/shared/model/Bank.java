package shared.model;

import com.google.gson.JsonObject;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

import java.util.HashMap;

/**
 * Bank Class
 */
public class Bank {

    private HashMap<ResourceType, Integer> resourseDeck;
    private HashMap<DevCardType, Integer> developmentCards;

    public Bank(JsonObject modelJSON) {
    	// Parse resources
    	resourseDeck = new HashMap<>();
		JsonObject resourcesJSON = modelJSON.getAsJsonObject("bank");
		resourseDeck.put(ResourceType.BRICK, resourcesJSON.get("brick").getAsInt());
		resourseDeck.put(ResourceType.ORE, resourcesJSON.get("ore").getAsInt());
		resourseDeck.put(ResourceType.SHEEP, resourcesJSON.get("sheep").getAsInt());
		resourseDeck.put(ResourceType.WHEAT, resourcesJSON.get("wheat").getAsInt());
		resourseDeck.put(ResourceType.WOOD, resourcesJSON.get("wood").getAsInt());


		// Parse old dev cards
		developmentCards = new HashMap<>();
		JsonObject oldDevCardsJSON = modelJSON.getAsJsonObject("deck");
		developmentCards.put(DevCardType.MONOPOLY, oldDevCardsJSON.get("monopoly").getAsInt());
		developmentCards.put(DevCardType.MONUMENT, oldDevCardsJSON.get("monument").getAsInt());
		developmentCards.put(DevCardType.ROAD_BUILD, oldDevCardsJSON.get("roadBuilding").getAsInt());
		developmentCards.put(DevCardType.SOLDIER, oldDevCardsJSON.get("soldier").getAsInt());
		developmentCards.put(DevCardType.YEAR_OF_PLENTY, oldDevCardsJSON.get("yearOfPlenty").getAsInt());

    }

    public HashMap<ResourceType, Integer> getResourceDeck() {
        return resourseDeck;
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

	public void setResourseDeck(HashMap<ResourceType, Integer> resourseDeck) {
		this.resourseDeck = resourseDeck;
	}

	public void setDevelopmentCards(HashMap<DevCardType, Integer> developmentCards) {
		this.developmentCards = developmentCards;
	}

	public void purchaseCity(Player player) {
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WHEAT, -2);
		int count = resourseDeck.containsKey(ResourceType.WHEAT) ? resourseDeck.get(ResourceType.WHEAT) : 0;
		resourseDeck.put(ResourceType.WHEAT, count + 2);
		
		player.addToResourceHand(ResourceType.ORE, -3);
		int count2 = resourseDeck.containsKey(ResourceType.ORE) ? resourseDeck.get(ResourceType.ORE) : 0;
		resourseDeck.put(ResourceType.ORE, count2 + 3);
	}
	
	public void purchaseRoad(Player player) {
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WOOD, -1);
		int count = resourseDeck.containsKey(ResourceType.WOOD) ? resourseDeck.get(ResourceType.WOOD) : 0;
		resourseDeck.put(ResourceType.WOOD, count + 1);
		
		player.addToResourceHand(ResourceType.BRICK, -1);
		int count2 = resourseDeck.containsKey(ResourceType.BRICK) ? resourseDeck.get(ResourceType.BRICK) : 0;
		resourseDeck.put(ResourceType.BRICK, count2 + 1);
	}
	
	public void purchaseSettlement(Player player) {
		//(1 wood, 1 brick, 1 wheat, 1 sheep; 1 settlement)
		// Subtract from player and add to bank resources
		player.addToResourceHand(ResourceType.WOOD, -1);
		int count = resourseDeck.containsKey(ResourceType.WOOD) ? resourseDeck.get(ResourceType.WOOD) : 0;
		resourseDeck.put(ResourceType.WOOD, count + 1);
		
		player.addToResourceHand(ResourceType.BRICK, -1);
		int count2 = resourseDeck.containsKey(ResourceType.BRICK) ? resourseDeck.get(ResourceType.BRICK) : 0;
		resourseDeck.put(ResourceType.BRICK, count2 + 1);
		
		player.addToResourceHand(ResourceType.WHEAT, -1);
		int count3 = resourseDeck.containsKey(ResourceType.WHEAT) ? resourseDeck.get(ResourceType.WHEAT) : 0;
		resourseDeck.put(ResourceType.WHEAT, count3 + 1);
		
		player.addToResourceHand(ResourceType.SHEEP, -1);
		int count4 = resourseDeck.containsKey(ResourceType.SHEEP) ? resourseDeck.get(ResourceType.SHEEP) : 0;
		resourseDeck.put(ResourceType.SHEEP, count4 + 1);
	}
}
