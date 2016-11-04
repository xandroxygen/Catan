package client.communication;

import java.util.*;

import client.base.*;
import shared.model.Game;
import client.model.Model;


/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController, Observer {

	public GameHistoryController(IGameHistoryView view) {
		
		super(view);
		
		initFromModel();
		
		Model.getInstance().addObserver(this);
	}
	
	@Override
	public IGameHistoryView getView() {
		
		return (IGameHistoryView)super.getView();
	}
	
	private void initFromModel() {
		
		if (Model.getInstance().getGame() != null) {
			getView().setEntries(Model.getInstance().getGame().getLog());
		}
	
	}

	@Override
	public void update(Observable o, Object arg) {
		Game game = (Game)arg;
		getView().setEntries(game.getLog());
	}
	
}

