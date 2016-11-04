package client.join;

import shared.definitions.CatanColor;

import java.util.Observable;
import java.util.Observer;

import client.admin.GameAdministrator;
import client.base.*;
import client.data.*;
import client.misc.*;
import shared.model.InvalidActionException;

/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController, Observer {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	
	GameAdministrator gameAdmin;
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
		
		gameAdmin = GameAdministrator.getInstance();
		gameAdmin.addObserver(this);

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
			gameAdmin.fetchGameList();
			getJoinGameView().setGames(gameAdmin.getAllCurrentGames(), gameAdmin.getCurrentUser().getLocalPlayer());
			
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
		getJoinGameView().showModal();
	}

	@Override
	public void createNewGame() {
		try {
			if(gameAdmin.canCreateGame(newGameView.getTitle(), newGameView.getRandomlyPlaceHexes(), 
					newGameView.getRandomlyPlaceNumbers(), newGameView.getUseRandomPorts())) {
				
				GameInfo currentGame = gameAdmin.createGame(newGameView.getTitle(), newGameView.getRandomlyPlaceHexes(),
						newGameView.getRandomlyPlaceNumbers(), newGameView.getUseRandomPorts());
				
				currentGame.addPlayer(gameAdmin.getCurrentUser().getLocalPlayer());
				gameAdmin.setCurrentGame(currentGame);
				gameAdmin.joinGame(currentGame.getId(), CatanColor.BLUE);
				gameAdmin.getAllCurrentGames().add(currentGame);
				
				gameAdmin.fetchGameList();
				getJoinGameView().setGames(gameAdmin.getAllCurrentGames(), gameAdmin.getCurrentUser().getLocalPlayer());

				getNewGameView().closeModal();
				getJoinGameView().showModal();
			}			
		} catch (InvalidActionException e) {
			getMessageView().showModal();
            getMessageView().setTitle("Error");
            getMessageView().setMessage("Error creating the game: " + e.getMessage());
		}
	}

	@Override
	public void startJoinGame(GameInfo game) {
		gameAdmin.setCurrentGame(game);
		
		gameID = game.getId();

		getJoinGameView().closeModal();
		getSelectColorView().showModal();
		
		// disable the colors that have already been taken
		for (CatanColor c : CatanColor.getColors()) {
			getSelectColorView().setColorEnabled(c, true);
		}
		
		for(PlayerInfo p : game.getPlayers()) {
			if (!p.getName().equals(gameAdmin.getCurrentUser().getLocalPlayer().getName())) {
				getSelectColorView().setColorEnabled(p.getColor(), false);
			}
		}
	}

	@Override
	public void cancelJoinGame() {
		getSelectColorView().closeModal();
		getJoinGameView().showModal();
	}

	@Override
	public void joinGame(CatanColor color) {

		try {
			if(gameAdmin.canJoinGame(gameID, color)){
				for(PlayerInfo p : gameAdmin.getAllCurrentGames().get(gameID).getPlayers()) {
					if(p.getId() == gameAdmin.getCurrentUser().getLocalPlayer().getId()) {
						gameAdmin.getCurrentUser().setLocalPlayer(p);
					}
				}
				
				gameAdmin.joinGame(gameID, color);
				gameAdmin.getCurrentUser().getLocalPlayer().setColor(color);
				gameAdmin.setCurrentGame(gameAdmin.getAllCurrentGames().get(gameID));
	
				
				closeOpenModals();
				
				joinAction.execute();
			}
		} catch (InvalidActionException e) {
			getMessageView().showModal();
            getMessageView().setTitle("Error");
            getMessageView().setMessage("Error joining the game: " + e.getMessage());
		}		
	}

	@Override
	public void update(Observable o, Object arg) {	
		getJoinGameView().setGames(gameAdmin.getAllCurrentGames(), gameAdmin.getCurrentUser().getLocalPlayer());
	}
	
	public void updateView() {
		getJoinGameView().setGames(gameAdmin.getAllCurrentGames(), gameAdmin.getCurrentUser().getLocalPlayer());
	}
	
	/**
	 * Closes all open modals that shouldn't be open
	 */
	private void closeOpenModals() {
			if(getSelectColorView().isModalShowing())
				getSelectColorView().closeModal();
			
			if(getJoinGameView().isModalShowing())
				getJoinGameView().closeModal();
			
			if(getNewGameView().isModalShowing())
				getNewGameView().closeModal();
	}

}

