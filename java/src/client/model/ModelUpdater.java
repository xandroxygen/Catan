package client.model;

import org.json.simple.JSONObject;

/**
 * ModelUpdater class.
 */
public class ModelUpdater {
	
	/**
	 * Updates model class.
	 *
	 * @pre <pre>
	 *     json is not null
	 *     json should look like this ...
	 *
	 *     ClientModel {
	 *     	bank (ResourceList): The cards available to be distributed to the players.
	 *     	chat (MessageList): All the chat messages.,
	 *     	log (MessageList): All the log messages.,
	 *     	map (Map),
	 *     	players (array[Player]),
	 *     	tradeOffer (TradeOffer, optional): The current trade offer, if there is one.,
	 *     	turnTracker (TurnTracker): This tracks who's turn it is and what action's being done.,
	 *     	version (index): The version of the model. This is incremented whenever anyone makes a move.,
	 *     	winner (index): This is -1 when nobody's won yet. When they have, it's their order index [0-3]
	 *     }
	 *     ResourceList {
	 *     	brick (integer),
	 *     	ore (integer),
	 *     	sheep (integer),
	 *     	wheat (integer),
	 *     	wood (integer)
	 *     }
	 *     MessageList {
	 *     	lines (array[MessageLine])
	 *     }
	 *     MessageLine {
	 *     	message (string),
	 *     	source (string)
	 *     }
	 *     Map {
	 *     	hexes (array[Hex]): A list of all the hexes on the grid - it's only land tiles,
	 *     	ports (array[Port]),
	 *     	roads (array[Road]),
	 *     	settlements (array[VertexObject]),
	 *     	cities (array[VertexObject]),
	 *     	radius (integer): The radius of the map (it includes the center hex, and the ocean hexes; pass	this into the hexgrid constructor),
	 *     	robber (HexLocation): The current location of the robber
	 *     }
	 *     Hex {
	 *     	location (HexLocation),
	 *     	resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What resource
	 *     	this tile gives - it's only here if the tile is not desert.,
	 *     	number (integer, optional): What number is on this tile. It's omitted if this is a desert hex.
	 *     }
	 *     HexLocation {
	 *     	x (integer),
	 *     	y (integer)
	 *     }
	 *     Port {
	 *      resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What type
	 *     	resource this port trades for. If it's omitted, then it's for any resource.,
	 *     	location (HexLocation): Which hex this port is on. This shows the (ocean/non-existent) hex to
	 *     	draw the port on.,
	 *     	direction (string) = ['NW' or 'N' or 'NE' or 'E' or 'SE' or 'SW']: Which edge this port is on.,
	 *     	ratio (integer): The ratio for trade in (ie, if this is 2, then it's a 2:1 port.
	 *     }
	 *     EdgeValue {
	 *     	owner (index): The index (not id) of the player who owns this piece (0-3),
	 *     	location (EdgeLocation): The location of this road.
	 *     }
	 *     EdgeLocation {
	 *     	x (integer),
	 *     	y (integer),
	 *     	direction (string) = ['NW' or 'N' or 'NE' or 'SW' or 'S' or 'SE']
	 *     }
	 *     VertexObject {
	 *     	owner (index): The index (not id) of the player who owns thie piece (0-3),
	 *     	location (EdgeLocation): The location of this road.
	 *     }
	 *     Player {
	 *     	cities (number): How many cities this player has left to play,
	 *     	color (string): The color of this player.,
	 *     	discarded (boolean): Whether this player has discarded or not already this discard phase.,
	 *     	monuments (number): How many monuments this player has played.,
	 *     	name (string),
	 *     	newDevCards (DevCardList): The dev cards the player bought this turn.,
	 *     	oldDevCards (DevCardList): The dev cards the player had when the turn started.,
	 *		playerIndex (index): What place in the array is this player? 0-3. It determines their turn order. This is used often everywhere.,
	 *  	playedDevCard (boolean): Whether the player has played a dev card this turn.,
	 *  	playerID (integer): The unique playerID. This is used to pick the client player apart from the
	 *  	others. This is only used here and in your cookie.,
	 *  	resources (ResourceList): The resource cards this player has.,
	 *  	roads (number),
	 *  	settlements (integer),
	 *		soldier(integer),
	 *		victoryPoints (integer)
	 * 	   }
	 *	   DevCardList {
	 * 		monopoly (number),
	 * 		monument (number),
	 * 		roadBuilding (number),
	 * 		soldier (number),
	 * 		yearOfPlenty (number)
	 * 		}
	 * 	   TradeOffer {
	 *		sender (integer): The index of the person offering the trade
	 * 		receiver (integer): The index of the person the trade was offered to.,
	 * 		offer (ResourceList): Positive numbers are resources being offered. Negative are resources
	 * 		being asked for.
	 *	   }
	 * 	   TurnTracker {
	 * 		currentTurn (index): Who's turn it is (0-3),
	 * 		status (string) = ['Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or
	 * 		'SecondRound']: What's happening now,
	 * 		longestRoad (index): The index of who has the longest road, -1 if no one has it
	 * 		largestArmy (index): The index of who has the biggest army (3 or more), -1 if no one has it
	 * 	   }
	 * 	</pre>
	 *
	 * @post <pre>
	 * 		the model will be updated according to the json passed into the function
	 * </pre>
	 *
	 * @param json 
	 */
	public void updateModel(JSONObject json) {
		
	}
	
	/**
	 * Updates bank class.
	 *
	 * @pre <pre>
	 *     json is not null
	 *     json should look like this ...
	 *
	 *	   ResourceList {
	 *		brick (integer),
	 *		ore (integer),
	 *		sheep (integer),
	 *		wheat (integer),
	 *		wood (integer)
	 *	   }
	 * 	</pre>
	 *
	 * @post <pre>
	 *		the bank will be updated according to the json passed into the function
	 * </pre>
	 *
	 * @param json
	 */
	public void updateBank(JSONObject json) {
		
	}
	
	/**
	 * Updates player classes.
	 *
	 * @pre <pre>
	 *     json is not null
	 *     json should look like this ...
	 *
	 *     Player {
	 *     	cities (number): How many cities this player has left to play,
	 *     	color (string): The color of this player.,
	 *     	discarded (boolean): Whether this player has discarded or not already this discard phase.,
	 *     	monuments (number): How many monuments this player has played.,
	 *     	name (string),
	 *     	newDevCards (DevCardList): The dev cards the player bought this turn.,
	 *     	oldDevCards (DevCardList): The dev cards the player had when the turn started.,
	 *		playerIndex (index): What place in the array is this player? 0-3. It determines their turn order. This is used often everywhere.,
	 *  	playedDevCard (boolean): Whether the player has played a dev card this turn.,
	 *  	playerID (integer): The unique playerID. This is used to pick the client player apart from the
	 *  	others. This is only used here and in your cookie.,
	 *  	resources (ResourceList): The resource cards this player has.,
	 *  	roads (number),
	 *  	settlements (integer),
	 *		soldier(integer),
	 *		victoryPoints (integer)
	 * 	   }
	 * 	</pre>
	 *
	 * @post <pre>
	 *		the player will be updated according to the json passed into the function
	 * </pre>
	 *
	 * @param json
	 */
	public void updatePlayer(JSONObject json) {
		
	}
	
	/**
	 * Updates game class.
	 *
	 * @pre <pre>
	 *     json is not null
	 *     json should look like this ...
	 *
	 *	   ClientModel {
	 *     	bank (ResourceList): The cards available to be distributed to the players.
	 *     	chat (MessageList): All the chat messages.,
	 *     	log (MessageList): All the log messages.,
	 *     	map (Map),
	 *     	players (array[Player]),
	 *     	tradeOffer (TradeOffer, optional): The current trade offer, if there is one.,
	 *     	turnTracker (TurnTracker): This tracks who's turn it is and what action's being done.,
	 *     	version (index): The version of the model. This is incremented whenever anyone makes a move.,
	 *     	winner (index): This is -1 when nobody's won yet. When they have, it's their order index [0-3]
	 *     }
	 *     MessageList {
	 *     	lines (array[MessageLine])
	 *     }
	 *     MessageLine {
	 *     	message (string),
	 *     	source (string)
	 *     }
	 *     TradeOffer {
	 *		sender (integer): The index of the person offering the trade
	 * 		receiver (integer): The index of the person the trade was offered to.,
	 * 		offer (ResourceList): Positive numbers are resources being offered. Negative are resources
	 * 		being asked for.
	 *	   }
	 * 	   TurnTracker {
	 * 		currentTurn (index): Who's turn it is (0-3),
	 * 		status (string) = ['Rolling' or 'Robbing' or 'Playing' or 'Discarding' or 'FirstRound' or
	 * 		'SecondRound']: What's happening now,
	 * 		longestRoad (index): The index of who has the longest road, -1 if no one has it
	 * 		largestArmy (index): The index of who has the biggest army (3 or more), -1 if no one has it
	 * 	   }
	 *
	 * 	</pre>
	 *
	 * @post <pre>
	 *		the game will be updated according to the json passed into the function
	 * </pre>
	 *
	 * @param json
	 */
	public void updateGame(JSONObject json) {
		
	}
	
	/**
	 * Updates map class.
	 *
	 * @pre <pre>
	 *     json is not null
	 *     json should look like this ...
	 *
	 *     Map {
	 *     	hexes (array[Hex]): A list of all the hexes on the grid - it's only land tiles,
	 *     	ports (array[Port]),
	 *     	roads (array[Road]),
	 *     	settlements (array[VertexObject]),
	 *     	cities (array[VertexObject]),
	 *     	radius (integer): The radius of the map (it includes the center hex, and the ocean hexes; pass	this into the hexgrid constructor),
	 *     	robber (HexLocation): The current location of the robber
	 *     }
	 *     Hex {
	 *     	location (HexLocation),
	 *     	resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What resource
	 *     	this tile gives - it's only here if the tile is not desert.,
	 *     	number (integer, optional): What number is on this tile. It's omitted if this is a desert hex.
	 *     }
	 *     HexLocation {
	 *     	x (integer),
	 *     	y (integer)
	 *     }
	 *     Port {
	 *      resource (string, optional) = ['Wood' or 'Brick' or 'Sheep' or 'Wheat' or 'Ore']: What type
	 *     	resource this port trades for. If it's omitted, then it's for any resource.,
	 *     	location (HexLocation): Which hex this port is on. This shows the (ocean/non-existent) hex to
	 *     	draw the port on.,
	 *     	direction (string) = ['NW' or 'N' or 'NE' or 'E' or 'SE' or 'SW']: Which edge this port is on.,
	 *     	ratio (integer): The ratio for trade in (ie, if this is 2, then it's a 2:1 port.
	 *     }
	 *     EdgeValue {
	 *     	owner (index): The index (not id) of the player who owns this piece (0-3),
	 *     	location (EdgeLocation): The location of this road.
	 *     }
	 *     EdgeLocation {
	 *     	x (integer),
	 *     	y (integer),
	 *     	direction (string) = ['NW' or 'N' or 'NE' or 'SW' or 'S' or 'SE']
	 *     }
	 *     VertexObject {
	 *     	owner (index): The index (not id) of the player who owns thie piece (0-3),
	 *     	location (EdgeLocation): The location of this road.
	 *     }
	 * 	</pre>
	 *
	 * @post <pre>
	 *		the map will be updated according to the json passed into the function
	 * </pre>
	 *
	 * @param json
	 */
	public void updateMap(JSONObject json) {
		
	}
}
