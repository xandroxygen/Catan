package server.http;

import com.sun.net.httpserver.HttpServer;
import server.facade.IServerFacade;
import server.facade.ServerFacade;
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
import server.persistence.ClassLoader;
import server.persistence.IPersistenceProvider;
import server.persistence.Persistence;

import java.io.BufferedReader;
import java.io.FileReader;
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

		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8081), 0);

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
		getPluginPersistence(args);
		server.start();
	}

	public static void getPluginPersistence(String[] args){
		String persistenceType = args[0];
		int numberOfCommands = Integer.parseInt(args[1]);

		//get plugin path and class path
		String pluginPath = "";
		String classPath = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("java\\config.txt"));
			String text;

			while ((text = reader.readLine()) != null) {
				String[] array = text.split(" ");
				if (array.length == 3 && persistenceType.toLowerCase().equals(array[0].toLowerCase())){
					pluginPath = array[1];
					classPath = array[2];
					break;
				}
			}
			if (text == null){
				System.out.print("That type of persistence is not permitted");
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


        //make the instance of PP
		try {
			Class<?> plugin = ClassLoader.loadClass(pluginPath, classPath);
			Persistence.setPersistence((IPersistenceProvider) plugin.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
//			//IPersistenceProvider persistenceProvider;
//			File file = new File("java\\src\\plugins\\relational");
//			URL[] jarUrl = new URL[]{new URL("file:"+file.getAbsolutePath())};
//			URLClassLoader urlClassLoader = new URLClassLoader(jarUrl);
////			Class<?> plugin = urlClassLoader.loadClass("plugins.relational.PersistenceProvider");
////			Constructor<?> constructor = plugin.getConstructor();
////			Object tempObj = constructor.newInstance();
////			IPersistenceProvider p = (IPersistenceProvider) tempObj;
////			int five = p.returnFive();
//
//			Class<?> plugin = Class.forName("plugins.relational.PersistenceProvider", true, urlClassLoader);
//			
//			IPersistenceProvider p1 = (IPersistenceProvider) plugin.newInstance();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        //load data from database

        //set command number
	}
}
