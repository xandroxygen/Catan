package server.http;

/**
 * Helper object for catan.user cookies
 */
public class UserInfo {

	private String name;
	private String password;
	private int playerID;

	public String getUsername() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setUsername(String username) {
		this.name = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
}
