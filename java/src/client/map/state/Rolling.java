package client.map.state;

public class Rolling extends MapState {
	
	private static Rolling inst = new Rolling();
	private Rolling() { }

	public static MapState instance() { return inst; }

}
