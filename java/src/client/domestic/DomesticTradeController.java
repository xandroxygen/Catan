package client.domestic;

import shared.definitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import client.base.*;
import client.misc.*;
import client.model.InvalidActionException;
import client.model.Model;


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

	//False === sending, true -> receiving
	private boolean woodStatus = false;
	private boolean brickStatus = false;
	private boolean sheepStatus = false;
	private boolean wheatStatus = false;
	private boolean oreStatus = false;

	private int tradePartner = -1;

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
		if (sendBrick+sendWood+sendSheep+sendWheat+sendOre > 0 && receiveBrick+receiveWood+receiveOre+receiveSheep+receiveWheat > 0 && tradePartner != -1) {
			getTradeOverlay().setTradeEnabled(true);
			getTradeOverlay().setStateMessage("Trade!");
		} else if (tradePartner == -1) {
			getTradeOverlay().setStateMessage("Select player to trade with");
			getTradeOverlay().setTradeEnabled(false);
		} else if (!(sendBrick+sendWood+sendSheep+sendWheat+sendOre > 0)) {
			getTradeOverlay().setStateMessage("Select resource to send to another player");
			getTradeOverlay().setTradeEnabled(false);
		} else if (!(receiveBrick+receiveWood+receiveOre+receiveSheep+receiveWheat > 0)) {
			getTradeOverlay().setStateMessage("Select resource to receive from another player");
			getTradeOverlay().setTradeEnabled(false);
		}
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		switch (resource) {
		case BRICK:
			if(brickStatus) {
				receiveBrick++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, receiveBrick < brickCount, receiveBrick > 0);
			} 
			else {
				sendBrick++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.BRICK, sendBrick < brickCount, true);
			}
			break;
		case WOOD:
			if(woodStatus) {
				receiveWood++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, receiveWood < woodCount, receiveWood > 0);
			} 
			else {
				sendWood++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WOOD, sendWood < woodCount, true);
			}
			break;
		case WHEAT:
			if(wheatStatus) {
				receiveWheat++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, receiveWheat < wheatCount, receiveWheat > 0);
			} 
			else {
				sendWheat++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.WHEAT, sendWheat < wheatCount, true);
			}
			break;
		case SHEEP:
			if(sheepStatus) {
				receiveSheep++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, receiveSheep < sheepCount, receiveSheep > 0);
			} 
			else {
				sendSheep++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.SHEEP, sendSheep < sheepCount, true);
			}
			break;
		case ORE:
			if(oreStatus) {
				receiveOre++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, receiveOre < oreCount, receiveOre > 0);
			} 
			else {
				sendOre++;
				getTradeOverlay().setResourceAmountChangeEnabled(ResourceType.ORE, sendOre < oreCount, true);
			}
			break;
		}
		if (sendBrick+sendWood+sendSheep+sendWheat+sendOre > 0 && receiveBrick+receiveWood+receiveOre+receiveSheep+receiveWheat > 0 && tradePartner != -1) {
			getTradeOverlay().setStateMessage("Trade!");
			getTradeOverlay().setTradeEnabled(true);
		} else if (tradePartner == -1) {
			getTradeOverlay().setStateMessage("Select player to trade with");
			getTradeOverlay().setTradeEnabled(false);
		} else if (!(sendBrick+sendWood+sendSheep+sendWheat+sendOre > 0)) {
			getTradeOverlay().setStateMessage("Select resource(s) to send to another player");
			getTradeOverlay().setTradeEnabled(false);
		} else if (!(receiveBrick+receiveWood+receiveOre+receiveSheep+receiveWheat > 0)) {
			getTradeOverlay().setStateMessage("Select resource(s) to receive from another player");
			getTradeOverlay().setTradeEnabled(false);
		}
	}

	@Override
	public void sendTradeOffer() {
		Map<ResourceType, Integer> offer = setTradeAmounts();		

		try {
			Model.getInstance().getServer().offerTrade(offer, tradePartner);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		}
		reInitValues();
		
		getTradeOverlay().closeModal();
		getWaitOverlay().showModal();
	}
	
	/**
	 * Sets the amounts of each resource type to send or receive
	 * 
	 * @return a map containing the amounts to send/ receive for each resource type
	 */
	private Map<ResourceType, Integer> setTradeAmounts() {
		Map<ResourceType, Integer> offer = new HashMap<ResourceType, Integer>();
		
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

	}

	@Override
	public void setResourceToReceive(ResourceType resource) {

	}

	@Override
	public void setResourceToSend(ResourceType resource) {

	}

	@Override
	public void unsetResource(ResourceType resource) {

	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {

		getAcceptOverlay().closeModal();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// set players
		
		//enable domestic trade
		
		getResourceCounts();
		
		reInitValues();
		
		getTradeOverlay().setTradeEnabled(false);
		
		
	}
	
	private void getResourceCounts() {
		woodCount = Model.getInstance().getGame().getCurrentPlayer().getNumberOfResourceType(ResourceType.BRICK);
		oreCount = Model.getInstance().getGame().getCurrentPlayer().getNumberOfResourceType(ResourceType.ORE);
		sheepCount = Model.getInstance().getGame().getCurrentPlayer().getNumberOfResourceType(ResourceType.SHEEP);
		wheatCount = Model.getInstance().getGame().getCurrentPlayer().getNumberOfResourceType(ResourceType.WHEAT);
		woodCount = Model.getInstance().getGame().getCurrentPlayer().getNumberOfResourceType(ResourceType.WOOD);
	}
	
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

		tradePartner = -1;
	}

}

