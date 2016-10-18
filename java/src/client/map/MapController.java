package client.map;

import client.admin.GameAdministrator;
import client.base.Controller;
import client.data.RobPlayerInfo;
import client.model.*;
import shared.definitions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;

	private GameStatus state;

	public MapController(IMapView view, IRobView robView) {
		super(view);
		setRobView(robView);
		Model.getInstance().addObserver(this);
	}

	public IMapView getView() {
		
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}
	private void setRobView(IRobView robView) {
		this.robView = robView;
	}
	
	protected void initFromModel() {

		//add hex's and their
		for (Map.Entry<HexLocation, Hex> entry : Model.getInstance().getGame().getTheMap().getHexes().entrySet()) {
			HexLocation hexLoc = entry.getValue().getLocation();
			HexType hexType = entry.getValue().getResource();
			if (hexType == null) {
				hexType = HexType.DESERT;
				getView().addHex(hexLoc, hexType);
			}
			else {
				int hexNumber =	entry.getValue().getNumber();
				getView().addHex(hexLoc, hexType);
				getView().addNumber(hexLoc, hexNumber);
			}
		}

//		for (int x = 0; x <= 2; ++x) {
//			int maxY = 2 - x;
//			for (int y = -2; y <= maxY; ++y) {
//				HexLocation hexLoc = new HexLocation(x, y);
//				HexType hexType = Model.getInstance().getGame().getTheMap().getHexes().get(hexLoc).getResource();
//				if (hexType == null) {
//					hexType = HexType.DESERT;
//				}
//				getView().addHex(hexLoc, hexType);
//
////				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
////						CatanColor.RED);
////				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
////						CatanColor.BLUE);
////				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
////						CatanColor.ORANGE);
////				getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
////				getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
//			}
//			if (x != 0) {
//				int minY = x - 2;
//				for (int y = minY; y <= 2; ++y) {
//					HexLocation hexLoc = new HexLocation(x, y);
//					HexType hexType = Model.getInstance().getGame().getTheMap().getHexes().get(hexLoc).getResource();
//					if (hexType == null) {
//						hexType = HexType.DESERT;
//					}
//					getView().addHex(hexLoc, hexType);

//					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
//							CatanColor.RED);
//					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
//							CatanColor.BLUE);
//					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
//							CatanColor.ORANGE);
//					getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
//					getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);

//				}
//			}
//		}
		//add Water Hex Boarder
		getView().addHex(new HexLocation(0, -3), HexType.WATER);
		getView().addHex(new HexLocation(1, -3), HexType.WATER);
		getView().addHex(new HexLocation(2, -3), HexType.WATER);
		getView().addHex(new HexLocation(3, -3), HexType.WATER);
		getView().addHex(new HexLocation(3, -2), HexType.WATER);
		getView().addHex(new HexLocation(3, -1), HexType.WATER);
		getView().addHex(new HexLocation(3, 0), HexType.WATER);
		getView().addHex(new HexLocation(2, 1), HexType.WATER);
		getView().addHex(new HexLocation(1, 2), HexType.WATER);
		getView().addHex(new HexLocation(0, 3), HexType.WATER);
		getView().addHex(new HexLocation(-1, 3), HexType.WATER);
		getView().addHex(new HexLocation(-2, 3), HexType.WATER);
		getView().addHex(new HexLocation(-3, 3), HexType.WATER);
		getView().addHex(new HexLocation(-3, 2), HexType.WATER);
		getView().addHex(new HexLocation(-3, 1), HexType.WATER);
		getView().addHex(new HexLocation(-3, 0), HexType.WATER);
		getView().addHex(new HexLocation(-2, -1), HexType.WATER);
		getView().addHex(new HexLocation(-1, -2), HexType.WATER);

		//add Ports
		for (Map.Entry<EdgeLocation, Port> entry : Model.getInstance().getGame().getTheMap().getPorts().entrySet()) {
			entry.getValue().getRatio();
			EdgeLocation edgeLocation = entry.getValue().getLocation();
			ResourceType resourceType = entry.getValue().getResource();
			if(resourceType == null){
				getView().addPort(new EdgeLocation(edgeLocation.getHexLoc(), edgeLocation.getDir()), PortType.THREE);
			}
			else{
				PortType portType = convertFromResourceToPortType(resourceType);
				getView().addPort(new EdgeLocation(edgeLocation.getHexLoc(), edgeLocation.getDir()), portType);
			}
		}

		//place robber
		Robber robber = Model.getInstance().getGame().getTheMap().getRobber();
		getView().placeRobber(robber.getLocation());

	}

	private PortType convertFromResourceToPortType(ResourceType resourceType){
		switch(resourceType){
			case WOOD :
				return PortType.WOOD;
			case BRICK:
				return PortType.BRICK;
			case ORE :
				return PortType.ORE;
			case SHEEP:
				return PortType.SHEEP;
			case WHEAT:
				return PortType.WHEAT;
			default:
				return PortType.THREE;
		}
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		int playerID;
		switch (state){
            case FirstRound:
            case SecondRound:
				playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
				return Model.getInstance().canPlaceRoad(playerID, true, edgeLoc);
			case Rolling:
			case Robbing:
			case Discarding:
				break;
			case Playing:
				playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
				return Model.getInstance().canPlaceRoad(playerID, false, edgeLoc);
		}
		return false;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		int playerID;
		switch (state){
            case FirstRound:
            case SecondRound:
				playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
				return Model.getInstance().canPlaceSettlement(playerID, true, vertLoc);
			case Rolling:
			case Robbing:
			case Discarding:
				break;
			case Playing:
				playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
				return Model.getInstance().canPlaceSettlement(playerID, false, vertLoc);
		}
		return true;
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		int playerID;
		switch (state){
			case FirstRound:
            case SecondRound:
			case Rolling:
			case Robbing:
			case Discarding:
				break;
			case Playing:
				playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
				return Model.getInstance().canPlaceCity(playerID, vertLoc);
		}
		return false;
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Rolling:
			case Playing:
			case Discarding:
				break;
			case Robbing:
				return Model.getInstance().canPlaceRobber(hexLoc);

		}
		return false;
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Playing:
				getView().placeRoad(edgeLoc, CatanColor.ORANGE);
			case Rolling:
			case Robbing:
			case Discarding:
				break;
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Playing:
				getView().placeSettlement(vertLoc, CatanColor.ORANGE);
			case Rolling:
			case Robbing:
			case Discarding:
				break;
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Playing:
				getView().placeCity(vertLoc, CatanColor.ORANGE);
			case Rolling:
			case Robbing:
			case Discarding:
				break;
		}
	}

	public void placeRobber(HexLocation hexLoc) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Robbing:
				getView().placeRobber(hexLoc);
				getRobView().showModal();
			case Rolling:
			case Playing:
			case Discarding:
				break;
		}

	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Rolling:
				getView().startDrop(pieceType, CatanColor.ORANGE, true);
			case Playing:
			case Robbing:
			case Discarding:
				break;
		}
	}
	
	public void cancelMove() {
		//TODO not sure what this is used for.
		switch (state){
            case FirstRound:
            case SecondRound:
			case Rolling:
//				getView().cancelMove(pieceType, CatanColor.ORANGE, true);
			case Playing:
			case Robbing:
			case Discarding:
		}
	}
	
	public void playSoldierCard() {
		switch (state){
			case Playing:
//				Model.getInstance().playSoldierCard();
			case FirstRound:
			case SecondRound:
			case Rolling:
			case Robbing:
			case Discarding:
				break;
		}
	}
	
	public void playRoadBuildingCard() {
		switch (state){
			case Playing:
//				Model.getInstance().playRoadBuildingCard();
			case FirstRound:
			case SecondRound:
			case Rolling:
			case Robbing:
			case Discarding:
				break;
		}
	}
	
	public void robPlayer(RobPlayerInfo victim) {
		switch (state){
            case FirstRound:
            case SecondRound:
			case Rolling:
			case Playing:
			case Discarding:
				break;
			case Robbing:
//				Model.getInstance().robPlayer();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(Model.getInstance().getGame() != null &&
				Model.getInstance().getGame().getTheMap() != null &&
				Model.getInstance().getGame().getTheMap().getHexes() != null){
			initFromModel();
		}

	}

}

