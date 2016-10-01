package client.admin;

import client.admin.PlayerDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains details of a game, including title, id, and list of player.
 * Not to be confused with the model's Game class, this contains details needed for admin functions.
 */
public class GameDetails {
    private String title;
    private int id;
    private List<PlayerDetails> players;
    private String cookie;

    public GameDetails(String title, int id) {
        this.title = title;
        this.id = id;
        players = new ArrayList<>();
    }

    public GameDetails(String title, int id, List<PlayerDetails> players) {
        this.title = title;
        this.id = id;
        this.players = players;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public List<PlayerDetails> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDetails> players) {
        this.players = players;
    }

    public void addPlayer(PlayerDetails player) {
        players.add(player);
    }

    public void removePlayer(PlayerDetails player) {
        players.remove(player);
    }
}
