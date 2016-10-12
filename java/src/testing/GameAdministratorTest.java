package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.admin.GameAdministrator;
import client.admin.User;
import client.model.InvalidActionException;
import client.model.Model;
import client.server.MockServerProxy;
import client.server.ServerPoller;
import shared.definitions.CatanColor;

public class GameAdministratorTest {
	MockServerProxy mockProxy;
	ServerPoller poller;
	Model model;
	GameAdministrator gameAdmin;
	User user = new User();

	@Before
	public void setUp() throws Exception {
		mockProxy= new MockServerProxy();
		poller = new ServerPoller(mockProxy);
		model = Model.getInstance();
		gameAdmin = GameAdministrator.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCanLogin() {
		System.out.println("Testing canLogin");
		
		boolean canLogin = false;
		String username = null;
		String password = null;
		canLogin = gameAdmin.canLogin(username, password);
		assertEquals(false, canLogin);
		
		username = "Sam";
		password="sam";
		canLogin = gameAdmin.canLogin(username, password);
		assertEquals(true, canLogin);
	}
	
	@Test
	public void testCanRegister() throws InvalidActionException {
		System.out.println("Testing canRegister");
		
		boolean canRegister = false;
		String username = null;
		String password = null;
		canRegister = gameAdmin.canRegister(username, password);
		assertEquals(false, canRegister);
		
		// try with name that is already taken
		username = "Sam";
		password="sam";
		canRegister = gameAdmin.canRegister(username, password);
		assertEquals(false, canRegister);
		
		// try with names that are already taken
		username = "Spencer";
		password="byu";
		canRegister = gameAdmin.canRegister(username, password);
		assertEquals(true, canRegister);
		
		// make check it is case-insensitive
		username = "SAM";
		password="sam";
		canRegister=gameAdmin.canRegister(username, password);
		assertEquals(true, canRegister);
	}
	
	@Test
	public void testCanCreateGame() {
		System.out.println("Testing canCreateGame");
		boolean canCreateGame;
		
		// test with valid input
		String gameName = "My Game";
		user.isLoggedIn = true;
		canCreateGame = gameAdmin.canCreateGame(gameName, false, false, true);
		assertEquals(true, canCreateGame);
		
		// test with null game name string
		gameName = null;
		canCreateGame = gameAdmin.canCreateGame(gameName, true, false, true);
		assertEquals(false, canCreateGame);
		
		// test when user is not logged in
		gameName = "My Game";
		user.isLoggedIn = false;
		canCreateGame = gameAdmin.canCreateGame(gameName, false, true, false);
		assertEquals(false, canCreateGame);
	}
	
	@Test
	public void testCanJoinGame() throws InvalidActionException {
		System.out.println("Testing canJoinGame");
		boolean canJoinGame;
		
		// test joining a game that is already full
		user.isLoggedIn = true;
		canJoinGame = gameAdmin.canJoinGame(0, CatanColor.PUCE);
		assertEquals(false, canJoinGame);
		
		// test when user is not logged in
		user.isLoggedIn = false;
		canJoinGame = gameAdmin.canJoinGame(2, CatanColor.RED);
		assertEquals(false, canJoinGame);
		
		// test with color that has already been taken in a game that is not full
		user.isLoggedIn = true;
		canJoinGame = gameAdmin.canJoinGame(2, CatanColor.BLUE);
		assertEquals(false, canJoinGame);
		
		// test with valid input and a game that is not already full
		canJoinGame = gameAdmin.canJoinGame(2, CatanColor.PUCE);
		assertEquals(true, canJoinGame);
	}

	@Test
	public void testLogin() throws InvalidActionException {
		String username = "Sam";
		String password = "sam";

		gameAdmin.login(username, password);
	}
}
