package client.communication;

import java.util.Observable;
import java.util.Observer;

import client.admin.GameAdministrator;
import client.base.*;
import client.model.Game;
import client.model.Model;


/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {

	public ChatController(IChatView view) {
		
		super(view);
		Model.getInstance().addObserver(this);
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) {
		Model.getInstance().sendMessage(message);
	}

	@Override
	public void update(Observable o, Object arg) {
		Game game = (Game)arg;
		getView().setEntries(game.getChat());
	}

}

