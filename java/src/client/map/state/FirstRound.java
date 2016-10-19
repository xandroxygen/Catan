package client.map.state;

public class FirstRound extends MapState {
	private static FirstRound inst = new FirstRound();
	private FirstRound() { }

	public static MapState instance() { return inst; }
}
