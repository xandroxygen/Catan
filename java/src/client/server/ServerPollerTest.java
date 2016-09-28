package client.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerPollerTest {
	MockServerProxy mockProxy;
	ServerPoller poller;
	
	@Before
	public void setUp() throws Exception {
		mockProxy= new MockServerProxy();
		poller = new ServerPoller(mockProxy);
	}

	@After
	public void tearDown() throws Exception {	}

	@Test
	public void testServerPoller() {
		assertEquals(mockProxy, poller.getProxy());
	}

	@Test
	public void testStart() {
		// how to test???
		fail("Not yet implemented");
	}
	
	@Test
	public void testCheckForUpdates() {	
		// test when JSON model contains updated data
		String model1 = mockProxy.testModel1;
		boolean updatesOccurred = poller.checkForUpdates(model1);
		assertEquals(true, updatesOccurred);
		
		//test when JSON model only contains string "true" 
		String model3 = mockProxy.testModel3;
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
		// TODO: get code for updating the model
		
		// Test when version numbers do not match
		poller.setVersion(0);
		poller.pollServer();
		// String response = get response from updateModel code
		// assertEquals(mockProxy.testModel1, response);
		
		// Test after retrieving one update and another update occurred 
		poller.setVersion(1);
		poller.pollServer();
		// String response = get response from updateModel code
		// assertEquals(mockProxy.testModel1, response);
		
		// Test when version numbers do match and no updates have occurred
		// TODO: How to test this???
		poller.setVersion(3);
		poller.pollServer();
		
		
	}

	@Test
	public void testUpdateVersion() {
		fail("Not yet implemented");
	}

	@Test
	public void testStop() {
		// not necessary to test???
		fail("Not yet implemented");
	}

}
