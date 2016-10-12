package client.map;

import client.base.Controller;
import client.data.RobPlayerInfo;
import client.model.GameStatus;
import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.locations.*;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;

	private GameStatus state;

	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		
		initFromModel();
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
		
		//<temp>
		
		Random rand = new Random();

		for (int x = 0; x <= 3; ++x) {
			
			int maxY = 3 - x;			
			for (int y = -3; y <= maxY; ++y) {				
				int r = rand.nextInt(HexType.values().length);
				HexType hexType = HexType.values()[r];
				HexLocation hexLoc = new HexLocation(x, y);
				getView().addHex(hexLoc, hexType);
				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
						CatanColor.RED);
				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
						CatanColor.BLUE);
				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
						CatanColor.ORANGE);
				getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
				getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
			}
			
			if (x != 0) {
				int minY = x - 3;
				for (int y = minY; y <= 3; ++y) {
					int r = rand.nextInt(HexType.values().length);
					HexType hexType = HexType.values()[r];
					HexLocation hexLoc = new HexLocation(-x, y);
					getView().addHex(hexLoc, hexType);
					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
							CatanColor.RED);
					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
							CatanColor.BLUE);
					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
							CatanColor.ORANGE);
					getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
					getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
				}
			}
		}
		
		PortType portType = PortType.BRICK;
		getView().addPort(new EdgeLocation(new HexLocation(0, 3), EdgeDirection.North), portType);
		getView().addPort(new EdgeLocation(new HexLocation(0, -3), EdgeDirection.South), portType);
		getView().addPort(new EdgeLocation(new HexLocation(-3, 3), EdgeDirection.NorthEast), portType);
		getView().addPort(new EdgeLocation(new HexLocation(-3, 0), EdgeDirection.SouthEast), portType);
		getView().addPort(new EdgeLocation(new HexLocation(3, -3), EdgeDirection.SouthWest), portType);
		getView().addPort(new EdgeLocation(new HexLocation(3, 0), EdgeDirection.NorthWest), portType);
		
		getView().placeRobber(new HexLocation(0, 0));
		
		getView().addNumber(new HexLocation(-2, 0), 2);
		getView().addNumber(new HexLocation(-2, 1), 3);
		getView().addNumber(new HexLocation(-2, 2), 4);
		getView().addNumber(new HexLocation(-1, 0), 5);
		getView().addNumber(new HexLocation(-1, 1), 6);
		getView().addNumber(new HexLocation(1, -1), 8);
		getView().addNumber(new HexLocation(1, 0), 9);
		getView().addNumber(new HexLocation(2, -2), 10);
		getView().addNumber(new HexLocation(2, -1), 11);
		getView().addNumber(new HexLocation(2, 0), 12);
		
		//</temp>
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		//TODO im not sure we did the model right for this function
		switch (state){
			case Rolling:
			case Trading:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
			break;
			case Building:
//				return Model.getInstance().canPlaceRoad(edgeLoc);
		}
		return false;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		//TODO im not sure we did the model right for this function
		switch (state){
			case Rolling:
			case Trading:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
			case Building:
//				return Model.getInstance().canPlaceSettlement(vertLoc);
		}
		return true;
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		//TODO im not sure we did the model right for this function
		switch (state){
			case Rolling:
			case Trading:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
			case Building:
//				return Model.getInstance().canPlaceCity(vertLoc);
		}
		return false;
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		//TODO im not sure we did the model right for this function
		switch (state){
			case Rolling:
			case Trading:
			case Building:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
				break;
			case Robber:
//				return Model.getInstance().canPlaceRobber(hexLoc);
		}
		return false;
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		switch (state){
			case Building:
				getView().placeRoad(edgeLoc, CatanColor.ORANGE);
			case Rolling:
			case Trading:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		switch (state){
			case Building:
				getView().placeSettlement(vertLoc, CatanColor.ORANGE);
			case Rolling:
			case Trading:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		switch (state){
			case Building:
				getView().placeCity(vertLoc, CatanColor.ORANGE);
			case Rolling:
			case Trading:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
		}
	}

	public void placeRobber(HexLocation hexLoc) {
		switch (state){
			case Robber:
				getView().placeRobber(hexLoc);
				getRobView().showModal();
			case Rolling:
			case Trading:
			case Building:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
				break;
		}

	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		switch (state){
			case Rolling:
				getView().startDrop(pieceType, CatanColor.ORANGE, true);
			case Trading:
			case Building:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
		}
	}
	
	public void cancelMove() {
		//TODO not sure what this is used for.
		switch (state){
			case Rolling:
//				getView().cancelMove(pieceType, CatanColor.ORANGE, true);
			case Trading:
			case Building:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
		}
	}
	
	public void playSoldierCard() {
		switch (state){
			case Trading:
			case Building:
//				Model.getInstance().playSoldierCard();
			case Rolling:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
		}
	}
	
	public void playRoadBuildingCard() {
		switch (state){
			case Trading:
			case Building:
//				Model.getInstance().playRoadBuildingCard();
			case Rolling:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
			case Robber:
				break;
		}
	}
	
	public void robPlayer(RobPlayerInfo victim) {
		switch (state){
			case Rolling:
			case Trading:
			case Building:
			case WaitingForResponse:
			case WaitingForTurn:
			case RespondToTrade:
				break;
			case Robber:
//				Model.getInstance().robPlayer();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}

