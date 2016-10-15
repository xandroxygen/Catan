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
		Game game = Model.getInstance().getGame();
		List<Player> players = game.getPlayerList();
		
		// set local player color
		this.getView().setLocalPlayerColor(GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getColor());
		
		// initialize each player in the view (name and color)
		initPlayers(players);
		
		// update each player in the view (victory points, who's turn it is, largest army, longest road)
		if(game.getTurnTracker() != null) {
			int largestArmy = game.getTurnTracker().getLargestArmy();
			int longestRoad = game.getTurnTracker().getLongestRoad();
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
	
	/**
	 * Tells the view to initialize each player with their name and color
	 * 
	 * @param players The list of players to initialize
	 */
	private void initPlayers(List<Player> players) {
		if (players != null) {
			for(Player p : players) {
				this.getView().initializePlayer(p.getPlayerID(), p.getName(), p.getColor());
			}
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		initFromModel();
		
		GameStatus state = Model.getInstance().getGame().getTurnTracker().getStatus();
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

