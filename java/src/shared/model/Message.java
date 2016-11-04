package shared.model;

/**
 * Message that will show in the chat box.
 * Contains content and a playerID.
 */
public class Message {
    private String content;
    private int playerId;

    public Message() {
        playerId = -1;
        content = "";
    }

    public Message(int playerId, String content) {
        this.playerId = playerId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
