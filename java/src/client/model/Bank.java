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
    boolean canWithdrawBrick(int numberOfBrick){
        return false;
    }

    /**
     * checks whether the bank has enough wood to withdraw
     * @param numberOfWood the desired number of wood to withdraw
     * @return
     */
    boolean canWithdrawWood(int numberOfWood){
        return false;
    }

    /**
     * checks whether the bank has enough ore to withdraw
     * @param numberOfOre the desired number of ore to withdraw
     * @return
     */
    boolean canWithdrawOre(int numberOfOre){
        return false;
    }

    /**
     * checks whether the bank has enough sheep to withdraw
     * @param numberOfSheep the desired number of sheep to withdraw
     * @return
     */
    boolean canWithdrawSheep(int numberOfSheep){
        return false;
    }

    /**
     * checks whether the bank has enough wheat to withdraw
     * @param numberOfWheat the desired number of wheat to withdraw
     * @return
     */
    boolean canWithdrawWheat(int numberOfWheat){
        return false;
    }

    /**
     * checks whether the player has enough bricks to deposit
     * @param numberOfBrick the desired number Of bricks to withdraw
     * @return
     */
    boolean canDepositBrick(int playerId, int numberOfBrick){
        return false;
    }

    /**
     *
     * @param numberOfWood
     * @return
     */
    boolean canDepositWood(int numberOfWood){
        return false;
    }

    /**
     *
     * @param numberOfOre
     * @return
     */
    boolean canDepositOre(int numberOfOre){
        return false;
    }

    /**
     *
     * @param numberOfSheep
     * @return
     */
    boolean canDepositSheep(int numberOfSheep){
        return false;
    }

    /**
     *
     * @param numberOfWheat
     * @return
     */
    boolean canDepositWheat(int numberOfWheat){
        return false;
    }

    /**
     *
     * @return
     */
    boolean canBuyDevelopmentCard(){
        return false;
    }

}
