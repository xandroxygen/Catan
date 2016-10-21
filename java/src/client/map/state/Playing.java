package client.map.state;


import client.admin.GameAdministrator;
import client.map.MapController;
import client.model.Model;
import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public class Playing extends MapState {
	
	private static Playing inst = new Playing();
	private Playing() { }

	public static MapState instance() { return inst; }
	
	public boolean canPlaceRoad(EdgeLocation edgeLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceRoad(playerID, false, edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceSettlement(playerID, false, vertLoc);

	}

	public boolean canPlaceCity(VertexLocation vertLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceCity(playerID, vertLoc);
	}
	
	public void placeRoad(EdgeLocation edgeLoc, MapController controller) {
		Model.getInstance().placeRoad(true,edgeLoc);

	}

	public void placeSettlement(VertexLocation vertLoc, MapController controller) {
		Model.getInstance().placeSettlement(true, vertLoc);

	}

	public void placeCity(VertexLocation vertLoc, MapController controller) {
		controller.getView().placeCity(vertLoc, Model.getInstance().getCurrentPlayer().getColor());
	}
	
	public void startMove(PieceType pieceType, boolean isFree,
			   boolean allowDisconnected, MapController controller) { 
		controller.getView().startDrop(pieceType, Model.getInstance().getCurrentPlayer().getColor(),true);
	}
}
