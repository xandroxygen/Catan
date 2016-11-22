package testing;

import org.junit.Before;
import org.junit.Test;
import server.command.moves.RollNumberCommand;
import server.facade.ServerFacade;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.locations.*;
import shared.model.DevCard;
import shared.model.Game;
import shared.model.Road;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by jbskaggs on 11/21/16.
 */
public class ServerModelTest {
    ServerFacade serverFacade = new ServerFacade();

    @Before
    public void setUp() throws Exception {
        try {
            serverFacade.userRegister("Jonathan", "jonathan");
            serverFacade.userRegister("Xander", "xander");
            serverFacade.userRegister("Spencer", "spencer");
            serverFacade.userRegister("Jeremy", "jeremy");

            serverFacade.userLogin("Jonathan", "jonathan");
            serverFacade.userLogin("Xander", "xander");
            serverFacade.userLogin("Spencer", "spencer");
            serverFacade.userLogin("Jeremy", "jeremy");

            serverFacade.gamesCreate("Game", false, false, false);

            serverFacade.gamesJoin(0, 0, CatanColor.RED);
            serverFacade.gamesJoin(0, 1, CatanColor.ORANGE);
            serverFacade.gamesJoin(0, 2, CatanColor.BLUE);
            serverFacade.gamesJoin(0, 3, CatanColor.GREEN);

            //firstRound
            serverFacade.buildRoad(0, 0, true, new EdgeLocation(new HexLocation(0,0), EdgeDirection.North));
            serverFacade.buildSettlement(0, 0, true, new VertexLocation(new HexLocation(0,0), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            serverFacade.buildRoad(0, 1, true, new EdgeLocation(new HexLocation(-1,0), EdgeDirection.North));
            serverFacade.buildSettlement(0, 1, true, new VertexLocation(new HexLocation(-1,0), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            serverFacade.buildRoad(0, 2, true, new EdgeLocation(new HexLocation(-2,0), EdgeDirection.North));
            serverFacade.buildSettlement(0, 2, true, new VertexLocation(new HexLocation(-2,0), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            serverFacade.buildRoad(0, 3, true, new EdgeLocation(new HexLocation(1,0), EdgeDirection.North));
            serverFacade.buildSettlement(0, 3, true, new VertexLocation(new HexLocation(1,0), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            //secondRound
            serverFacade.buildRoad(0, 3, true, new EdgeLocation(new HexLocation(2,0), EdgeDirection.North));
            serverFacade.buildSettlement(0, 3, true, new VertexLocation(new HexLocation(2,0), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            serverFacade.buildRoad(0, 2, true, new EdgeLocation(new HexLocation(0,-1), EdgeDirection.North));
            serverFacade.buildSettlement(0, 2, true, new VertexLocation(new HexLocation(0,-1), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            serverFacade.buildRoad(0, 1, true, new EdgeLocation(new HexLocation(0,1), EdgeDirection.North));
            serverFacade.buildSettlement(0, 1, true, new VertexLocation(new HexLocation(0,1), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);

            serverFacade.buildRoad(0, 0, true, new EdgeLocation(new HexLocation(0,-2), EdgeDirection.North));
            serverFacade.buildSettlement(0, 0, true, new VertexLocation(new HexLocation(0,-2), VertexDirection.NorthWest));
            serverFacade.finishTurn(0);
        } catch (Exception e){}

    }

    @Test
    public void placeCity() throws Exception {

    }

    @Test
    public void placeSettlement() throws Exception {

    }

    @Test
    public void placeRoad() throws Exception {

    }

    @Test
    public void buyDevelopmentCard() throws Exception {
        Game game = serverFacade.getModel().getGames(0);
        for (int i = 0; i < 4; i++) {
            HashMap<DevCardType, Integer> oldDevCards, newDevCards;
            oldDevCards = game.getPlayerList().get(i).getOldDevCards();
            newDevCards = game.getPlayerList().get(i).getNewDevCards();

            assertEquals((int)oldDevCards.get(DevCardType.SOLDIER), 0);
            assertEquals((int)oldDevCards.get(DevCardType.YEAR_OF_PLENTY), 0);
            assertEquals((int)oldDevCards.get(DevCardType.MONOPOLY), 0);
            assertEquals((int)oldDevCards.get(DevCardType.ROAD_BUILD), 0);
            assertEquals((int)oldDevCards.get(DevCardType.MONUMENT), 0);

            assertEquals((int)newDevCards.get(DevCardType.SOLDIER), 0);
            assertEquals((int)newDevCards.get(DevCardType.YEAR_OF_PLENTY), 0);
            assertEquals((int)newDevCards.get(DevCardType.MONOPOLY), 0);
            assertEquals((int)newDevCards.get(DevCardType.ROAD_BUILD), 0);
            assertEquals((int)newDevCards.get(DevCardType.MONUMENT), 0);

            assertFalse(game.getPlayerList().get(i).isPlayedDevCard());

            serverFacade.buyDevCard(0, i);

            assertEquals((int)oldDevCards.get(DevCardType.SOLDIER), 0);
            assertEquals((int)oldDevCards.get(DevCardType.YEAR_OF_PLENTY), 0);
            assertEquals((int)oldDevCards.get(DevCardType.MONOPOLY), 0);
            assertEquals((int)oldDevCards.get(DevCardType.ROAD_BUILD), 0);

            assertEquals((int)newDevCards.get(DevCardType.MONUMENT), 0);

            assertEquals(oldDevCards.get(DevCardType.MONUMENT) + newDevCards.get(DevCardType.SOLDIER) +
                    newDevCards.get(DevCardType.YEAR_OF_PLENTY) + newDevCards.get(DevCardType.MONOPOLY) +
                    newDevCards.get(DevCardType.ROAD_BUILD), 1);

            assertFalse(game.getPlayerList().get(i).isPlayedDevCard());
            serverFacade.finishTurn(0);
        }
    }

    @Test
    public void playSoldierCard() throws Exception {
        Game game = serverFacade.getModel().getGames(0);
        for (int i = 0; i < 4; i++) {
            int number_of_soldier_cards = game.getPlayerList().get(i).getOldDevCards().get(DevCardType.SOLDIER);
            game.getPlayerList().get(i).getOldDevCards().put(DevCardType.SOLDIER, 1);
            int player_resources  = game.getPlayerList().get(i).getTotalOfResources();
            if(i == 0){
                int victim_resources  = game.getPlayerList().get(3).getTotalOfResources();
                serverFacade.playSoldier(0, i, new HexLocation(0,0), 3);
                if(game.getPlayerList().get(3).getTotalOfResources() != 0) {
                    assertEquals(player_resources + 1, game.getPlayerList().get(i).getTotalOfResources());
                    assertEquals(victim_resources - 1, game.getPlayerList().get(3).getTotalOfResources());
                } else{
                    assertEquals(player_resources, game.getPlayerList().get(i).getTotalOfResources());
                    assertEquals(victim_resources, game.getPlayerList().get(3).getTotalOfResources());
                }
            }
            else if(i == 1){
                int victim_resources  = game.getPlayerList().get(0).getTotalOfResources();
                serverFacade.playSoldier(0, i, new HexLocation(0,-2), 0);
                if(game.getPlayerList().get(1).getTotalOfResources() != 0) {
                    assertEquals(player_resources + 1, game.getPlayerList().get(1).getTotalOfResources());
                    assertEquals(victim_resources - 1, game.getPlayerList().get(0).getTotalOfResources());
                } else{
                    assertEquals(player_resources, game.getPlayerList().get(i).getTotalOfResources());
                    assertEquals(victim_resources, game.getPlayerList().get(0).getTotalOfResources());
                }
            }
            else if(i == 2){
                int player_zero_resources  = game.getPlayerList().get(0).getTotalOfResources();
                int player_one_resources  = game.getPlayerList().get(1).getTotalOfResources();
                int player_three_resources  = game.getPlayerList().get(3).getTotalOfResources();
                serverFacade.playSoldier(0, i, new HexLocation(2,0), -1);
                assertEquals(player_resources, game.getPlayerList().get(i).getTotalOfResources());
                assertEquals(player_zero_resources, game.getPlayerList().get(0).getTotalOfResources());
                assertEquals(player_one_resources, game.getPlayerList().get(1).getTotalOfResources());
                assertEquals(player_three_resources, game.getPlayerList().get(3).getTotalOfResources());
            }
            else{
                int victim_resources  = game.getPlayerList().get(2).getTotalOfResources();
                serverFacade.playSoldier(0, i, new HexLocation(1,1), 2);
                if(game.getPlayerList().get(2).getTotalOfResources() != 0) {
                    assertEquals(player_resources + 1, game.getPlayerList().get(i).getTotalOfResources());
                    assertEquals(victim_resources - 1, game.getPlayerList().get(2).getTotalOfResources());
                } else{
                    assertEquals(player_resources, game.getPlayerList().get(i).getTotalOfResources());
                    assertEquals(victim_resources, game.getPlayerList().get(2).getTotalOfResources());
                }
            }
            assertEquals(number_of_soldier_cards, 0);
            serverFacade.finishTurn(0);
        }
    }

    @Test
    public void playYearOfPlenty() throws Exception {
        Game game = serverFacade.getModel().getGames(0);
        for (int i = 0; i < 4; i++) {
            int number_of_YearOfPlenty_cards = game.getPlayerList().get(i).getOldDevCards().get(DevCardType.YEAR_OF_PLENTY);
            game.getPlayerList().get(i).getOldDevCards().put(DevCardType.YEAR_OF_PLENTY, 1);
            ResourceType ResourceTypeOne, ResourceTypeTwo;
            if(i == 0){
                ResourceTypeOne = ResourceType.BRICK;
                ResourceTypeTwo = ResourceType.ORE;
            }
            else if(i == 1){
                ResourceTypeOne = ResourceType.SHEEP;
                ResourceTypeTwo = ResourceType.BRICK;
            }
            else if(i == 2){
                ResourceTypeOne = ResourceType.WHEAT;
                ResourceTypeTwo = ResourceType.WOOD;
            }
            else{
                ResourceTypeOne = ResourceType.BRICK;
                ResourceTypeTwo = ResourceType.BRICK;
            }
            int NumberOfResourceTypeOne = game.getPlayerList().get(i).getResources().get(ResourceTypeOne);
            int NumberOfResourceTypeTwo = game.getPlayerList().get(i).getResources().get(ResourceTypeTwo);
            serverFacade.playYearOfPlenty(0, i, ResourceTypeOne, ResourceTypeTwo);

            if (ResourceTypeOne == ResourceTypeTwo){
                assertEquals(NumberOfResourceTypeOne + 2, (int)game.getPlayerList().get(i).getResources().get(ResourceTypeOne));
            } else{
                assertEquals(NumberOfResourceTypeOne + 1, (int)game.getPlayerList().get(i).getResources().get(ResourceTypeOne));
                assertEquals(NumberOfResourceTypeTwo + 1, (int)game.getPlayerList().get(i).getResources().get(ResourceTypeTwo));
            }
            assertEquals(number_of_YearOfPlenty_cards, 0);
            serverFacade.finishTurn(0);
        }
    }

    @Test
    public void playRoadCard() throws Exception {
        Game game = serverFacade.getModel().getGames(0);
        int number_of_RoadCard_cards = game.getPlayerList().get(0).getOldDevCards().get(DevCardType.ROAD_BUILD);
        game.getPlayerList().get(0).getOldDevCards().put(DevCardType.ROAD_BUILD, 1);
        Road roadOne = game.getTheMap().getRoads().remove(new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthEast));
        Road roadTwo = game.getTheMap().getRoads().remove(new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthWest));
        assertNull(roadOne);
        assertNull(roadTwo);
        serverFacade.playRoadBuilding(0,0, new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthEast),
                new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthWest));
        roadOne = game.getTheMap().getRoads().get(new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthEast));
        roadTwo = game.getTheMap().getRoads().get(new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthWest));
        assertNotNull(roadOne);
        assertNotNull(roadTwo);
        assertEquals(number_of_RoadCard_cards, 0);
        serverFacade.finishTurn(0);
    }

    @Test
    public void playMonopolyCard() throws Exception {
        Game game = serverFacade.getModel().getGames(0);
        for (int i = 0; i < 4; i++) {
            int number_of_YearOfPlenty_cards = game.getPlayerList().get(i).getOldDevCards().get(DevCardType.YEAR_OF_PLENTY);
            game.getPlayerList().get(i).getOldDevCards().put(DevCardType.YEAR_OF_PLENTY, 1);
            ResourceType ResourceTypeOne;
            if(i == 0){
                ResourceTypeOne = ResourceType.BRICK;
            }
            else if(i == 1){
                ResourceTypeOne = ResourceType.SHEEP;
            }
            else if(i == 2){
                ResourceTypeOne = ResourceType.WHEAT;
            }
            else{
                ResourceTypeOne = ResourceType.ORE;
            }
            int NumberOfResourceTypeOne = game.getPlayerList().get(0).getResources().get(ResourceTypeOne) +
                    game.getPlayerList().get(1).getResources().get(ResourceTypeOne) +
                    game.getPlayerList().get(2).getResources().get(ResourceTypeOne) +
                    game.getPlayerList().get(3).getResources().get(ResourceTypeOne);
            serverFacade.playMonopoly(0, i, ResourceTypeOne);


            assertEquals(NumberOfResourceTypeOne, (int)game.getPlayerList().get(i).getResources().get(ResourceTypeOne));
            if(i != 0){
                assertEquals(0, (int)game.getPlayerList().get(0).getResources().get(ResourceTypeOne));
            }
            if(i != 1){
                assertEquals(0, (int)game.getPlayerList().get(1).getResources().get(ResourceTypeOne));
            }
            if(i != 2){
                assertEquals(0, (int)game.getPlayerList().get(2).getResources().get(ResourceTypeOne));
            }
            if(i != 3){
                assertEquals(0, (int)game.getPlayerList().get(3).getResources().get(ResourceTypeOne));
            }

            assertEquals(number_of_YearOfPlenty_cards, 0);
            serverFacade.finishTurn(0);
        }
    }

    @Test
    public void playMonumentCard() throws Exception {

    }

    @Test
    public void rollDice() throws Exception {

    }

    @Test
    public void sendMessage() throws Exception {

    }

    @Test
    public void makeTradeOffer() throws Exception {

    }

    @Test
    public void acceptTradeOffer() throws Exception {

    }

    @Test
    public void makeMaritimeTrade() throws Exception {

    }


    @Test
    public void robPlayer() throws Exception {

    }

    @Test
    public void discardCards() throws Exception {

    }

    @Test
    public void createGame() throws Exception {

    }

}