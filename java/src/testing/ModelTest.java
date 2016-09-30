package testing;

import client.model.Model;
import client.model.Player;
import org.junit.Test;
import shared.definitions.ResourceType;

import java.util.HashMap;

/**
 * Created by Jonathan Skaggs on 9/30/2016.
 */
public class ModelTest {
    @Test
    public void canBuyDevelopmentCard() throws Exception {
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            Model.getInstance().getGame().getBank().;
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
        }
    }
}
