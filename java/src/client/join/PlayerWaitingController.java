package client.join;

import java.util.Observable;
import java.util.Observer;

import client.base.*;
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
		view.setPlayers(Model.getInstance().getGame().getPlayerList());
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {

		getView().showModal();
	}

	@Override
	public void addAI() {

		// TEMPORARY
		getView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		view.setPlayers(Model.getInstance().getGame().getPlayerList());
	}

}

