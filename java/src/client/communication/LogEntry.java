package client.communication;

import shared.definitions.*;

/**
 * Message (or entry) displayed in the LogComponent
 */
public class LogEntry
{
	
	/**
	 * Color used when displaying the message
	 */
	private CatanColor color;
	
	/**
	 * Message text
	 */
	private String message;
	private String username = "";
	
	public LogEntry(CatanColor color, String message)
	{
		this.color = color;
		this.message = message;
	}
	
	public LogEntry(String message, String user) {
		this.message = message;
		this.username = user;
	}
	
	public CatanColor getColor()
	{
		return color;
	}
	
	public void setColor(CatanColor color)
	{
		this.color = color;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getUsername() {
		return username;
	}
	
}

