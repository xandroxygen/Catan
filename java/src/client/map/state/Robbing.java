package client.map.state;

import client.map.MapController;
import client.model.Model;
import shared.locations.HexLocation;

public class Robbing extends MapState {
	
	private static Robbing inst = new Robbing();
	private Robbing() { }

	public static MapState instance() { return inst; }
	
	public boolean canPlaceRobber(HexLocation hexLoc, MapController controller) {
		return Model.getInstance().canPlaceRobber(hexLoc);
	}
}
