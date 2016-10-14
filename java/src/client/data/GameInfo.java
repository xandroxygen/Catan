package client.data;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Used to pass game information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique game ID</li>
 * <li>Title: Game title (non-empty string)</li>
 * <li>Players: List of players who have joined the game (can be empty)</li>
 * </ul>
 * 
 */
public class GameInfo
{
	private int id;
	private String title;
	private List<PlayerInfo> players;
	private String cookie;
	
	public GameInfo()
	{
		setId(-1);
		setTitle("");
		players = new ArrayList<PlayerInfo>();
	}
	
	public GameInfo(String title, int id) {
		this.title = title;
		this.id = id;
		players = new ArrayList<PlayerInfo>();
	}
	
	//[{"title":"Default Game","id":0,"players":[{"color":"orange","name":"Sam","id":0}
	
	public GameInfo(JsonObject json) {
		players = new ArrayList<PlayerInfo>();
		this.id = json.get("id").getAsInt();
		this.title = json.get("title").getAsString();
		JsonArray playerArray = json.getAsJsonArray("players");
		for (JsonElement player : playerArray) {
			// TODO: Make this more efficent. The json gives an empty object {}, not sure hwo to check for that.
			PlayerInfo playerInfo = new PlayerInfo(player.getAsJsonObject());
			if (playerInfo.getId() != -1) {
				players.add(new PlayerInfo(player.getAsJsonObject()));
			}
		}
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getCookie() {
		return cookie;
	}
	
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void addPlayer(PlayerInfo newPlayer)
	{
		players.add(newPlayer);
	}
	
	public List<PlayerInfo> getPlayers()
	{
		return Collections.unmodifiableList(players);
	}
	
	@Override
	public boolean equals(Object o) {
		return ((GameInfo) o).getId() == this.id;
	}
}

