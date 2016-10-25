package client.map.state;

import client.admin.GameAdministrator;
import client.map.MapController;
import client.model.Model;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public class FirstRound extends MapState {
	private static FirstRound inst = new FirstRound();
	private boolean roadPlaced = false;
	private boolean settlementPlaced = false;
	private FirstRound() { }

	public static MapState instance() { return inst; }
	
	public boolean canPlaceRoad(EdgeLocation edgeLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceRoad(playerID, true, edgeLoc);
	}
	
	public boolean canPlaceSettlement(VertexLocation vertLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		return Model.getInstance().canPlaceSettlement(playerID, true, vertLoc);
	}
	
	public void placeRoad(EdgeLocation edgeLoc, MapController controller) { 
		Model.getInstance().placeRoad(true,edgeLoc);
		roadPlaced = true;
	}
	
	public void placeSettlement(VertexLocation vertexLocation, MapController controller) {
		Model.getInstance().placeSettlement(true, vertexLocation);
		controller.setState(SecondRound.instance());
		settlementPlaced = true;
		Model.getInstance().finishTurn();
	}
	
	public boolean getRoadPlaced() {
		return roadPlaced;
	}
	
	public void initiateSetup(MapController controller) {
		if (!roadPlaced) {
			controller.getView().startDrop(PieceType.ROAD, Model.getInstance().getCurrentPlayer().getColor(),false);
		}
		else if (!settlementPlaced){
			controller.getView().startDrop(PieceType.SETTLEMENT, Model.getInstance().getCurrentPlayer().getColor(),false);
		}
	}
}
