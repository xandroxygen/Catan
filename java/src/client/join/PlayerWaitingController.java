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
		

		GameAdministrator.getInstance().addObserver(this);

		
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
		
		GameInfo currentGame = GameAdministrator.getInstance().getCurrentGame();
		
		if (currentGame.getPlayers().size() == 4) {
			startGame();
		}
		else {
			PlayerInfo[] players = new PlayerInfo[currentGame.getPlayers().size()];
			currentGame.getPlayers().toArray(players);
			view.setPlayers(players);
			
			view.setAIChoices(GameAdministrator.getInstance().getAIList());
			getView().showModal();
		}
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
		if (GameAdministrator.getInstance().getCurrentGame() != null) {
			if (GameAdministrator.getInstance().getCurrentGame().getPlayers().size() == 4) {
				// SOME KIND OF ACTION TO START GAME.....
				startGame();
			}
			else {
				GameInfo currentGame= (GameInfo) arg;
				PlayerInfo[] players = new PlayerInfo[currentGame.getPlayers().size()];
				currentGame.getPlayers().toArray(players);
				view.setPlayers(players);
				view.closeModal();
				view.showModal();
			}
		}	
	}
	
	public void startGame() {
		// Change status so that poller starts getting the model information
		if(getView().isModalShowing()) {
			getView().closeModal();
		}
		
		GameAdministrator.getInstance().setSettingUp(false);
	}

}

