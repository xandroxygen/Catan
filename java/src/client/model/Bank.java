package client.model;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Bank Class
 */
public class Bank {

    private HashMap<ResourceType, Integer> resourseDeck;
    private ArrayList<DevCardType> developmentCards;

    public HashMap<ResourceType, Integer> getResourseDeck() {
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
        return developmentCards.size() < 0;
    }

    //TODO create CLASS HashMap<ResourceType, Integer> resourceDeck

}
