package client.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import client.model.InvalidActionException;
import client.model.ModelUpdater;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Polls the server to check whether a new model exists. If a new model exists, the ServerPoller sends
 * the new data to the Model to update itself.
 * This class runs on its own thread, extends Timer and uses a TimerTask to stay up to date.
 */

public class ServerPoller {
	
	/**
	 * The number of milliseconds at which to poll the server for updates.
	 */
	private final long POLL_INTERVAL = 1000;
	
	/**
	 * The number of milliseconds to delay before executing the timer task
	 */
	private final long DELAY = 0;
	
	/**
	 * <p>
	 * The version of the server since the last poll. 
	 * Used to check if changes have been made to the server
	 * by comparing the version numbers.
	 * </p>
	 */
	private int version = -1;
	
	/**
	 * The proxy to use when polling the server. This will be set externally.
	 * Could be either a mock proxy or the real server proxy.
	 */
	private IServerProxy server;

	/**
	 * The class that does all model updates.
	 */
	private ModelUpdater modelUpdater;

    /**
     * <pre>
     * The timer to poll the server at regular intervals.
     * Will run on it's own thread, independent of the other code.
     * </pre>
     */
	private Timer poller;
	
	/**
	 * 
	 * @param seconds The interval at which to poll the server for updates
	 * @param server The proxy to use when polling the server.
	 */
	public ServerPoller(IServerProxy server) {
        this.server = server;
    }
	
	/**
	 * Initiates regularly polling the server for updated models
	 */
	public void start() {
		assert server != null;
		
		poller = new Timer(true);
		poller.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				pollServer();
			}
			
		}, DELAY, POLL_INTERVAL);
	}
	
	/**
	 * Calls the server to retrieve the current model state.
	 *
	 * @pre <pre>
	 *     1. proxy is not null
	 *     2. version is not null
	 *
	 * </pre>
	 *
	 * @post <pre>
	 * 	If the operation succeeds:
	 * 		1. A new version of the model is received from the server
	 *
	 * If the operation fails:
	 * 		1. The server returns an HTTP 400 error message and the response body contains an error message
	 * </pre>
	 */
	public void pollServer() {	
		try {
			String response = server.gameGetModel(version);
			
			boolean hasChanged = checkForUpdates(response);
			if (hasChanged) {
				JsonObject newModel = new JsonParser().parse(response).getAsJsonObject();
				modelUpdater.updateModel(newModel);
				//updateVersion(result);
			}
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the JSONObject contains updated data.
     *
     * @pre
     * data is not null
	 *
	 * @post<pre>
	 * If the JSONObject contains string "true", the model is already up-to-date.
     * Otherwise, the model will be sent the new data to update itself.
	 * </pre>
	 *
	 * @param data The JSON string to check for new data
	 * @return true if the JSON contains new data, otherwise false
	 */
	public boolean checkForUpdates(String response) { 
		if(response.equals("true")) {
			// no updates occurred, the version numbers matched up
			return false;
		}
		else {
			return true; 
		}
	}
	
	/**
	 * Extracts the version number from the server response and assigns it to the 
	 * version number on the client side.
	 * 
	 * @pre response is not null
	 * 
	 * @post The local version number is set equal to the version number from the server
	 * 
	 * @param response The response from the server containing new model data
	 */
	public void updateVersion(String response) {
		assert response != null;
		
		JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
		JsonElement version= obj.get("version");
		setVersion(version.getAsInt());
	}
	
	/**
     * Stops the poller and sets it to null
     */
    public void stop() {
        poller.cancel();
        poller = null;
    }
    
    public IServerProxy getProxy() {
    	return this.server;
    }

	public void setVersion(int version) {
		this.version = version;
	}

	public void setServer(IServerProxy proxy) {
		this.server = proxy;
	}

	public void setModelUpdater(ModelUpdater m) { this.modelUpdater = m; }

}

