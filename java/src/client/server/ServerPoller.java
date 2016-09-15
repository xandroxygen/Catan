package client.server;

import org.json.simple.JSONObject;

/**
 * @author	Spencer Olsen
 * 
 * Polls the server to check whether a new model exists. If a new model exists, the ServerPoller sends
 * the new data to the Model to update itself.
 */

public class ServerPoller {
	
	/**
	 * Specifies the interval at which to poll the server for updates.
	 */
	private int interval;
	
	/**
	 * <p>
	 * The version of the server since the last poll. 
	 * Used to check if changes have been made to the server
	 * by comparing the version numbers.
	 * </p>
	 */
	private int version;
	
	/**
	 * The proxy to use when polling the server. This will be set externally.
	 */
	private IServerProxy proxy;
	

	public ServerPoller() {	}
	
	/**
	 * 
	 * @param interval The interval at which to poll the server for updates
	 * @param version The version of the server since the last poll
	 * @param proxy The proxy to use when polling the server.
	 */
	public ServerPoller(int interval, int version, IServerProxy proxy);
	
	/**
	 * Calls the server proxy to retrieve the current model state.
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
		JSONObject result = proxy.getGameModel(version);
		if (checkForUpdates(result)) {
			// get new version number


			updateModel(result);
		}
	}

	/**
	 * Checks if the JSONObject contains updated data.
	 *
	 * @post<pre>
	 * If the JSONObject is null, the method returns false, signifying that there is no new data.
	 * If the JSONObject is not null, the method returns true, signifying that there was new data
	 * retrieved from the server.
	 * </pre>
	 *
	 * @param data The JSONObject to check for new data
	 * @return true if the JSONObject contains new data, otherwise false
	 */
	private boolean checkForUpdates(JSONObject data) {
		if (data == null)
			return false;
		else
			return true;

	}
	
	/**
	 * Sends the new JSON data to the Model so the Model can update itself.
	 *
	 * @pre <pre>
	 * data is not null
	 * </pre>
	 *
	 * @post <pre>
	 * The model is updated with the new data.
	 * </pre>
	 *
	 * @param data	The data to send to the model so it can update itself
	 */
	private void updateModel(JSONObject data) {}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public IServerProxy getProxy() {
		return proxy;
	}

	public void setProxy(IServerProxy proxy) {
		this.proxy = proxy;
	}

}

