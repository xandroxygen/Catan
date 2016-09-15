package client.model;

import java.util.ArrayList;

/**
 * Created by Jonathan Skaggs on 9/14/2016.
 */
public class Bank {

    public int numberOfBrickResourceCards;
    public int numberOfWoodResourceCards;
    public int numberOfOreResourceCards;
    public int numberOfSheepResourceCards;
    public int numberOfWheatResourceCards;
    public ArrayList<DevelopmentCards> developmentCards;

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
