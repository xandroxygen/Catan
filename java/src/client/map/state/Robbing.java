package client.map.state;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.model.InvalidActionException;
import client.model.Model;
import shared.definitions.PieceType;
import shared.locations.HexLocation;

public class Robbing extends MapState {
	
	private static Robbing inst = new Robbing();
	private HexLocation newLocation = null;
	
	private Robbing() { }

	public static MapState instance() { return inst; }
	
	public boolean canPlaceRobber(HexLocation hexLoc, MapController controller) {
		return Model.getInstance().canPlaceRobber(hexLoc);
	}
	
	public void startMove(PieceType pieceType, boolean isFree,
			   boolean allowDisconnected, MapController controller) { 
		controller.getView().startDrop(pieceType, Model.getInstance().getCurrentPlayer().getColor(),false);
	}
	
	public void placeRobber(HexLocation hexLoc, MapController controller) {
		controller.getView().placeRobber(hexLoc);
		newLocation = hexLoc;
	}
	
	public void robPlayer(RobPlayerInfo victim, MapController controller) {
		if (newLocation != null && victim != null) {
			try {
				Model.getInstance().getGame().getServer().robPlayer(newLocation, victim.getPlayerIndex());
				newLocation = null;
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
		else if (newLocation != null) {
			try {
				Model.getInstance().getGame().getServer().robPlayer(newLocation, -1);
				newLocation = null;
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}
}
