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

import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

import static org.junit.Assert.*;

import java.util.HashMap;

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
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            Model.getInstance().getGame().getBank().;
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
        }
    }*/
}
