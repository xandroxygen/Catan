package client.admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for a player. A user is someone who has logged in to the application.
 * Once a user joins a game, a player is created with the same ID.
 */
public class User {
    private String username;
    private String password;
    private String cookie;
    private List<Integer> gamesJoined;

    public User() {
        username = "";
        password = "";
        cookie = "";
        gamesJoined = new ArrayList<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        cookie = "";
        gamesJoined = new ArrayList<>();
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
}
