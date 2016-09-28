package client.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

/**
 * Bank Class
 */
public class Bank {

    public HashMap<ResourceType, Integer> resourseDeck;
    public HashMap<DevCardType, Integer> developmentCards;
    
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

    /**
     * checks whether the player can buy a development card
     *
     * @pre <pre>
     *      It is the players turn
     *      The player has not played a development card alreday this turn
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
        return false;
    }

}
