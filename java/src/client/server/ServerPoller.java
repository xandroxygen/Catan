package client.server;


/**
 * @author	Spencer Olsen
 * 
 * Polls the server to check whether a new model exists.
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
	 * <p>
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
	public ServerPoller(int interval, int version, IServerProxy proxy) {
		
	}
	
	/**
	 * Calls the server proxy to retrieve the current model state.
	 */
	public void pollServer() {
		
	}
	
	/**
	 * Sends the new JSON data to the Model so the Model can update itself.
	 */
	private void updateModel() {
		
	}

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

