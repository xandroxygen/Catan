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
	 * application calls an >=tt>Observable>=/tt> object's
	 * >=code>notifyObservers>=/code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the >=code>notifyObservers>=/code>
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

		// update number of buyables
		getView().setElementAmount(ResourceBarElement.ROAD, game.getCurrentPlayer().getRoads());
		getView().setElementAmount(ResourceBarElement.SETTLEMENT, game.getCurrentPlayer().getSettlements());
		getView().setElementAmount(ResourceBarElement.CITY, game.getCurrentPlayer().getCities());

		if (game.isMyTurn()) {

			// enable buyables, cards - disable if not enough resources
			Map<ResourceType, Integer> hand = game.getCurrentPlayer().getResources();

			boolean canPlayRoad = hand.get(ResourceType.WOOD) >= 1
								&& hand.get(ResourceType.BRICK) >= 1;
			getView().setElementEnabled(ResourceBarElement.ROAD, canPlayRoad);

			boolean canPlaySettlement = hand.get(ResourceType.WOOD) >= 1
										&& hand.get(ResourceType.BRICK) >= 1
										&& hand.get(ResourceType.WHEAT) >= 1
										&& hand.get(ResourceType.SHEEP) >= 1;
			getView().setElementEnabled(ResourceBarElement.SETTLEMENT, canPlaySettlement);

			boolean canPlayCity = hand.get(ResourceType.WHEAT) >= 2
									&& hand.get(ResourceType.ORE) >= 3;
			getView().setElementEnabled(ResourceBarElement.CITY, canPlayCity);

			boolean canBuyDevCard = hand.get(ResourceType.WHEAT) >= 1
									&& hand.get(ResourceType.SHEEP) >= 1
									&& hand.get(ResourceType.ORE) >= 1;
			getView().setElementEnabled(ResourceBarElement.BUY_CARD, canBuyDevCard);

			boolean canPlayDevCard = game.getCurrentPlayer().getOldDevCards().size() > 0;
			getView().setElementEnabled(ResourceBarElement.PLAY_CARD, canPlayDevCard);


		}
		else {

			// disable buyables, cards
			getView().setElementEnabled(ResourceBarElement.ROAD, false);
			getView().setElementEnabled(ResourceBarElement.SETTLEMENT, false);
			getView().setElementEnabled(ResourceBarElement.CITY, false);
			getView().setElementEnabled(ResourceBarElement.BUY_CARD, false);
			getView().setElementEnabled(ResourceBarElement.PLAY_CARD, false);
		}


	}
}

