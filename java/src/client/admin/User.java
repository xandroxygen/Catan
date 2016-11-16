package client.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import client.data.PlayerInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Superclass for a player. A user is someone who has logged in to the application.
 * Once a user joins a game, a player is created with the same ID.
 */
public class User {
    private String username;
    private String password;
    private String cookie;
    public boolean isLoggedIn;
    private List<Integer> gamesJoined;
    private PlayerInfo localPlayer;

    public User() {
        username = "";
        password = "";
        cookie = "";
        isLoggedIn = false;
        gamesJoined = new ArrayList<>();
        localPlayer = new PlayerInfo();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        cookie = "";
        isLoggedIn = false;
        gamesJoined = new ArrayList<>();
        localPlayer = new PlayerInfo();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean canJoinGame() {
        return false;
    }

    public boolean canCreateGame() {
        return false;
    }

    public boolean joinGame(int id) {
        if (gamesJoined.contains(id)) {
            return false;
        }
        else {
            gamesJoined.add(id);
            return true;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

	public PlayerInfo getLocalPlayer() {
		return localPlayer;
	}

	public void setLocalPlayer(PlayerInfo localPlayer) {
		this.localPlayer = localPlayer;
	}

    /**
     * Decodes cookie to get name and ID, and creates PlayerInfo from that.
     */
	public void createLocalPlayer() {

        try {
            String decodedCookie = URLDecoder.decode(cookie, "UTF-8");
            decodedCookie = decodedCookie.substring(11);
            JsonObject jsonCookie = new JsonParser().parse(decodedCookie).getAsJsonObject();

            String name = jsonCookie.get("name").getAsString();
            int id = jsonCookie.get("playerID").getAsInt();

            localPlayer = new PlayerInfo();
            localPlayer.setName(name);
            localPlayer.setId(id);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
