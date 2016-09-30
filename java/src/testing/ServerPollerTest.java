package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import client.model.Model;
import client.server.MockServerProxy;
import client.server.ServerPoller;

public class ServerPollerTest {
	MockServerProxy mockProxy;
	ServerPoller poller;
	Model model;
	
	@Before
	public void setUp() throws Exception {
		mockProxy= new MockServerProxy();
		poller = new ServerPoller(mockProxy);
		model = Model.getInstance();
	}

	@After
	public void tearDown() throws Exception {	}

	@Test
	public void testServerPoller() {
		System.out.println("Testing ServerPoller");
		
		assertEquals(mockProxy, poller.getProxy());
	}
	
	@Test
	public void testCheckForUpdates() {	
		System.out.println("Testing checkForUpdates");
		
		// test when JSON model contains updated data
		String model1 = mockProxy.testModel1;
		boolean updatesOccurred = poller.checkForUpdates(model1);
		assertEquals(true, updatesOccurred);
		
		//test when JSON model only contains string "true" 
		String model3 = mockProxy.testModel4;
		updatesOccurred = poller.checkForUpdates(model3);
		assertEquals(false, updatesOccurred);
	}

	/**
	 * Check whether Server Poller can retrieve response from server, check for updates, 
	 * and update the model if necessary.
	 * 
	 * Code for testing whether the model is updated correctly is 
	 */
	@Test
	public void testPollServer() {
		System.out.println("Testing pollServer");
		
		// initialize model with data, check that model has been updated 
		JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
		model.updateModel(newModel);
		model.getGame().version = 1;
		
		//updates occurred, server poller should recognize and update the model
		poller.pollServer();	
		assertEquals(2, model.getVersion());
		
		//after updating the model once, does the poller correctly recognize and update a second time
		poller.pollServer();	
		assertEquals(3, model.getVersion());
		
		//does poller correctly recognize when the server model has not been changed?
		model.getGame().version = 4;
		poller.pollServer();	
		assertEquals("James", model.getGame().getPlayerList().get(0).getName());
	}

}
