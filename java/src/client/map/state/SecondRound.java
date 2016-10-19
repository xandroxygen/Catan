package client.map.state;

import client.admin.GameAdministrator;
import client.map.MapController;
import client.model.Model;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public class SecondRound extends MapState {
	
	private static SecondRound inst = new SecondRound();
	private SecondRound() { }

	public static MapState instance() { return inst; }
	
	public boolean canPlaceRoad(EdgeLocation edgeLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceRoad(playerID, true, edgeLoc);
	}
	
	public boolean canPlaceSettlement(VertexLocation vertLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceSettlement(playerID, true, vertLoc);
	}
	
	
}
