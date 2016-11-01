package client.map.state;

public class Discarding extends MapState {
	private static Discarding inst = new Discarding();
	private Discarding() { }

	public static MapState instance() { return inst; }
}
