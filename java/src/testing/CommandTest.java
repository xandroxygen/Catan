package testing;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.command.moves.AcceptTradeCommand;
import server.command.moves.BuildCityCommand;
import server.command.moves.BuildRoadCommand;
import server.command.moves.BuildSettlementCommand;
import server.command.moves.BuyDevCardCommand;
import server.command.moves.DiscardCardsCommand;
import server.command.moves.FinishTurnCommand;
import server.command.moves.MaritimeTradeCommand;
import server.command.moves.MonopolyCommand;
import server.command.moves.MonumentCommand;
import server.command.moves.OfferTradeCommand;
import server.command.moves.RoadBuildingCommand;
import server.command.moves.RobPlayerCommand;
import server.command.moves.RollNumberCommand;
import server.command.moves.SendChatCommand;
import server.command.moves.SoldierCommand;
import server.command.moves.YearOfPlentyCommand;
import server.facade.MockServerFacade;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.InvalidActionException;

public class CommandTest {
	MockServerFacade serverFacade;
	AcceptTradeCommand acceptTradeCommand1;
	AcceptTradeCommand acceptTradeCommand2;
	BuildCityCommand buildCityCommand;
	BuildRoadCommand buildRoadCommand1;
	BuildRoadCommand buildRoadCommand2;
	BuildSettlementCommand buildSettlementCommand1;
	BuildSettlementCommand buildSettlementCommand2;
	BuyDevCardCommand buyDevCommand1;
	BuyDevCardCommand buyDevCommand2;
	DiscardCardsCommand discardCommand1;
	FinishTurnCommand finishTurnCommand;
	MaritimeTradeCommand maritimeCommand;
	MonopolyCommand monopolyCommand;
	MonumentCommand monumentCommand;
	OfferTradeCommand offerTradeCommand1;
	OfferTradeCommand offerTradeCommand2;
	RoadBuildingCommand roadBuildingCommand;
	RobPlayerCommand robPlayerCommand;
	RollNumberCommand rollCommand;
	SendChatCommand sendChatCommand;
	SoldierCommand soldierCardCommand;
	YearOfPlentyCommand yearOfPlentyCommand;
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Before
	public void setUpBeforeClass() throws Exception {
		serverFacade = new MockServerFacade();
		
		acceptTradeCommand1 = new AcceptTradeCommand(serverFacade, 0, false);
		acceptTradeCommand2 = new AcceptTradeCommand(serverFacade, 0, true);
		
		VertexLocation vertexLoc = new VertexLocation(new HexLocation(0,0), VertexDirection.East);
		buildCityCommand = new BuildCityCommand(serverFacade, 0, 0, vertexLoc, false);
		
		HexLocation hexLoc = new HexLocation(0,0);
		EdgeLocation edgeLoc = new EdgeLocation(hexLoc, EdgeDirection.North);
		buildRoadCommand1 = new BuildRoadCommand(serverFacade, 0, 0, edgeLoc, false);
		buildRoadCommand2 = new BuildRoadCommand(serverFacade, 0, 0, edgeLoc, true);
	
		buildSettlementCommand1 = new BuildSettlementCommand(serverFacade, 0, 0, vertexLoc, false);
		buildSettlementCommand2 = new BuildSettlementCommand(serverFacade, 0, 0, vertexLoc, true);
		
		buyDevCommand1 = new BuyDevCardCommand(serverFacade, 0, 0);
		buyDevCommand2 = new BuyDevCardCommand(serverFacade, 0, 1);
		
		HashMap<ResourceType, Integer> hand = new HashMap<ResourceType, Integer>();
		hand.put(ResourceType.BRICK, 5);
		discardCommand1 = new DiscardCardsCommand(serverFacade, 0, 0, hand);
		
		finishTurnCommand = new FinishTurnCommand(0, serverFacade);
		
		maritimeCommand = new MaritimeTradeCommand(serverFacade, 0, 0, 0, ResourceType.BRICK, ResourceType.WOOD);
		
		monopolyCommand = new MonopolyCommand(serverFacade, 0, 0, ResourceType.SHEEP);
		
		monumentCommand = new MonumentCommand(serverFacade, 0, 0);
		
		offerTradeCommand1 = new OfferTradeCommand(serverFacade, 0, 0, 0, hand);
		
		roadBuildingCommand = new RoadBuildingCommand(serverFacade, 0, 0, edgeLoc, edgeLoc);
	
		robPlayerCommand = new RobPlayerCommand(serverFacade, 0, 0, 0, hexLoc);
		
		rollCommand = new RollNumberCommand(serverFacade, 0, 0 , 7);
		
		sendChatCommand = new SendChatCommand(serverFacade, 0, 0, "hello");
		
		soldierCardCommand = new SoldierCommand(serverFacade, 0, 0, hexLoc, 0);
		
		yearOfPlentyCommand = new YearOfPlentyCommand(serverFacade, 0, 0, ResourceType.BRICK, ResourceType.WOOD);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAcceptTrade() {
		try {
			assertEquals(false, acceptTradeCommand1.execute());
			
			assertEquals(true, acceptTradeCommand2.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBuildCity() {
		try {
			assertEquals(VertexDirection.East, buildCityCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBuildRoad() {
		try {
			assertEquals(false, buildRoadCommand1.execute());
			assertEquals(true, buildRoadCommand2.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBuildSettlement() {
		try {
			assertEquals(false, buildSettlementCommand1.execute());
			assertEquals(true, buildSettlementCommand2.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBuyDevCard() {
		try {
			assertEquals(0, buyDevCommand1.execute());
			assertEquals(1, buyDevCommand2.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDiscardCards() {
		try {
			assertEquals(5, discardCommand1.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFinishTurn() {
		try {
			assertEquals(0, finishTurnCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMaritimeTrade() {
		try {
			assertEquals(ResourceType.BRICK, maritimeCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPlayMonopoly() {
		try {
			assertEquals(ResourceType.SHEEP, monopolyCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPlayMonument() {
		try {
			assertEquals(0, monumentCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfferTrade() {
		try {
			assertEquals(5, offerTradeCommand1.execute());
			
			// test amount receiving
			HashMap<ResourceType, Integer> hand = new HashMap<ResourceType, Integer>();
			hand.put(ResourceType.BRICK, -1);
			offerTradeCommand2 = new OfferTradeCommand(serverFacade, 0, 0, 0, hand);
			
			assertEquals(-1, offerTradeCommand2.execute());
			
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRoadBuilding() {
		try {
			assertEquals(0, roadBuildingCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRobPlayer() {
		try {
			assertEquals(0, robPlayerCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRollNumber() {
		try {
			assertEquals(7, rollCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSendChat() {
		try {
			assertEquals("hello", sendChatCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPlaySoldier() {
		try {
			assertEquals(0, soldierCardCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPlayYearOfPlenty() {
		try {
			assertEquals(ResourceType.BRICK, yearOfPlentyCommand.execute());
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}
}
