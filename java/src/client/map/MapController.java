package client.map;

import client.base.Controller;
import client.data.RobPlayerInfo;
import client.map.state.Discarding;
import client.map.state.FirstRound;
import client.map.state.MapState;
import client.map.state.Playing;
import client.map.state.Robbing;
import client.map.state.Rolling;
import client.map.state.SecondRound;
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

	private MapState state;

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
	
	public void setState(MapState state) {
		this.state = state;
	}
	
	protected void initFromModel() {
		//add Hexes and their numbers
		addHexes();
		//add Ports
		addPorts();
		//place robber
		placeRobber();
		//add roads
		addRoads();
		//add cities
		addCities();
		//add settlements
		addSettlements();
	}

	private void addHexes(){
		//add hex's and their numbers
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
		//add the water boarder
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
	}


	private void addPorts(){
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
	}

	private void placeRobber(){
		Robber robber = Model.getInstance().getGame().getTheMap().getRobber();
		getView().placeRobber(robber.getLocation());
	}

	private void addRoads(){
		for (Map.Entry<EdgeLocation, Road> entry : Model.getInstance().getGame().getTheMap().getRoads().entrySet()) {
			EdgeLocation edgeLocation = entry.getValue().getLocation().getNormalizedLocation();
			CatanColor catanColor = getCatanColorFromPlayerIndex(entry.getValue().getOwnerIndex());
			getView().placeRoad(edgeLocation, catanColor);
		}
	}

	private void addCities(){
		for (Map.Entry<VertexLocation, City> entry : Model.getInstance().getGame().getTheMap().getCities().entrySet()) {
			VertexLocation vertexLocation = entry.getValue().getLocation().getNormalizedLocation();
			CatanColor catanColor = getCatanColorFromPlayerIndex(entry.getValue().getOwnerIndex());
			getView().placeCity(vertexLocation, catanColor);
		}
	}

	private void addSettlements(){
		for (Map.Entry<VertexLocation, Settlement> entry : Model.getInstance().getGame().getTheMap().getSettlements().entrySet()) {
			VertexLocation vertexLocation = entry.getValue().getLocation().getNormalizedLocation();
			CatanColor catanColor = getCatanColorFromPlayerIndex(entry.getValue().getOwnerIndex());
			getView().placeSettlement(vertexLocation, catanColor);
		}
	}

	private CatanColor getCatanColorFromPlayerIndex(int playerIndex){
		Player player = Model.getInstance().getGame().getPlayerList().get(playerIndex);
		return player.getColor();
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
		return state.canPlaceRoad(edgeLoc,this);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		return state.canPlaceSettlement(vertLoc, this);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		return state.canPlaceCity(vertLoc, this);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return state.canPlaceRobber(hexLoc, this);
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		state.placeRoad(edgeLoc, this);
	}

	public void placeSettlement(VertexLocation vertLoc) {
		state.placeSettlement(vertLoc, this);
	}

	public void placeCity(VertexLocation vertLoc) {
		state.placeCity(vertLoc, this);
	}

	public void placeRobber(HexLocation hexLoc) {
		state.placeRobber(hexLoc, this);
		getRobView().setPlayers(Model.getInstance().getCandidateVictims(hexLoc));
		getRobView().closeModal();
		getRobView().showModal();
		
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		state.startMove(pieceType, isFree, allowDisconnected, this);
	}
	
	public void cancelMove() {
		state.cancelMove(this);
	}
	
	public void playSoldierCard() {
		state.playSoldierCard(this);
	}
	
	public void playRoadBuildingCard() {
		state.playRoadBuildingCard(this);
	}
	
	public void robPlayer(RobPlayerInfo victim) {
		state.robPlayer(victim, this);
		getRobView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		Game game = Model.getInstance().getGame();
		if(game != null && game.getTheMap() != null && 
				game.getTheMap().getHexes() != null && game.isMyTurn()) {
			
			
			/*if (game.getTurnTracker().getStatus() == GameStatus.FirstRound && game.isMyTurn()) {
				state = FirstRound.instance();
				state.initiateSetup(this);
			}
			
			else if (game.getTurnTracker().getStatus() == GameStatus.SecondRound && game.isMyTurn()) {
				state.initiateSetup(this);
			}*/
			
			GameStatus modelStatus = game.getTurnTracker().getStatus();
			
			switch (modelStatus) {
				case FirstRound:
					this.setState(FirstRound.instance());
					break;
				case SecondRound:
					this.setState(SecondRound.instance());
					break;
				case Discarding:
					this.setState(Discarding.instance());
					break;
				case Playing:
					this.setState(Playing.instance());
					break;
				case Robbing:
					this.setState(Robbing.instance());
					state.startMove(PieceType.ROBBER, true, false, this);
					break;
				case Rolling:
					this.setState(Rolling.instance());
					break;
				default:
					this.setState(Playing.instance());
			}
			
			state.initiateSetup(this);
			initFromModel();
		}
		
		
			
	}

}

