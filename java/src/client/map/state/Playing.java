package client.map.state;


import client.admin.GameAdministrator;
import client.data.RobPlayerInfo;
import client.map.MapController;
import client.map.MapView;
import client.model.InvalidActionException;
import client.model.Model;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public class Playing extends MapState {
	
	private static Playing inst = new Playing();
	private HexLocation newLocation = null;
	private boolean isRoadBuildingCard = false;
	private EdgeLocation firstRoad;

	private Playing() { }

	public static MapState instance() { return inst; }
	
	public boolean canPlaceRoad(EdgeLocation edgeLoc, MapController controller) {
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		if(!isRoadBuildingCard){
			return Model.getInstance().canPlaceRoad(playerID, false, edgeLoc);
		}
		else{
			return Model.getInstance().canPlaceRoad(playerID, firstRoad, edgeLoc);
		}
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
		if(!isRoadBuildingCard){
			Model.getInstance().placeRoad(false,edgeLoc);
		}
		else{
			if(firstRoad == null){
				firstRoad = edgeLoc;
				controller.getView().startDrop(PieceType.ROAD, Model.getInstance().getCurrentPlayer().getColor(), false);
				((MapView)controller.getView()).getMap().placeRoad(firstRoad, Model.getInstance().getCurrentPlayer().getColor());
			}
			else{
				try {
					Model.getInstance().getServer().playRoadBuilding(firstRoad, edgeLoc);
				} catch (InvalidActionException e) {
					e.printStackTrace();
				}
				isRoadBuildingCard = false;
				firstRoad = null;
			}
		}
	}

	public void placeSettlement(VertexLocation vertLoc, MapController controller) {
		Model.getInstance().placeSettlement(false, vertLoc);
	}

	public void placeCity(VertexLocation vertLoc, MapController controller) {
		Model.getInstance().placeCity(vertLoc);
	}
	
	public void startMove(PieceType pieceType, boolean isFree,
			   boolean allowDisconnected, MapController controller) { 
		controller.getView().startDrop(pieceType, Model.getInstance().getCurrentPlayer().getColor(),true);
	}

	public void playSoldierCard(MapController controller) {
		controller.getView().startDrop(PieceType.ROBBER, Model.getInstance().getCurrentPlayer().getColor(), false);
	}

	public void playRoadBuildingCard(MapController controller) {
		controller.getView().startDrop(PieceType.ROAD, Model.getInstance().getCurrentPlayer().getColor(), false);
		isRoadBuildingCard = true;
	}

	public boolean canPlaceRobber(HexLocation hexLoc, MapController controller) {
		return Model.getInstance().canPlaceRobber(hexLoc);
	}

	//used for playing a soldier card
	public void placeRobber(HexLocation hexLoc, MapController controller) {
		controller.getView().placeRobber(hexLoc);
		newLocation = hexLoc;
	}

	//used for playing a soldier card
	public void robPlayer(RobPlayerInfo victim, MapController controller) {
		if (newLocation != null && victim != null) {
			try {
				Model.getInstance().getGame().getServer().playSoldier(newLocation, victim.getPlayerIndex());
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
		else if (newLocation != null) {
			try {
				Model.getInstance().getGame().getServer().playSoldier(newLocation,-1);
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}
}
