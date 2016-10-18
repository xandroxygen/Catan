package client.maritime;

import shared.definitions.*;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import client.base.*;
import client.model.City;
import client.model.Game;
import client.model.InvalidActionException;
import client.model.Model;
import client.model.Player;
import client.model.Port;
import client.model.Settlement;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController, Observer {

	private IMaritimeTradeOverlay tradeOverlay;
	private Map<ResourceType, Integer> resourceRatios;
	
	private ResourceType input = null;
	private ResourceType output = null;
	private int ratio;
	
	
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		resourceRatios = new HashMap<>();
		setTradeOverlay(tradeOverlay);
		
		Model.getInstance().addObserver(this);
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {
		
		// Save pointer to current player
		Player player = Model.getInstance().getCurrentPlayer();
		
		
		getTradeOverlay().showGiveOptions(getGiveResources(player));
		
		
		// Disable the trade button
		getTradeOverlay().setTradeEnabled(false);
		
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {
		
		// Make trade through the server proxy
		Model.getInstance().maritimeTrade(ratio, input, output);
		resourceRatios.clear();
		
		// Disable the trade button
		getTradeOverlay().setTradeEnabled(false);
		getTradeOverlay().hideGetOptions();
		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {
		try {
			Model.getInstance().getServer().finishTurn();
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getTradeOverlay().closeModal();
		resourceRatios.clear();
	}

	@Override
	public void setGetResource(ResourceType resource) {
		getTradeOverlay().selectGetOption(resource,1) ;
		// Enable trade
		getTradeOverlay().setTradeEnabled(true);
		
		output = resource;
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		Player player = Model.getInstance().getCurrentPlayer();
		getTradeOverlay().selectGiveOption(resource, resourceRatios.get(resource));
		getTradeOverlay().showGetOptions(getGetResources(player));
		
		input = resource;
		ratio = resourceRatios.get(resource);
	}

	@Override
	public void unsetGetValue() {
		Player player = Model.getInstance().getCurrentPlayer();
		getTradeOverlay().hideGetOptions();
		getTradeOverlay().showGetOptions(getGetResources(player));
		
		//Disable trade
		getTradeOverlay().setTradeEnabled(false);
		output = null;
	}

	@Override
	public void unsetGiveValue() {
		Player player = Model.getInstance().getCurrentPlayer();
		getTradeOverlay().hideGetOptions();
		getTradeOverlay().hideGiveOptions();
		getTradeOverlay().showGiveOptions(getGiveResources(player));
		
		//Disable trade
		getTradeOverlay().setTradeEnabled(false);
		input = null;
		ratio = -1;
	}
	
	// HELPER METHODS
	private ResourceType[] getGiveResources(Player player) {
		client.model.Map map = Model.getInstance().getGame().getTheMap();
		
		Map<EdgeLocation, Port> portList = Model.getInstance().getGame().getTheMap().getPorts();
		ArrayList<ResourceType> enabledTypes = new ArrayList<>();
		
		
		// Look for ports.
		// TODO: Make the port finding more efficient. Probably by saving a list into the player class
		for (EdgeLocation key : portList.keySet()) {
			Port port = portList.get(key);
			if (map.edgeHasPlayerMunicipality(port.getLocation(), player)) {
				// Check for 2 of the port resource
				if (port.getRatio() == 2) {
					if (player.getNumberOfResourceType(port.getResource()) >= 2) {
						enabledTypes.add(port.getResource());
						resourceRatios.put(port.getResource(), 2);
					}
				}
				// Else if 3:1 port, check for 3 of everything
				else if (port.getRatio() == 3) {
					for (ResourceType resourceKey : player.getResources().keySet()) {
						if (player.getResources().get(resourceKey) >= 3) {
							enabledTypes.add(resourceKey);
							resourceRatios.put(resourceKey, 3);
						}
					}
				}
			}
		}
		
		// Check for 4 of anything to trade (any player can make this trade)
		for (ResourceType resourceKey : player.getResources().keySet()) {
			if (player.getResources().get(resourceKey) >= 4) {
				if (!enabledTypes.contains(resourceKey)) {  // DOES THIS CONTAINS WORK WITH ENUMERATED TYPES??
					enabledTypes.add(resourceKey);
					resourceRatios.put(resourceKey, 4);
				}
			}
		}
		
		ResourceType[] enabled = new ResourceType[enabledTypes.size()];
		enabledTypes.toArray(enabled);
		return enabled;
	}
	
	private ResourceType[] getGetResources(Player player) {
		ArrayList<ResourceType> enabledTypes = new ArrayList<>();
		for (ResourceType resourceKey : player.getResources().keySet()) {
			enabledTypes.add(resourceKey);
		}
		
		ResourceType[] enabled = new ResourceType[enabledTypes.size()];
		enabledTypes.toArray(enabled);
		return enabled;
	}

	@Override
	public void update(Observable o, Object arg) {
		Game game = (Game)arg;
		getTradeView().enableMaritimeTrade(game.isMyTurn());
	}

}

