package client.points;

import java.util.Observable;
import java.util.Observer;

import client.base.*;
import client.model.Model;
import client.model.Player;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {

	private IGameFinishedView finishedView;
	
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView Game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		
		super(view);
		
		setFinishedView(finishedView);
		
		Model.getInstance().addObserver(this);
		
		initFromModel();
	}
	
	public IPointsView getPointsView() {
		
		return (IPointsView)super.getView();
	}
	
	public IGameFinishedView getFinishedView() {
		return finishedView;
	}
	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		if(Model.getInstance().getGame().getCurrentPlayer() != null) {
			int victoryPoints = Model.getInstance().getGame().getCurrentPlayer().getVictoryPoints();
			getPointsView().setPoints(victoryPoints);
		}
		else {
			getPointsView().setPoints(5);
		}		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		int winnerIndex = Model.getInstance().getGame().getWinner();
		if(winnerIndex != -1) {	// someone won the game
			Player p = Model.getInstance().getGame().getPlayerList().get(winnerIndex);
			this.getFinishedView().setWinner(p.getName(), isLocalPlayer(p));
			this.finishedView.showModal();
		}
		
		if(Model.getInstance().getGame() != null) {
			initFromModel();
		}	
	}
	
	private boolean isLocalPlayer(Player p) {
		return p.getPlayerID() == Model.getInstance().getGame().getCurrentPlayer().getPlayerID();	
	}
	
}

