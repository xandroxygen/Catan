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

public class GameAdministratorTest {
	MockServerProxy mockProxy;
	ServerPoller poller;
	Model model;
	GameAdministrator gameAdmin;

	@Before
	public void setUp() throws Exception {
		mockProxy= new MockServerProxy();
		poller = new ServerPoller(mockProxy);
		model = Model.getInstance();
		User user = new User();
		gameAdmin = new GameAdministrator(user, mockProxy);
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
		fail("Not yet implemented");
	}
	
	@Test
	public void testCanJoinGame() {
		fail("Not yet implemented");
	}

}
