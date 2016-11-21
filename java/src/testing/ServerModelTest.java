package testing;

import org.junit.Before;
import org.junit.Test;
import server.command.moves.RollNumberCommand;
import server.facade.ServerFacade;
import shared.definitions.CatanColor;
import shared.locations.*;

import static org.junit.Assert.*;

/**
 * Created by jbskaggs on 11/21/16.
 */
public class ServerModelTest {
    ServerFacade serverFacade = new ServerFacade();

    @Before
    public void setUp() throws Exception {
        serverFacade.userRegister("Jonathan", "jonathan");
        serverFacade.userRegister("Xander", "xander");
        serverFacade.userRegister("Spencer", "spencer");
        serverFacade.userRegister("Jeremy", "jeremy");

        serverFacade.userLogin("Jonathan", "jonathan");
        serverFacade.userLogin("Xander", "xander");
        serverFacade.userLogin("Spencer", "spencer");
        serverFacade.userLogin("Jeremy", "jeremy");

        serverFacade.gamesCreate("Game", false, false, false);

        serverFacade.gamesJoin(0, 0, CatanColor.RED);
        serverFacade.gamesJoin(0, 1, CatanColor.ORANGE);
        serverFacade.gamesJoin(0, 2, CatanColor.BLUE);
        serverFacade.gamesJoin(0, 3, CatanColor.GREEN);

        //firstRound
        serverFacade.buildRoad(0, 0, true, new EdgeLocation(new HexLocation(0,0), EdgeDirection.North));
        serverFacade.buildSettlement(0, 0, true, new VertexLocation(new HexLocation(0,0), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        serverFacade.buildRoad(0, 1, true, new EdgeLocation(new HexLocation(-1,0), EdgeDirection.North));
        serverFacade.buildSettlement(0, 1, true, new VertexLocation(new HexLocation(-1,0), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        serverFacade.buildRoad(0, 2, true, new EdgeLocation(new HexLocation(-2,0), EdgeDirection.North));
        serverFacade.buildSettlement(0, 2, true, new VertexLocation(new HexLocation(-2,0), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        serverFacade.buildRoad(0, 3, true, new EdgeLocation(new HexLocation(1,0), EdgeDirection.North));
        serverFacade.buildSettlement(0, 3, true, new VertexLocation(new HexLocation(1,0), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        //secondRound
        serverFacade.buildRoad(0, 3, true, new EdgeLocation(new HexLocation(2,0), EdgeDirection.North));
        serverFacade.buildSettlement(0, 3, true, new VertexLocation(new HexLocation(2,0), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        serverFacade.buildRoad(0, 2, true, new EdgeLocation(new HexLocation(0,-1), EdgeDirection.North));
        serverFacade.buildSettlement(0, 2, true, new VertexLocation(new HexLocation(0,-1), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        serverFacade.buildRoad(0, 1, true, new EdgeLocation(new HexLocation(0,1), EdgeDirection.North));
        serverFacade.buildSettlement(0, 1, true, new VertexLocation(new HexLocation(0,1), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

        serverFacade.buildRoad(0, 0, true, new EdgeLocation(new HexLocation(0,-2), EdgeDirection.North));
        serverFacade.buildSettlement(0, 0, true, new VertexLocation(new HexLocation(0,-2), VertexDirection.NorthWest));
        serverFacade.finishTurn(0);

    }

    @Test
    public void placeCity() throws Exception {

    }

    @Test
    public void placeSettlement() throws Exception {

    }

    @Test
    public void placeRoad() throws Exception {

    }

    @Test
    public void buyDevelopmentCard() throws Exception {

    }

    @Test
    public void playSoldierCard() throws Exception {

    }

    @Test
    public void playYearOfPlenty() throws Exception {

    }

    @Test
    public void playRoadCard() throws Exception {

    }

    @Test
    public void playMonopolyCard() throws Exception {

    }

    @Test
    public void playMonumentCard() throws Exception {

    }

    @Test
    public void rollDice() throws Exception {

    }

    @Test
    public void sendMessage() throws Exception {

    }

    @Test
    public void makeTradeOffer() throws Exception {

    }

    @Test
    public void acceptTradeOffer() throws Exception {

    }

    @Test
    public void makeMaritimeTrade() throws Exception {

    }

    @Test
    public void addPlayer() throws Exception {

    }

    @Test
    public void finishTurn() throws Exception {

    }

    @Test
    public void robPlayer() throws Exception {

    }

    @Test
    public void discardCards() throws Exception {

    }

    @Test
    public void createGame() throws Exception {

    }

    @Test
    public void listGames() throws Exception {

    }

    @Test
    public void getUpdatedGame() throws Exception {

    }


}