package shared.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageList implements Serializable {
	private ArrayList<Message> messages;

	public ArrayList<Message> getMessages() {
		return messages;
	}
}
