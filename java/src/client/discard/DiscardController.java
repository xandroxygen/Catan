package client.discard;

import shared.definitions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import client.base.*;
import client.misc.*;
import client.model.GameStatus;
import client.model.InvalidActionException;
import client.model.Model;
import client.model.Player;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController, Observer {

	private IWaitView waitView;
	
	private int totalRemaining;
	private int totalDiscarded;
	private int numToDiscard;
	
	private int discardedBrick;
    private int discardedOre;
    private int discardedSheep;
    private int discardedWheat;
    private int discardedWood;
    
    private int remainingBrick;
    private int remainingOre;
    private int remainingSheep;
    private int remainingWheat;
    private int remainingWood;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		Model.getInstance().addObserver(this);
		
		this.waitView = waitView;
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

	@Override
	public void increaseAmount(ResourceType resource) {
		switch (resource) {
	        case BRICK:
	            discardedBrick++;
	            remainingBrick--;
	            getDiscardView().setResourceDiscardAmount(resource, discardedBrick);
	            break;
	        case ORE:
	            discardedOre++;
	            remainingOre--;
	            getDiscardView().setResourceDiscardAmount(resource, discardedOre);
	            break;
	        case SHEEP:
	            discardedSheep++;
	            remainingSheep--;
	            getDiscardView().setResourceDiscardAmount(resource, discardedSheep);
	            break;
	        case WHEAT:
	            discardedWheat++;
	            remainingWheat--;
	            getDiscardView().setResourceDiscardAmount(resource, discardedWheat);
	            break;
	        case WOOD:
	            discardedWood++;
	            remainingWood--;
	            getDiscardView().setResourceDiscardAmount(resource, discardedWood);
	            break;
	        default:
	            break;
		}
		
	    totalDiscarded++;
	    totalRemaining--;
	    getDiscardView().setStateMessage("Discard: " + totalDiscarded + "/" + numToDiscard);
	    
	    if(totalDiscarded == numToDiscard) {
	        getDiscardView().setDiscardButtonEnabled(true);
	        
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, false, discardedBrick > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, false, discardedOre > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, false, discardedSheep > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, false, discardedWheat > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, false, discardedWood > 0);
	    } else {
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, remainingBrick > 0, discardedBrick > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, remainingOre > 0, discardedOre > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, remainingSheep > 0, discardedSheep > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, remainingWheat > 0, discardedWheat > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, remainingWood > 0, discardedWood > 0);
	    }
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		switch (resource) {
	        case BRICK:
	            discardedBrick--;
	            remainingBrick++;
	            getDiscardView().setResourceDiscardAmount(resource, discardedBrick);
	            break;
	        case ORE:
	            discardedOre--;
	            remainingOre++;
	            getDiscardView().setResourceDiscardAmount(resource, discardedOre);
	            break;
	        case SHEEP:
	            discardedSheep--;
	            remainingSheep++;
	            getDiscardView().setResourceDiscardAmount(resource, discardedSheep);
	            break;
	        case WHEAT:
	            discardedWheat--;
	            remainingWheat++;
	            getDiscardView().setResourceDiscardAmount(resource, discardedWheat);
	            break;
	        case WOOD:
	            discardedWood--;
	            remainingWood++;
	            getDiscardView().setResourceDiscardAmount(resource, discardedWood);
	            break;
	        default:
	            break;
	    }
		
	    totalDiscarded--;
	    totalRemaining++;
	    getDiscardView().setStateMessage("Discard: " + totalDiscarded + "/" + numToDiscard);
	    
	    if(totalDiscarded != numToDiscard) {
	        getDiscardView().setDiscardButtonEnabled(false);
	        
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, remainingBrick > 0, discardedBrick > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, remainingOre > 0, discardedOre > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, remainingSheep > 0, discardedSheep > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, remainingWheat > 0, discardedWheat > 0);
	        getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, remainingWood > 0, discardedWood > 0);
	    }
	}	

	@Override
	public void discard() {
		getDiscardView().closeModal();
		
		try {
			Model.getInstance().getServer().discardCards(fillHand());
		} catch (InvalidActionException e) {
			// TODO Properly catch error
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(Model.getInstance().getGame().getTurnTracker().getStatus() == GameStatus.Discarding) {
			initFromModel();
		}
		else if(getWaitView().isModalShowing()) {
                getWaitView().closeModal();
        }
	}

	private void initFromModel() {
		totalDiscarded = 0;
        discardedBrick = 0;
        discardedOre = 0;
        discardedSheep = 0;
        discardedWheat = 0;
        discardedWood = 0;
        
        Player p = Model.getInstance().getGame().getCurrentPlayer();
        
        remainingBrick = p.getNumberOfResourceType(ResourceType.BRICK);
        remainingOre = p.getNumberOfResourceType(ResourceType.ORE);
        remainingSheep = p.getNumberOfResourceType(ResourceType.SHEEP);
        remainingWheat= p.getNumberOfResourceType(ResourceType.WHEAT);
        remainingWood= p.getNumberOfResourceType(ResourceType.WOOD);
        totalRemaining = remainingBrick + remainingOre + remainingSheep + remainingWheat + remainingWood;
        
        numToDiscard = totalRemaining / 2;
        
        if(!p.isDiscarded()) {
        	if(!getDiscardView().isModalShowing()) {
        		getDiscardView().setStateMessage("Discard: " + totalDiscarded + "/" + numToDiscard);
        		
        		getDiscardView().setDiscardButtonEnabled(false);
            	
            	getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, remainingBrick > 0, false);
            	getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, remainingOre > 0, false);
            	getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, remainingSheep > 0, false);
            	getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, remainingWheat > 0, false);
            	getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, remainingWood > 0, false);
            	
            	setResourceDiscardAmount();
            	
            	setResourceMaxAmount();
            	
            	getDiscardView().showModal();
        	}       	
        }
        else if(!getWaitView().isModalShowing()) { 
    		getWaitView().setMessage("Your fellow players are a tad slower than you... Just hang tight.");
    		getWaitView().showModal();
        } 
	}
	
	private void setResourceDiscardAmount() {
		getDiscardView().setResourceDiscardAmount(ResourceType.BRICK, 0);
    	getDiscardView().setResourceDiscardAmount(ResourceType.ORE, 0);
    	getDiscardView().setResourceDiscardAmount(ResourceType.SHEEP, 0);
    	getDiscardView().setResourceDiscardAmount(ResourceType.WHEAT, 0);
    	getDiscardView().setResourceDiscardAmount(ResourceType.WOOD, 0);
	}
	
	private void setResourceMaxAmount() {
		getDiscardView().setResourceMaxAmount(ResourceType.BRICK, remainingBrick);
    	getDiscardView().setResourceMaxAmount(ResourceType.ORE, remainingOre);
    	getDiscardView().setResourceMaxAmount(ResourceType.SHEEP, remainingSheep);
    	getDiscardView().setResourceMaxAmount(ResourceType.WHEAT, remainingWheat);
    	getDiscardView().setResourceMaxAmount(ResourceType.WOOD, remainingWood);
	}
	
	private Map<ResourceType, Integer> fillHand() {
		Map<ResourceType,Integer> hand = new HashMap<ResourceType, Integer>();
		
		hand.put(ResourceType.BRICK, discardedBrick);
		hand.put(ResourceType.ORE, discardedOre);
		hand.put(ResourceType.SHEEP, discardedSheep);
		hand.put(ResourceType.WHEAT, discardedWheat);
		hand.put(ResourceType.WOOD, discardedWood);
		
		return hand;
	}
}

