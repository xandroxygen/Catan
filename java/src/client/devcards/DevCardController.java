package client.devcards;

import client.base.Controller;
import client.base.IAction;
import shared.model.InvalidActionException;
import client.model.Model;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {
		super(view);
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		if(Model.getInstance().canBuyDevelopmentCard(Model.getInstance().getGame().getCurrentPlayer().getPlayerID())) {
				getBuyCardView().showModal();
		}
	}

	@Override
	public void cancelBuyCard() {
		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {
		if(Model.getInstance().canBuyDevelopmentCard(Model.getInstance().getGame().getCurrentPlayer().getPlayerID())) {
			try {
				Model.getInstance().getServer().buyDevCard();
				getBuyCardView().closeModal();
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}

    public void setPlayDevCard(DevCardType devCardType) {
		if (devCardType != DevCardType.MONUMENT) {
			getPlayCardView().setCardEnabled(devCardType,
					Model.getInstance().getGame().getCurrentPlayer().getOldDevCards().get(devCardType) != 0 &&
					!Model.getInstance().getGame().getCurrentPlayer().isPlayedDevCard());

		}
		else{
			getPlayCardView().setCardEnabled(devCardType,
					Model.getInstance().getGame().getCurrentPlayer().getOldDevCards().get(devCardType) +
					Model.getInstance().getGame().getCurrentPlayer().getNewDevCards().get(devCardType) != 0);
		}
		getPlayCardView().setCardAmount(devCardType,
				Model.getInstance().getGame().getCurrentPlayer().getOldDevCards().get(devCardType) +
				Model.getInstance().getGame().getCurrentPlayer().getNewDevCards().get(devCardType));
	}

	@Override
	public void startPlayCard() {
		getPlayCardView().showModal();
        setPlayDevCard(DevCardType.SOLDIER);
        setPlayDevCard(DevCardType.MONOPOLY);
        setPlayDevCard(DevCardType.ROAD_BUILD);
		setPlayDevCard(DevCardType.YEAR_OF_PLENTY);
		setPlayDevCard(DevCardType.MONUMENT);
	}

	@Override
	public void cancelPlayCard() {
		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		if (Model.getInstance().canPlayMonopolyCard(Model.getInstance().getGame().getCurrentPlayer().getPlayerID(), resource)) {
			try {
				Model.getInstance().getServer().playMonopoly(resource);
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void playMonumentCard() {
		if (Model.getInstance().canPlayMonumentCard(Model.getInstance().getGame().getCurrentPlayer().getPlayerID())) {
			try {
				Model.getInstance().getServer().playVictoryPoint();
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void playRoadBuildCard() {
		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {
		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		if (Model.getInstance().canPlayYearOfPlenty(Model.getInstance().getGame().getCurrentPlayer().getPlayerID(), resource1, resource2)) {
			try {
				Model.getInstance().getServer().playYearOfPlenty(resource1, resource2);
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}
}

