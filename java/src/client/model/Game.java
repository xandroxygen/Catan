package client.model;

import java.util.ArrayList;

/**
 * Game class.
 */
public class Game {

    public ArrayList<Player> playerList;
    public Map theMap;
    public Bank bank;
    public int currentTurnIndex;
    public TurnTracker turnTracker;
    public MessageList log;
    public MessageList chat;
    public String winner;
    public Robber robber;

    public Robber getRobber() {
        return robber;
    }

    public boolean isTurn(int playerId){
        //TODO (I am not sure how getCurrentTurn() is implemented) Is it a playerId or is it an index number?
        return getPlayerIndex(playerId) == turnTracker.getCurrentTurn();
    }

    public int getPlayerIndex(int playerId){
        int i = 0;
        for (Player tempPlayer  : playerList) {
            if(tempPlayer.getPlayerId() == playerId){
                return i;
            }
            i++;
        }
        //TODO should throw and invalidExceptionError
        return -1;
    }

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
        return playerList.size() < 4;
    }

    /**
     * Authenticates the user
     *
     * @pre <pre>
     *      The player must be an authenticated user
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
