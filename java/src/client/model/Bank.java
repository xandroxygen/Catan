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
     * checks whether the bank has enough brick to withdraw
     * @param numberOfBrick the desired number of bricks to withdraw
     * @return
     */
    public boolean canWithdrawBrick(int numberOfBrick){
        return false;
    }

    /**
     * checks whether the bank has enough wood to withdraw
     * @param numberOfWood the desired number of wood to withdraw
     * @return
     */
    public boolean canWithdrawWood(int numberOfWood){
        return false;
    }

    /**
     * checks whether the bank has enough ore to withdraw
     * @param numberOfOre the desired number of ore to withdraw
     * @return
     */
    public boolean canWithdrawOre(int numberOfOre){
        return false;
    }

    /**
     * checks whether the bank has enough sheep to withdraw
     * @param numberOfSheep the desired number of sheep to withdraw
     * @return
     */
    public boolean canWithdrawSheep(int numberOfSheep){
        return false;
    }

    /**
     * checks whether the bank has enough wheat to withdraw
     * @param numberOfWheat the desired number of wheat to withdraw
     * @return
     */
    public boolean canWithdrawWheat(int numberOfWheat){
        return false;
    }

    /**
     * checks whether the player has enough bricks to deposit
     * @param numberOfBrick the desired number Of bricks to withdraw
     * @return
     */
    public boolean canDepositBrick(int playerId, int numberOfBrick){
        return false;
    }

    /**
     * checks whether the player has enough wood to deposit
     * @param numberOfWood the desired number Of wood to withdraw
     * @return
     */
    public boolean canDepositWood(int numberOfWood){
        return false;
    }

    /**
     * checks whether the player has enough ore to deposit
     * @param numberOfOre the desired number Of ore to withdraw
     * @return
     */
    public boolean canDepositOre(int numberOfOre){
        return false;
    }

    /**
     * checks whether the player has enough sheep to deposit
     * @param numberOfSheep the desired number Of sheep to withdraw
     * @return
     */
    public boolean canDepositSheep(int numberOfSheep){
        return false;
    }

    /**
     * checks whether the player has enough wheat to deposit
     * @param numberOfWheat the desired number Of wheat to withdraw
     * @return
     */
    public boolean canDepositWheat(int numberOfWheat){
        return false;
    }

    /**
     * checks whether the player can buy a development card
     * @return
     */
    public boolean canBuyDevelopmentCard(){
        return false;
    }

}
