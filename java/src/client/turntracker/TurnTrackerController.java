package client.turntracker;

import client.admin.GameAdministrator;
import client.base.Controller;
import client.model.*;

import javax.swing.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import client.admin.GameAdministrator;
import client.base.*;
import client.model.Game;
import client.model.GameStatus;
import client.model.InvalidActionException;
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
		if(Model.getInstance().canEndTurn(GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId())) {
			try {
				System.out.println("Ending turn");
				Model.getInstance().getServer().finishTurn();
			} catch (InvalidActionException e) {
				JOptionPane.showMessageDialog((TurnTrackerView)getView(), "Something went wrong ending your turn!");
			}
		}
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
		if (game.isMyTurn()) {
			if(state != null) {
			switch (state) {
				case FirstRound:
					this.getView().updateGameState("FirstRound", false);
					break;
				case SecondRound:
					this.getView().updateGameState("SecondRound", false);
					break;
				case Rolling:
					this.getView().updateGameState("Rolling", false);
					break;
				case Robbing:
					this.getView().updateGameState("Robbing", false);
					break;
				case Playing:
					this.getView().updateGameState("Playing", false);
					break;
				case Discarding:
					this.getView().updateGameState("Discarding", false);
					break;
				default:
					break;
				}
			}
		}
		else {
			this.getView().updateGameState("Waiting for other players", false);
		}
	}

}

