package client.model;

import java.util.ArrayList;

/**
 * Game class.
 */
public class Game {

    public ArrayList<Player> playerList;
    public Dice dice;
    public Map theMap;
    public Bank bank;
    public int currentTurnIndex;
    public String status;
    public String winner;

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
    public boolean canCreateUser(){
        return false;
    }

    /**
     * Authenticates the user
     *
     * @pre <pre>
     *      The player must be an atherized user
     * 	</pre>
     *
     * @post <pre>
     *      returns true to authenticate the user.
     * </pre>
     *
     * @return
     */
    public boolean canAuthenticateUser(){
        return false;
    }
}
