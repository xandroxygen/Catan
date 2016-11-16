package server.http;

import com.sun.net.httpserver.HttpServer;
import server.command.moves.BuyDevCardCommand;
import server.facade.IServerFacade;
import server.facade.ServerFacade;
import server.http.handlers.ExampleHandler;
import server.http.handlers.SwaggerHandler;
import server.http.handlers.game.AddAIHandler;
import server.http.handlers.game.ListAIHandler;
import server.http.handlers.game.ModelHandler;
import server.http.handlers.games.CreateHandler;
import server.http.handlers.games.JoinHandler;
import server.http.handlers.games.ListHandler;
import server.http.handlers.moves.*;
import server.http.handlers.user.LoginHandler;
import server.http.handlers.user.RegisterHandler;

import java.net.InetSocketAddress;

/**
 * This is the entry point for our server implementation.
 * All requests from the client route through the HttpServer contained in this class.
 * All endpoint groups must be registered, and handlers created.
 * Specific endpoints are laid out in the Handler classes.
 *
 */
public class CatanServer {

	public static void main(String[] args) throws Exception {

		IServerFacade facade = new ServerFacade();

		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8082), 0);

		// --- ENDPOINTS ---

		// -- SWAGGER --
		//server.createContext("/", 						new ExampleHandler("Catan is online"));
		server.createContext("/docs/api/data", 			new SwaggerHandler.JSONAppender(""));
		server.createContext("/docs/api/view", 			new SwaggerHandler.BasicFile(""));


		// -- NON-MOVE OPERATIONS
		server.createContext("/user/login", 			new LoginHandler(facade));
		server.createContext("/user/register",			new RegisterHandler(facade));

		server.createContext("/games/list", 			new ListHandler(facade));
		server.createContext("/games/create", 			new CreateHandler(facade));
		server.createContext("/games/join", 			new JoinHandler(facade));

		server.createContext("/game/model", 			new ModelHandler(facade));
		server.createContext("/game/addAI", 			new AddAIHandler(facade));
		server.createContext("/game/listAI", 			new ListAIHandler(facade));

		// -- MOVE OPERATIONS --
		server.createContext("/moves/sendChat", 		new SendChatHandler(facade));
		server.createContext("/moves/rollNumber", 		new RollHandler(facade));
		server.createContext("/moves/robPlayer", 		new RobHandler(facade));
		server.createContext("/moves/finishTurn", 		new TurnHandler(facade));

		server.createContext("/moves/buyDevCard", 		new DevCardHandler(facade));
		server.createContext("/moves/Year_Of_Plenty", 	new YearOfPlentyHandler(facade));
		server.createContext("/moves/Road_Building", 	new RoadHandler(facade));
		server.createContext("/moves/Soldier", 			new SoldierHandler(facade));
		server.createContext("/moves/Monopoly", 		new MonopolyHandler(facade));
		server.createContext("/moves/Monument", 		new MonumentHandler(facade));

		server.createContext("/moves/buildRoad", 		new BuildRoadHandler(facade));
		server.createContext("/moves/buildCity", 		new BuildCityHandler(facade));
		server.createContext("/moves/buildSettlement", 	new BuildSettlementHandler(facade));

		server.createContext("/moves/offerTrade", 		new OfferTradeHandler(facade));
		server.createContext("/moves/acceptTrade", 		new AcceptTradeHandler(facade));
		server.createContext("/moves/maritimeTrade", 	new MaritimeTradeHandler(facade));
		server.createContext("/moves/discardCards",		new DiscardHandler(facade));

		// -----------------
		server.setExecutor(null); // uses default
		server.start();
	}
}
