package client.domestic;

import shared.definitions.*;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import client.admin.GameAdministrator;
import client.base.*;
import client.data.PlayerInfo;
import client.misc.*;
import client.model.InvalidActionException;
import client.model.Model;
import client.model.Player;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController, Observer {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	
	private int woodCount = 0;
	private int brickCount = 0;
	private int sheepCount = 0;
	private int wheatCount = 0;
	private int oreCount = 0;

	private int sendWood = 0;
	private int sendBrick = 0;
	private int sendSheep = 0;
	private int sendWheat = 0;
	private int sendOre = 0;

	private int receiveWood = 0;
	private int receiveBrick = 0;
	private int receiveSheep = 0;
	private int receiveWheat = 0;
	private int receiveOre = 0;

	//False === sending, true === receiving
	private boolean woodStatus = false;
	private boolean brickStatus = false;
	private boolean sheepStatus = false;
	private boolean wheatStatus = false;
	private boolean oreStatus = false;

	private int tradingPartner = -1;
	
	private boolean playersSetUp;

	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
		
		Model.getInstance().addObserver(this);
		
		playersSetUp = false;
	}
	
	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}
	
	/**
	 * Retrieves all players that are not the current player and sets them in the trade overlay
	 */
	private void setPlayers() {
		PlayerInfo[] players = new PlayerInfo[3];
		int playerID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
		
		int count = 0;
		for(PlayerInfo p : GameAdministrator.getInstance().getCurrentGame().getPlayers()) {
			if(p.getId() != playerID) {
				players[count] = p;
				count++;
			}
		}
		
		getTradeOverlay().setPlayers(players);
	}

	@Override
	public void startTrade() {
		getTradeOverlay().reset();
		update(null, null);
		getTradeOverlay().setPlayerSelectionEnabled(true);
		getTradeOverlay().setResourceSelectionEnabled(true);
		getTradeOverlay().showModal();
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		switch (resource) {
			case BRICK:
				if(brickStatus) {
					receiveBrick--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, true, receiveBrick > 0);
				} 
				else {
					sendBrick--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, sendBrick < brickCount, sendBrick > 0);
				}
				break;
			case WOOD:
				if(woodStatus) {
					receiveWood--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, true, receiveWood > 0);
				} 
				else {
					sendWood--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, sendWood < woodCount, sendWood > 0);
				}
				break;
			case WHEAT:
				if(wheatStatus) {
					receiveWheat--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, true, receiveWheat > 0);
				} 
				else {
					sendWheat--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, sendWheat < wheatCount, sendWheat > 0);
				}
				break;
			case SHEEP:
				if(sheepStatus) {
					receiveSheep--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, true, receiveSheep > 0);
				} 
				else {
					sendSheep--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sendSheep < sheepCount, sendSheep > 0);
				}
				break;
			case ORE:
				if(oreStatus) {
					receiveOre--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, true, receiveOre > 0);
				} 
				else {
					sendOre--;
					getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, sendOre < oreCount, sendOre > 0);
				}
				break;
		}
		
		setPlayerToTradeWith(tradingPartner);
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		switch (resource) {
		case BRICK:
			if(brickStatus) {
				receiveBrick++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, receiveBrick < 50, receiveBrick > 0);
			} 
			else {
				sendBrick++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, sendBrick < brickCount, true);
			}
			break;
		case WOOD:
			if(woodStatus) {
				receiveWood++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, receiveWood < 50, receiveWood > 0);
			} 
			else {
				sendWood++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, sendWood < woodCount, true);
			}
			break;
		case WHEAT:
			if(wheatStatus) {
				receiveWheat++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, receiveWheat < 50, receiveWheat > 0);
			} 
			else {
				sendWheat++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, sendWheat < wheatCount, true);
			}
			break;
		case SHEEP:
			if(sheepStatus) {
				receiveSheep++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, receiveSheep < 50, receiveSheep > 0);
			} 
			else {
				sendSheep++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sendSheep < sheepCount, true);
			}
			break;
		case ORE:
			if(oreStatus) {
				receiveOre++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, receiveOre < 50, receiveOre > 0);
			} 
			else {
				sendOre++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, sendOre < oreCount, true);
			}
			break;
		}
		
		setPlayerToTradeWith(tradingPartner);
	}

	@Override
	public void sendTradeOffer() {
		HashMap<ResourceType, Integer> offer = setTradeAmounts();		

		try {
			int senderID = GameAdministrator.getInstance().getCurrentUser().getLocalPlayer().getId();
			int receiverID = Model.getInstance().getGame().getPlayerList().get(tradingPartner).getPlayerID();
			
			if(Model.getInstance().canTradeWithPlayer(senderID, receiverID, offer))
				Model.getInstance().getServer().offerTrade(offer, tradingPartner);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
		reInitValues();
		
		getTradeOverlay().closeModal();
		
		//TODO: if the player receiving the trade offer is an AI, don't show the wait modal
		//getWaitOverlay().showModal();
	}
	
	/**
	 * Sets the amounts of each resource type to send or receive (a negative number signifies the amount to receive,
	 * a positive number signifies the amount to send)
	 * 
	 * @return a map containing the amounts to send/ receive for each resource type
	 */
	private HashMap<ResourceType, Integer> setTradeAmounts() {
		HashMap<ResourceType, Integer> offer = new HashMap<ResourceType, Integer>();
		
		if(receiveBrick > 0) {
			receiveBrick = -receiveBrick;
			offer.put(ResourceType.BRICK, receiveBrick);
		}
		else {
			offer.put(ResourceType.BRICK, sendBrick);
		}	
		if(receiveOre > 0) {
			receiveOre = -receiveOre;
			offer.put(ResourceType.ORE, receiveOre);
		}
		else {
			offer.put(ResourceType.ORE, sendOre);
		}
		if(receiveWood > 0) {
			receiveWood = -receiveWood;
			offer.put(ResourceType.WOOD, receiveWood);
		}
		else {
			offer.put(ResourceType.WOOD, sendWood);
		}
		if(receiveWheat > 0) {
			receiveWheat = -receiveWheat;
			offer.put(ResourceType.WHEAT, receiveWheat);
		}
		else {
			offer.put(ResourceType.WHEAT, sendWheat);
		}
		if(receiveSheep > 0) {
			receiveSheep = -receiveSheep;
			offer.put(ResourceType.SHEEP, receiveSheep);
		}
		else {
			offer.put(ResourceType.SHEEP, sendSheep);
		}
		
		return offer;
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {
		tradingPartner = playerIndex;
		
		if (tradingPartner == -1) {
			getTradeOverlay().setStateMessage("Select a player to trade with");
			getTradeOverlay().setTradeEnabled(false);
		} else if (sendBrick+sendWood+sendSheep+sendWheat+sendOre > 0 && receiveBrick+receiveWood+receiveOre+receiveSheep+receiveWheat > 0) {
			getTradeOverlay().setStateMessage("Trade!");
			getTradeOverlay().setTradeEnabled(true);
		}else if (!(sendBrick+sendWood+sendSheep+sendWheat+sendOre > 0)) {
			getTradeOverlay().setStateMessage("Select resource(s) to send");
			getTradeOverlay().setTradeEnabled(false);
		} else if (!(receiveBrick+receiveWood+receiveOre+receiveSheep+receiveWheat > 0)) {
			getTradeOverlay().setStateMessage("Select resource(s) to receive");
			getTradeOverlay().setTradeEnabled(false);
		}
		
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		switch (resource) {
			case BRICK:
				brickStatus = true;
				receiveBrick = 0;
				break;
			case WOOD:
				woodStatus = true;
				receiveWood = 0;
				break;
			case WHEAT:
				wheatStatus = true;
				receiveWheat = 0;
				break;
			case SHEEP:
				sheepStatus = true;
				receiveSheep = 0;
				break;
			case ORE:
				oreStatus = true;
				receiveOre = 0;
				break;
		}
		
		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		switch (resource) {
			case BRICK:
				brickStatus = false;
				sendBrick = 0;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, brickCount > 0, false);
				break;
			case WOOD:
				woodStatus = false;
				sendWood = 0;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, woodCount > 0 , false);
				break;
			case WHEAT:
				wheatStatus = false;
				sendWheat = 0;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, wheatCount > 0 , false);
				break;
			case SHEEP:
				sheepStatus = false;
				sendSheep = 0;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sheepCount > 0 , false);
				break;
			case ORE:
				oreStatus = false;
				sendOre = 0;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, oreCount > 0 , false);
				break;
		}
		
		getTradeOverlay().setResourceAmount(resource, "0");
	}

	@Override
	public void unsetResource(ResourceType resource) {
		switch (resource) {
			case BRICK:
				sendBrick = 0;
				receiveBrick = 0;
				break;
			case WOOD:
				sendWood = 0;
				receiveWood = 0;
				break;
			case SHEEP:
				sendSheep = 0;
				receiveSheep = 0;
				break;
			case WHEAT:
				sendWheat = 0;
				receiveWheat = 0;
				break;
			case ORE:
				sendOre = 0;
				receiveOre = 0;
				break;
		}
		
		if(sendBrick+sendWood+sendSheep+sendWheat+sendOre <= 0) {
			getTradeOverlay().setTradeEnabled(false);
			getTradeOverlay().setStateMessage("Select resource(s) to send");
		}
		if(receiveBrick+receiveWood+receiveSheep+receiveWheat+receiveOre <= 0) {
			getTradeOverlay().setTradeEnabled(false);
			getTradeOverlay().setStateMessage("Select resource(s) to receive");
		}
	}

	@Override
	public void cancelTrade() {
		getTradeOverlay().closeModal();
		getTradeOverlay().setPlayerSelectionEnabled(false);
		
		reInitValues();
		
		getTradeOverlay().setTradeEnabled(false);

		getTradeOverlay().setResourceAmount(ResourceType.BRICK, "0");
		getTradeOverlay().setResourceAmount(ResourceType.WOOD, "0");
		getTradeOverlay().setResourceAmount(ResourceType.WHEAT, "0");
		getTradeOverlay().setResourceAmount(ResourceType.ORE, "0");
		getTradeOverlay().setResourceAmount(ResourceType.SHEEP, "0");
		
		getTradeOverlay().setResourceSelectionEnabled(false);
		getTradeOverlay().setStateMessage("Select a player to trade with");
	}

	@Override
	public void acceptTrade(boolean willAccept) {
		if(getAcceptOverlay().isModalShowing()) {
			getAcceptOverlay().closeModal();
		}	
		getAcceptOverlay().reset();
		try {
			Model.getInstance().getServer().acceptTrade(willAccept);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// enable/ disable the button that lets the player open the trade modal
		getTradeView().enableDomesticTrade(Model.getInstance().canTrade());
		
		// set the players that the player can trade with
		if(!playersSetUp) {
			setPlayers();
			playersSetUp = true;
		}	
		
		updateResourceCounts();
		
		reInitValues();
		
		getTradeOverlay().setTradeEnabled(false);		
	}
	
	/**
	 * Updates the resource count for each resource
	 */
	private void updateResourceCounts() {
		Player p = Model.getInstance().getGame().getCurrentPlayer();
		
		brickCount = p.getNumberOfResourceType(ResourceType.BRICK);
		oreCount = p.getNumberOfResourceType(ResourceType.ORE);
		sheepCount = p.getNumberOfResourceType(ResourceType.SHEEP);
		wheatCount = p.getNumberOfResourceType(ResourceType.WHEAT);
		woodCount = p.getNumberOfResourceType(ResourceType.WOOD);
	}
	
	/**
	 * Re-initializes the values of sendResource, receiveResource, resourceStatus and tradePartner to their original, default values
	 */
	private void reInitValues() {
		sendWood = 0;
		sendBrick = 0;
		sendSheep = 0;
		sendWheat = 0;
		sendOre = 0;

		receiveWood = 0;
		receiveBrick = 0;
		receiveSheep = 0;
		receiveWheat = 0;
		receiveOre = 0;

		woodStatus = false;
		brickStatus = false;
		sheepStatus = false;
		wheatStatus = false;
		oreStatus = false;

		tradingPartner = -1;
	}

}

