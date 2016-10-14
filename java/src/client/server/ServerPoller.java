package client.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import client.admin.GameAdministrator;
import client.model.InvalidActionException;
import client.model.Model;
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
	private final long POLL_INTERVAL = 3500;
	
	/**
	 * The number of milliseconds to delay before executing the timer task
	 */
	private final long DELAY = 0;
	
	/**
	 * The proxy to use when polling the server. This will be set externally.
	 * Could be either a mock proxy or the real server proxy.
	 */
	private IServerProxy server;

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
        //modelUpdater = Model.getModelUpdater();
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
			if(GameAdministrator.getInstance().isSettingUp()) {
				GameAdministrator.getInstance().fetchGameList();
			}
			else {
				String response = server.gameGetModel(Model.getInstance().getVersion());
				
				boolean hasChanged = checkForUpdates(response);
				if (hasChanged) {
					JsonObject newModel = new JsonParser().parse(response).getAsJsonObject();
					Model.getInstance().updateModel(newModel);
				}
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
     * Stops the poller and sets it to null
     */
    public void stop() {
        poller.cancel();
        poller = null;
    }
    
    public IServerProxy getProxy() {
    	return this.server;
    }

	public void setServer(IServerProxy proxy) {
		this.server = proxy;
	}

}

