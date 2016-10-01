package client.admin;

/**
 * Contains details of a player, including color, name, and id.
 * Used only in reference to admin functions, not the Player model.
 */
public class PlayerDetails {
    private String color;
    private String name;
    private int id;

    public PlayerDetails(String color, String name, int id) {
        this.color = color;
        this.name = name;
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
