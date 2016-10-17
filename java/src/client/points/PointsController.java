package client.points;

import client.base.*;
import client.model.Model;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController {

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
		//<temp>		
		getPointsView().setPoints(Model.getInstance().getGame().currentPlayer.getVictoryPoints());
		//</temp>
		
	}
	
	private int getVictoryPoints() {
		
		return 0;
	}
	
	public void update() {
		if(Model.getInstance().getGame() != null) {
			initFromModel();
		}
	}
	
}

