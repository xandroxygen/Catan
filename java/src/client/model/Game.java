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
     * @return
     */
    public boolean canCreateUser(){
        return false;
    }

    /**
     * checks to see if the user is who he says he is
     * @return
     */
    public boolean canAuthenticateUser(){
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canGetServerInfo(){
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canStartGame(){
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canEndGame(){
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canRotateTurn(){
        return false;
    }

    /**
     *
     * @param playerId
     * @return
     */
    public boolean canRollDice(int playerId){
        return false;
    }

    /**
     *
     * @param playerId
     * @return
     */
    public boolean canPayPlayer(int playerId){
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canAwardPlayers(){
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canCheckForWinners(){
        return false;
    }
}
