package client.join;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import client.admin.GameAdministrator;
import client.base.*;
import client.data.GameInfo;
import client.data.PlayerInfo;
import client.model.InvalidActionException;
import client.model.Model;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {
	
	IPlayerWaitingView view;

	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
		this.view = view;
		
		try {
			GameAdministrator.getInstance().addObserver(this);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
		
		try {
			GameInfo currentGame = GameAdministrator.getInstance().getCurrentGame();
			PlayerInfo[] players = new PlayerInfo[currentGame.getPlayers().size()];
			currentGame.getPlayers().toArray(players);
			view.setPlayers(players);
			
			view.setAIChoices(GameAdministrator.getInstance().getAIList());
		}
		catch (InvalidActionException e) {
			// Error getting list
		}
		getView().showModal();
	}

	@Override
	public void addAI() {
		try {
			GameAdministrator.getInstance().addAI(view.getSelectedAI());
			getView().showModal();
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			if (GameAdministrator.getInstance().getCurrentGame() != null) {
				if (GameAdministrator.getInstance().getCurrentGame().getPlayers().size() == 4) {
					// SOME KIND OF ACTION TO START GAME.....
					view.closeModal();
				}
				GameInfo currentGame= (GameInfo) arg;
				PlayerInfo[] players = new PlayerInfo[currentGame.getPlayers().size()];
				currentGame.getPlayers().toArray(players);
				view.setPlayers(players);
				view.closeModal();
				view.showModal();
			}
		}
		catch (InvalidActionException e) {
			// Error getting list
		}
	}

}

