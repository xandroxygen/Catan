package client.turntracker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import client.admin.GameAdministrator;
import client.base.*;
import client.model.Game;
import client.model.GameStatus;
import client.model.Model;
import client.model.Player;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	Game game;
	
	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		
		Model.getInstance().addObserver(this);
		
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		
	}
	
	private void initFromModel() {
		game = Model.getInstance().getGame();
		List<Player> players = game.getPlayerList();
		
		// set local player color
		this.getView().setLocalPlayerColor(GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getColor());
		
		initPlayers(players);
		
		updatePlayers(players);
	}
	
	/**
	 * Tells the view to initialize each player with their name and color
	 * 
	 * @param players The list of players to initialize
	 */
	private void initPlayers(List<Player> players) {
		if (players != null) {
			for(Player p : players) {
				this.getView().initializePlayer(p.getPlayerIndex(), p.getName(), p.getColor());
			}
		}
		
	}
	
	/** Updates the view for each player's victory points, who's turn it is, and who has
	 * the largest army and longest road
	 * 
	 * @param players The list of players to update the view on
	 */
	private void updatePlayers(List<Player> players) {		
		int largestArmy = -1;
		int longestRoad = -1;
		
		if(game.getTurnTracker() != null) {
			largestArmy = game.getTurnTracker().getLargestArmy();
			longestRoad = game.getTurnTracker().getLongestRoad();
		}
		
		if (players != null) {
			for (Player p : players) {
				int playerIndex = p.getPlayerIndex();
				
				boolean isMyTurn = game.isTurn(p.getPlayerID());
				int victoryPoints = p.getVictoryPoints();
				boolean isLargestArmy = (playerIndex == largestArmy);
				boolean isLongestRoad = (playerIndex == longestRoad);
				
				this.getView().updatePlayer(playerIndex, victoryPoints, isMyTurn, isLargestArmy, isLongestRoad);	
			}
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		initFromModel();
		
		GameStatus state = game.getTurnTracker().getStatus();
		if(state != null) {
			switch (state) {
			case Rolling:
				this.getView().updateGameState("Rolling", false);
				break;
			case Trading:
				this.getView().updateGameState("Trading", false);
				break;
			case Building:
				this.getView().updateGameState("Building", false);
				break;
			case WaitingForResponse:
				this.getView().updateGameState("Waiting for response", false);
				break;
			case WaitingForTurn:
				this.getView().updateGameState("Waiting for turn", false);
				break;
			case RespondToTrade:
				this.getView().updateGameState("Responding to trade", false);
				break;
			case Robber:
				this.getView().updateGameState("Robbin", false);
				break;
			default:
				break;
		}
		}
		
			
	}

}

