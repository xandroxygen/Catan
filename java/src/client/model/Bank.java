package client.model;

import java.util.ArrayList;
import java.util.HashMap;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

/**
 * Bank Class
 */
public class Bank {

    public HashMap<ResourceType, Integer> resourseDeck;
    public ArrayList<DevCardType> developmentCards;

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

}
