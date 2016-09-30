package testing;

import client.model.Model;
import client.model.Player;
import client.server.MockServerProxy;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Jonathan Skaggs on 9/30/2016.
 */
public class ModelTest {
    @Test
    public void canBuyDevelopmentCard() throws Exception {
        MockServerProxy mockProxy = new MockServerProxy();
        JsonObject newModel = new JsonParser().parse(mockProxy.testModel1).getAsJsonObject();
        Model.getInstance().updateModel(newModel);

        //all prerequisites are met/ testing if it is not the players turn
        int i = 0;
        HashMap<DevCardType, Integer> devCardsInBank = new HashMap<>();
        devCardsInBank.put(DevCardType.SOLDIER, 3);
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
            if(i == Model.getInstance().getGame().getCurrentTurnIndex()){
                assertTrue(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            } else {
                assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            }
            i++;
        }
        //all prerequisites are met except their resources
        i = 0;
        devCardsInBank = new HashMap<>();
        devCardsInBank.put(DevCardType.SOLDIER, 3);
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            tempPlayer.setResources(resourceHand);
            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            i++;
        }
        //tests if there are no cards in the bank
        i = 0;
        devCardsInBank = new HashMap<>();
        Model.getInstance().getGame().getBank().setDevelopmentCards(devCardsInBank);
        for(Player tempPlayer: Model.getInstance().getGame().playerList){
            HashMap<ResourceType, Integer> resourceHand = new HashMap<>();
            resourceHand.put(ResourceType.ORE, 1);
            resourceHand.put(ResourceType.WHEAT, 1);
            resourceHand.put(ResourceType.SHEEP, 1);
            tempPlayer.setResources(resourceHand);
            assertFalse(Model.getInstance().canBuyDevelopmentCard(tempPlayer.getPlayerId()));
            i++;
        }
    }
}
