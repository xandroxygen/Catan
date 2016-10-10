package client.join;

import shared.definitions.CatanColor;

import java.util.Observable;
import java.util.Observer;

import client.admin.GameAdministrator;
import client.base.*;
import client.data.*;
import client.misc.*;
import client.model.InvalidActionException;
import client.model.Model;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController, Observer {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	
	
	// Is this good practice???
	int gameID = -1;
	
	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {

		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {	
		
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		
		return selectColorView;
	}
	public void setSelectColorView(ISelectColorView selectColorView) {
		
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	public void setMessageView(IMessageView messageView) {
		
		this.messageView = messageView;
	}

	@Override
	public void start() {		
		
		try {
			GameAdministrator.getInstance().fetchGameList();
			
			//TODO: where is the current player stored??
			getJoinGameView().setGames(GameAdministrator.getInstance().getAllCurrentGames(), null);
			
			getJoinGameView().showModal();
		} catch (InvalidActionException e) {
			getMessageView().showModal();
            getMessageView().setTitle("Error");
            getMessageView().setMessage(e.getMessage());
		}
	}

	@Override
	public void startCreateNewGame() {
		
		
		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}

	@Override
	public void createNewGame() {
		
		getNewGameView().closeModal();
	}

	@Override
	public void startJoinGame(GameInfo game) {
		gameID = game.getId();
		
		// disable the colors that have already been taken
		// TODO: Do I need to explicitly enable to other colors that haven't been take, or does that happen by default?
		for(PlayerInfo p : game.getPlayers()) {
			getSelectColorView().setColorEnabled(p.getColor(), false);
		}
		getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
	
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		try {
			if(GameAdministrator.getInstance().canJoinGame(gameID, color)){
				GameAdministrator.getInstance().joinGame(gameID, color);
			}
		} catch (InvalidActionException e) {
			getMessageView().showModal();
            getMessageView().setTitle("Error");
            getMessageView().setMessage("Error joining the game: " + e.getMessage());
		}
		
		// If join succeeded
		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		joinAction.execute();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		
	}

}

