package testing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import client.admin.GameAdministrator;
import client.admin.GameDetails;
import client.admin.PlayerDetails;
import client.admin.User;
import client.model.InvalidActionException;
import client.model.Model;
import client.server.MockServerProxy;
import client.server.ServerPoller;

public class GameAdministratorTest {
	MockServerProxy mockProxy;
	ServerPoller poller;
	Model model;
	GameAdministrator gameAdmin;
	
	@Before
	public void setUp() throws Exception {
		mockProxy= new MockServerProxy();
		poller = new ServerPoller(mockProxy);	
		User user = new User();
		gameAdmin = new GameAdministrator(user, mockProxy);
		
		// initialize model with data
		model = Model.getInstance();
		JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
		model.updateModel(newModel);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCanLogin() {
		System.out.println("Testing canLogin");
		boolean canLogin;
		
		String username = "Sam";
		String password = "sam";
		canLogin = gameAdmin.canLogin(username, password);
		assertEquals(true, canLogin);
		
		username = null;
		password = null;
		canLogin = gameAdmin.canLogin(username, password);
		assertEquals(false, canLogin);		
	}
	
	@Test
	public void testCanResgister() throws InvalidActionException {
		boolean canRegister;
		
		System.out.println("Testing canRegister");
		
		// try to register with null username and password
		String username = null;
		String password = null;
		canRegister = gameAdmin.canRegister(username, password);
		assertEquals(false, canRegister);
		
		// try to register with a username that has been taken
		username = "Sam";
		password = "sam";
		canRegister = gameAdmin.canRegister(username, password);
		assertEquals(false, canRegister);
		
		// try to register with a username that hasn't been taken
		username = "Spencer";
		password = "byu";
		canRegister = gameAdmin.canRegister(username, password);
		assertEquals(true, canRegister);
	}
	
	@Test
	public void testCanCreateGame() {
		System.out.println("Testing canCreateGame");
		
		// try to create game with null game name
		String gameName = null;
		boolean canCreateGame = gameAdmin.canCreateGame(gameName, true, true, true);
		assertEquals(false, canCreateGame);
		
		
	}
	
	@Test
	public void testCanJoinGame() {
		fail("Not yet implemented");
	}

}
