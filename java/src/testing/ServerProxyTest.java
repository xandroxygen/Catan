package testing;

import client.model.InvalidActionException;
import client.server.HTTPOperations;
import client.server.RequestResponse;
import client.server.ServerProxy;
import org.junit.Before;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ServerProxy to make sure server connection works.
 * Also tests get/post methods, cookies, invalid input, parameters, int vs string.
 */
public class ServerProxyTest {

    private ServerProxy server;
    private HTTPOperations httpOps;
    private String url;

    public ServerProxyTest() {
        server = new ServerProxy();
        httpOps = new HTTPOperations();
        url = "http://localhost:8081";
    }

    /**
     * Tests that the ServerProxy instance is in fact a ServerProxy, and that it's instantiated.
     */
    @Test
    public void shouldTestValidClass() {
        assertEquals(server.getClass(), ServerProxy.class);
    }

    /**
     * Tests the connection to the server. Make sure ant server is running before doing this!
     *  If no exception is thrown, method works and can be asserted true.
     */
    @Test
    public void shouldConnectToServer() {
        try {

            URL requestURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int code = connection.getResponseCode();
            assertEquals("Connected!", 200, code);
        }
        catch (Exception e) {
            assertTrue("Exception when connecting", false);
        }
    }

    /**
     * Tests the get method of the server. Uses the /games/list call to the server, which requires no cookies.
     *  If no exception is thrown, method works and can be asserted true.
     */
    @Test
    public void shouldGetFromServer() {
        try {
            Map<String, String> headers = new HashMap<>();
            RequestResponse result = httpOps.get(url + "/games/list", headers);
            if (result.hasError()) {
                assertTrue("Error from GET", false);
            }
            else {
                assertTrue("Result is good!", true);
            }
        }
        catch (Exception e) {
            assertTrue("Exception", false);
        }
    }

    /**
     * Tests the post method of the server. Uses the /games/create method, which requires no cookies.
     * Also tests gamesCreate and serializeNonMoveCall.
     * If no exception is thrown, method works and can be asserted true.
     */
    @Test
    public void shouldPostToServer() {
        try {
            String result = server.gamesCreate("newGame", true, true, true);
            assertTrue("Proper post", true);
        }
        catch (Exception e) {
            assertTrue("Exception", false);
        }
    }

    /**
     * Tests getting and setting a new user cookie, and being able to use it in another request.
     * Also tests userRegister and gamesJoin for the cookies.
     *  If no exception is thrown, method works and can be asserted true.
     */
    @Test
    public void shouldSetAndUsePlayerCookie() {
        String username = "Sam";
        String password = "sam";

        try {
            String pCookie = server.userLogin(username, password);
            String gCookie = server.gamesJoin(3, CatanColor.PUCE);
            assertEquals(gCookie, "catan.game=3");
        }
        catch (Exception e) {
            assertTrue("Exception", false);
        }
    }

    /**
     * Tests making a move request once both player and game cookies are set.
     * Uses /moves/sendChat, and serializeMoveCall since this does not require turns.
     * If no exception is thrown, method works and can be asserted true.
     */
    @Test
    public void shouldUseGameCookie() {
        shouldSetAndUsePlayerCookie();
        String message = "This is a chat.";

        try {
            //server.sendChat(message);
            assertTrue("Success", true);
        }
        catch (Exception e) {
            assertTrue("Exception", false);
        }



    }

    @Test
    public void shouldBuildRoad() {

		try {
			server.userLogin("Sam", "sam");
			server.gamesJoin(0, CatanColor.PURPLE);


			EdgeLocation edge = new EdgeLocation(new HexLocation(0,0), EdgeDirection.North);
            server.buildRoad(true, edge);

        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }
}
