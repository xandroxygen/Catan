package testing;

import client.model.City;
import client.model.Model;
import client.model.Player;

import client.model.Road;
import client.model.Settlement;
import client.server.MockServerProxy;
import client.server.ServerPoller;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import client.server.MockServerProxy;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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
		// Initialize model
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
		// Initialize model
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
	
	@Test
	public void testRoadPlacement() {
		System.out.println("Testing can build road");
		// Initialize model
		JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
		Model.getInstance().updateModel(newModel);	
		Model model = Model.getInstance();
		
		// Make up edge
		EdgeLocation edge = new EdgeLocation(new HexLocation(1,1),EdgeDirection.North);
		assertFalse(model.canPlaceRoad(0, true, edge));
		VertexLocation vertex = new VertexLocation(new HexLocation(1,1),VertexDirection.NorthWest);
		model.getGame().theMap.getSettlements().put(vertex, new Settlement(vertex, 0));
		assertTrue(model.canPlaceRoad(0, true, edge));
		
		// Attempt to attach to another player's settlement
		EdgeLocation edge2 = new EdgeLocation(new HexLocation(2,1),EdgeDirection.NorthEast);
		VertexLocation vertex2 = new VertexLocation(new HexLocation(2,1),VertexDirection.NorthEast);
		model.getGame().theMap.getSettlements().put(vertex2, new Settlement(vertex2, 1));
		assertFalse(model.canPlaceRoad(0, true, edge2));
		
		// Build without road
		EdgeLocation edge3 = new EdgeLocation(new HexLocation(3,1),EdgeDirection.North);
		assertFalse(model.canPlaceRoad(0, false, edge3));
		
		// Attach to player road
		model.getGame().theMap.getRoads().put(edge3, new Road(edge3,0));
		EdgeLocation edge4 = new EdgeLocation(new HexLocation(3,1),EdgeDirection.NorthEast);
		assertTrue(model.canPlaceRoad(0, false, edge4));
		
		// Attempt attach to other player road
		EdgeLocation edge5 = new EdgeLocation(new HexLocation(1,2),EdgeDirection.North);
		model.getGame().theMap.getRoads().put(edge5, new Road(edge5,1));
		EdgeLocation edge6 = new EdgeLocation(new HexLocation(1,2),EdgeDirection.NorthEast);
		assertFalse(model.canPlaceRoad(0, false, edge6));
	}
	
	@Test
	public void testTradeOffer() {
		System.out.println("Testing can make trade");
		// Initialize model
		JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
		Model.getInstance().updateModel(newModel);	
		Model model = Model.getInstance();
		
		// Send trade to player while having the necessary resources
		HashMap<ResourceType,Integer> offer = new HashMap<>();
		offer.put(ResourceType.BRICK, 1);
		offer.put(ResourceType.ORE, 1);
		offer.put(ResourceType.SHEEP, 1);
		offer.put(ResourceType.WHEAT, -1);
		offer.put(ResourceType.WOOD, 1);
		assertTrue(model.canTradeWithPlayer(0, 1, offer));
		
		// Send trade to player while NOT having the necessary resources
		HashMap<ResourceType,Integer> offer2 = new HashMap<>();
		offer2.put(ResourceType.BRICK, 1);
		offer2.put(ResourceType.ORE, 1);
		offer2.put(ResourceType.SHEEP, 1);
		offer2.put(ResourceType.WHEAT, -1);
		offer2.put(ResourceType.WOOD, 1);
		assertFalse(model.canTradeWithPlayer(1, 2, offer2));
		
		
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
        i = 0;
        devCardsInBank = new HashMap<>();
        devCardsInBank.put(DevCardType.SOLDIER, 3);
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            tempPlayer.setResources(resourceHand);
            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            i++;
        }
        //tests if there are no cards in the bank
        i = 0;
        devCardsInBank = new HashMap<>();
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            i++;
        }
    }*/
}
