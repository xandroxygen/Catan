package client.resources;

import java.util.*;

import client.base.*;
import client.model.Game;
import client.model.Model;
import shared.definitions.ResourceType;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController, Observer {

	private Map<ResourceBarElement, IAction> elementActions;
	
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();
		Model.getInstance().addObserver(this);
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

		elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
		executeElementAction(ResourceBarElement.ROAD);
	}

	@Override
	public void buildSettlement() {
		executeElementAction(ResourceBarElement.SETTLEMENT);
	}

	@Override
	public void buildCity() {
		executeElementAction(ResourceBarElement.CITY);
	}

	@Override
	public void buyCard() {
		executeElementAction(ResourceBarElement.BUY_CARD);
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			
			IAction action = elementActions.get(element);
			action.execute();
		}
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {

		Game game = (Game) arg;

		// update number of resources
		getView().setElementAmount(ResourceBarElement.WOOD, game.getCurrentPlayer().getNumberOfResourceType(ResourceType.WOOD));
		getView().setElementAmount(ResourceBarElement.WHEAT, game.getCurrentPlayer().getNumberOfResourceType(ResourceType.WHEAT));
		getView().setElementAmount(ResourceBarElement.BRICK, game.getCurrentPlayer().getNumberOfResourceType(ResourceType.BRICK));
		getView().setElementAmount(ResourceBarElement.ORE, game.getCurrentPlayer().getNumberOfResourceType(ResourceType.ORE));
		getView().setElementAmount(ResourceBarElement.SHEEP, game.getCurrentPlayer().getNumberOfResourceType(ResourceType.SHEEP));

		// update number of soldiers
		getView().setElementAmount(ResourceBarElement.SOLDIERS, game.getCurrentPlayer().getSoldiers()); // is this soldiers in hand or soldiers played?

	}
}

