package client.join;

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
		
		Model.getInstance().addObserver(this);
		
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {

		getView().showModal();
		
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
	}

	@Override
	public void addAI() {

		// TEMPORARY
		getView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		try {
			GameInfo currentGame = GameAdministrator.getInstance().getCurrentGame();
			PlayerInfo[] players = new PlayerInfo[currentGame.getPlayers().size()];
			currentGame.getPlayers().toArray(players);
			view.setPlayers(players);
		}
		catch (InvalidActionException e) {
			// Error getting list
		}
	}

}

