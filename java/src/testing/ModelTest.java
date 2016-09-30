package testing;

import client.model.Model;
import client.model.Player;
import client.server.MockServerProxy;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jonathan Skaggs on 9/30/2016.
 */
public class ModelTest {
	MockServerProxy mockProxy;
	ServerPoller poller;

	@Before
	public void setUp() throws Exception {
		mockProxy= new MockServerProxy();
		poller = new ServerPoller(mockProxy);
	}

	@Test
	public void testCityPlacement() {
		System.out.println("Testing can build city");
		// Initiate model
		JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
		Model.getInstance().updateModel(newModel);
		Model model = Model.getInstance();

		// Make up a Vertex Location
		VertexLocation vertex = new VertexLocation(new HexLocation(-1,0),VertexDirection.NorthWest);
		assertFalse(model.canPlaceCity(0, vertex));

		// Add settlement belonging to other player
		model.getGame().theMap.getSettlements().put(vertex, new Settlement(vertex, 1));
		assertFalse(model.canPlaceCity(0, vertex));

		// Make new Location
		VertexLocation vertex2 = new VertexLocation(new HexLocation(-2,0),VertexDirection.NorthWest);
		// Add settlement for player
		model.getGame().theMap.getSettlements().put(vertex2, new Settlement(vertex, 0));
		assertTrue(model.canPlaceCity(0, vertex2));
	}

	@Test
	public void testSettlementPlacement() {
		System.out.println("Testing can build settlement");
		// Initiate model
		JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
		Model.getInstance().updateModel(newModel);
		Model model = Model.getInstance();

		// Make up a Vertex Location
		VertexLocation vertex = new VertexLocation(new HexLocation(-2,0),VertexDirection.NorthWest);
		assertTrue(model.canPlaceSettlement(0, true, vertex));

		// Add settlement belonging to other player
		model.getGame().theMap.getSettlements().put(vertex, new Settlement(vertex, 1));
		assertFalse(model.canPlaceSettlement(0, true, vertex));

		// Attempt to build adjacent settlement
		VertexLocation vertex2 = new VertexLocation(new HexLocation(-2,0),VertexDirection.NorthEast);
		assertFalse(model.canPlaceSettlement(0, true, vertex2));

		// Add a road to build off of
		EdgeLocation edge = new EdgeLocation(new HexLocation(1,1),EdgeDirection.North);
		VertexLocation vertex3 = new VertexLocation(new HexLocation(1,1),VertexDirection.NorthWest);
		model.getGame().theMap.getRoads().put(edge, new Road(edge,0));
		assertTrue(model.canPlaceSettlement(0, false, vertex3));
	}




    /*@Test
    public void canBuyDevelopmentCard() throws Exception {
        MockServerProxy mockProxy = new MockServerProxy();
        JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
        Model.getInstance().updateModel(newModel);

        //all prerequisites are met/ testing if it is not the players turn
        int i = 0;
        HashMap<DevCardType, Integer> devCardsInBank = new HashMap<>();
        devCardsInBank.put(DevCardType.SOLDIER, 3);
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
            if(i == Model.getInstance().getGame().getCurrentTurnIndex()){
                assertTrue(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            } else {
                assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            }
            i++;
        }
        //all prerequisites are met except their resources
        devCardsInBank = new HashMap<>();
        devCardsInBank.put(DevCardType.SOLDIER, 3);
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            tempPlayer.setResources(resourceHand);
            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
        }
        //tests if there are no cards in the bank
        devCardsInBank = new HashMap<>();
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
        }
    }
//*         It's your turn
//* 		The client model status is 'Playing'
//* 		You have the specific card you want to play in your old dev card hand
//* 		You have not yet played a non­monument dev card this turn
//* 		The first road location (spot1) is connected to one of your roads.
//* 		The second road location (spot2) is connected to one of your roads or to the first road location (spot1)
//* 		Neither road location is on water
//* 		You have at least two unused roads

    @Test
    public void canPlayYearOfPlenty() throws Exception{
        MockServerProxy mockProxy = new MockServerProxy();
        JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
        Model.getInstance().updateModel(newModel);
        //all requirements are met / testing is players turn
        int i = 0;
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.YEAR_OF_PLENTY, 1);
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            //add stuff here
            if (i == Model.getInstance().getGame().getCurrentTurnIndex()) {
                assertTrue(Model.getInstance().canPlayYearOfPlenty(tempPlayer.getPlayerId(), ResourceType.BRICK, ResourceType.ORE));
            } else {
                assertFalse(Model.getInstance().canPlayYearOfPlenty(tempPlayer.getPlayerId(), ResourceType.BRICK, ResourceType.ORE));
            }
            i++;
        }
    }*/
        //testing if GameStatus in not Playing
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.FirstRound);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.YEAR_OF_PLENTY, 1);
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            //add stuff here
            assertFalse(Model.getInstance().canPlayYearOfPlenty(tempPlayer.getPlayerId(), ResourceType.BRICK, ResourceType.ORE));
        }
        //testing if there is no Year of Plenty Dev Card
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            //add stuff here
            assertFalse(Model.getInstance().canPlayYearOfPlenty(tempPlayer.getPlayerId(), ResourceType.BRICK, ResourceType.ORE));
        }
        //testing if there has already been a Dev Card Played
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.YEAR_OF_PLENTY, 1);
            tempPlayer.setPlayedDevCard(true);
            tempPlayer.setOldDevCards(oldDevCards);
            //add stuff here
            assertFalse(Model.getInstance().canPlayYearOfPlenty(tempPlayer.getPlayerId(), ResourceType.BRICK, ResourceType.ORE));
        }
    }

//    @Test
//    public void canPlayRoadCard() throws Exception{
//        MockServerProxy mockProxy = new MockServerProxy();
//        JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
//        Model.getInstance().updateModel(newModel);
//        //all requirements are met / testing is players turn
//        int i = 0;
//        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
//        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
//            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
//            oldDevCards.put(DevCardType.ROAD_BUILD, 1);
//            tempPlayer.setPlayedDevCard(false);
//            tempPlayer.setOldDevCards(oldDevCards);
//            //add stuff here
//            if (i == Model.getInstance().getGame().getCurrentTurnIndex()) {
//                assertTrue(Model.getInstance().canPlayRoadCard(tempPlayer.getPlayerId(), ));
//            } else {
//                assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
//            }
//            i++;
//        }
//        //testing if GameStatus in not Playing
//        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.FirstRound);
//        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
//            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
//            oldDevCards.put(DevCardType.ROAD_BUILD, 1);
//            tempPlayer.setPlayedDevCard(false);
//            tempPlayer.setOldDevCards(oldDevCards);
//            //add stuff here
//            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
//        }
//        //testing if there is no ROAD_BUILD
//        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
//        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
//            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
//            tempPlayer.setPlayedDevCard(false);
//            tempPlayer.setOldDevCards(oldDevCards);
//            //add stuff here
//            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
//        }
//        //testing if there has already been a Dev Card Played
//        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
//        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
//            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
//            oldDevCards.put(DevCardType.ROAD_BUILD, 1);
//            tempPlayer.setPlayedDevCard(true);
//            tempPlayer.setOldDevCards(oldDevCards);
//            //add stuff here
//            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
//        }
//    }

    @Test
    public void canPlayMonopolyCard() throws Exception{
        MockServerProxy mockProxy = new MockServerProxy();
        JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
        Model.getInstance().updateModel(newModel);
        //all requirements are met / testing is players turn
        int i = 0;
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.MONOPOLY, 1);
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            if (i == Model.getInstance().getGame().getCurrentTurnIndex()) {
                assertTrue(Model.getInstance().canPlayMonopolyCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
            } else {
                assertFalse(Model.getInstance().canPlayMonopolyCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
            }
            i++;
        }
        //testing if GameStatus in not Playing
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.FirstRound);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.MONOPOLY, 1);
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            assertFalse(Model.getInstance().canPlayMonopolyCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
        }
        //testing if there is no Year of Plenty Dev Card
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            assertFalse(Model.getInstance().canPlayMonopolyCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
        }
        //testing if there has already been a Dev Card Played
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.MONOPOLY, 1);
            tempPlayer.setPlayedDevCard(true);
            tempPlayer.setOldDevCards(oldDevCards);
            assertFalse(Model.getInstance().canPlayMonopolyCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
        }
    }

    @Test
    public void canPlayMonumentCard() throws Exception{
        MockServerProxy mockProxy = new MockServerProxy();
        JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
        Model.getInstance().updateModel(newModel);
        //all requirements are met / testing is players turn
        int i = 0;
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.MONOPOLY, 1);
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            if (i == Model.getInstance().getGame().getCurrentTurnIndex()) {
                assertTrue(Model.getInstance().canPlayMonumentCard(tempPlayer.getPlayerId()));
            } else {
                assertFalse(Model.getInstance().canPlayMonumentCard(tempPlayer.getPlayerId()));
            }
            i++;
        }
        //testing if GameStatus in not Playing
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.FirstRound);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.MONOPOLY, 1);
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            assertFalse(Model.getInstance().canPlayMonumentCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
        }
        //testing if there is no Year of Plenty Dev Card
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            tempPlayer.setPlayedDevCard(false);
            tempPlayer.setOldDevCards(oldDevCards);
            assertFalse(Model.getInstance().canPlayMonumentCard(tempPlayer.getPlayerId(), ResourceType.BRICK));
        }
        //testing if there has already been a Dev Card Played
        Model.getInstance().getGame().getTurnTracker().setStatus(GameStatus.Playing);
        for(Player tempPlayer: Model.getInstance().getGame().playerList) {
            HashMap<DevCardType, Integer> oldDevCards = new HashMap<>();
            oldDevCards.put(DevCardType.MONOPOLY, 1);
            tempPlayer.setPlayedDevCard(true);
            tempPlayer.setOldDevCards(oldDevCards);
            assertFalse(Model.getInstance().canPlayMonumentCard(tempPlayer.getPlayerId()));
        }
    }
}
