package server.http;

import client.communication.LogEntry;
import client.server.Serializer;
import com.google.gson.*;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.*;
import java.util.List;

/**
 * Central place for all serialization related functions,
 * mostly for the model and its related classes.
 */
public class ModelSerializer {

		public static String serializeGame(Game game) {

			JsonObject object = new JsonObject();

			// bank of resources
			JsonObject bank = new JsonObject();
			for (ResourceType resource : game.getBank().getResourceDeck().keySet()) {

				bank.addProperty(resource.toString().toLowerCase(), game.getBank().getResourceDeck().get(resource));
			}
			object.add("bank", bank);

			// deck of dev cards
			JsonObject deck = new JsonObject();
			for (DevCardType devCardType : game.getBank().getDevelopmentCards().keySet()) {

				String cardType;

				// change card_type to camelCase
				if (devCardType == DevCardType.YEAR_OF_PLENTY) {
					cardType = "yearOfPlenty";
				}
				else if (devCardType == DevCardType.ROAD_BUILD) {
					cardType = "roadBuilding";
				}
				else {
					cardType = devCardType.toString().toLowerCase();
				}


				deck.addProperty(cardType, game.getBank().getDevelopmentCards().get(devCardType));
			}
			object.add("deck", deck);

			// map
			object.add("map", serializeMap(game));

			// players
			JsonArray players = new JsonArray();
			for (Player player : game.getPlayerList()) {
				players.add(serializePlayer(player));
			}
			object.add("players", players);

			// logs
			JsonObject logs = new JsonObject();
			logs.add("lines", serializeLogEntryList(game.getLog()));
			object.add("logs", logs);

			// chat
			JsonObject chat = new JsonObject();
			chat.add("lines", serializeLogEntryList(game.getChat()));
			object.add("chat", chat);

			return object.toString();

		}

		private static JsonElement serializeMap(Game game) {
			JsonObject map = new JsonObject();

			// hexes
			JsonArray hexes = new JsonArray();
			for (Hex hex: game.getTheMap().getHexes().values()) {

				JsonObject jHex = new JsonObject();
				JsonObject location = Serializer.serializeHexLocation(hex.getLocation());

				if (hex.getResource() != null) {
					jHex.addProperty("resource", hex.getResource().toString().toLowerCase());
				}
				jHex.add("location",location);
				jHex.addProperty("number", hex.getNumber());

				hexes.add(jHex);
			}
			map.add("hexes", hexes);

			// ports
			JsonArray ports = new JsonArray();
			for (Port port : game.getTheMap().getPorts().values()) {

				JsonObject jPort = new JsonObject();
				JsonObject location = Serializer.serializeEdgeLocation(port.getLocation());

				jPort.addProperty("ratio", port.getRatio());
				if (port.getResource() != null) {
					jPort.addProperty("resource", port.getResource().toString().toLowerCase());
				}
				jPort.add("direction", location.get("direction"));
				location.remove("direction");
				jPort.add("location", location);

				ports.add(jPort);
			}
			map.add("ports", ports);

			// roads
			JsonArray roads = new JsonArray();
			for (Road road : game.getTheMap().getRoads().values()) {

				JsonObject jRoad = new JsonObject();
				JsonObject location = Serializer.serializeEdgeLocation(road.getLocation());

				jRoad.addProperty("owner", road.getOwnerIndex());
				jRoad.add("location", location);

				roads.add(jRoad);
			}
			map.add("roads", roads);

			// cities
			JsonArray cities = new JsonArray();
			for (City city : game.getTheMap().getCities().values()) {

				JsonObject jCity = new JsonObject();
				JsonObject location = Serializer.serializeVertexLocation(city.getLocation());

				jCity.addProperty("owner", city.getOwnerIndex());
				jCity.add("location", location);

				cities.add(jCity);
			}
			map.add("cities", cities);

			// settlements
			JsonArray settlements = new JsonArray();
			for (Settlement settlement : game.getTheMap().getSettlements().values()) {

				JsonObject jSettlement = new JsonObject();
				JsonObject location = Serializer.serializeVertexLocation(settlement.getLocation());

				jSettlement.addProperty("owner", settlement.getOwnerIndex());
				jSettlement.add("location", location);

				settlements.add(jSettlement);
			}
			map.add("settlements", settlements);

			// robber
			JsonObject robber = new JsonObject();
			robber.addProperty("x", game.getTheMap().getRobber().getLocation().getX());
			robber.addProperty("y", game.getTheMap().getRobber().getLocation().getY());

			map.add("robber", robber);

			map.addProperty("radius", game.getTheMap().getRadius());

			return map;
		}

		private static JsonElement serializePlayer(Player player) {

			JsonObject object = new JsonObject();

			// resources
			JsonObject resources = new JsonObject();
			for (ResourceType resource : player.getResources().keySet()) {

				resources.addProperty(resource.toString().toLowerCase(), player.getResources().get(resource));
			}
			object.add("resources", resources);

			// old dev cards
			JsonObject oldDevCards = new JsonObject();
			for (DevCardType devCardType : player.getOldDevCards().keySet()) {

				oldDevCards.addProperty(devCardType.toString().toLowerCase(), player.getOldDevCards().get(devCardType)); // TODO proper names
			}
			object.add("oldDevCards", oldDevCards);

			// new dev cards
			JsonObject newDevCards = new JsonObject();
			for (DevCardType devCardType : player.getNewDevCards().keySet()) {

				newDevCards.addProperty(devCardType.toString().toLowerCase(), player.getNewDevCards().get(devCardType)); // TODO proper names
			}
			object.add("newDevCards", newDevCards);

			object.addProperty("roads", player.getRoads());
			object.addProperty("settlements", player.getSettlements());
			object.addProperty("cities", player.getCities());
			object.addProperty("soldiers", player.getSoldiers());
			object.addProperty("victoryPoints", player.getVictoryPoints());

			object.addProperty("monuments", player.getMonuments());
			object.addProperty("playedDevCard", player.isPlayedDevCard());
			object.addProperty("discarded", player.isDiscarded());

			object.addProperty("playerID", player.getPlayerID());
			object.addProperty("playerIndex", player.getPlayerIndex());
			object.addProperty("name", player.getName());
			object.addProperty("color", player.getColor().toString().toLowerCase());

			return object;
		}

		private static JsonArray serializeLogEntryList(List<LogEntry> logEntries) {

			JsonArray array = new JsonArray();
			for (LogEntry entry : logEntries) {
				JsonObject object = new JsonObject();
				object.addProperty("source", entry.getUsername());
				object.addProperty("message", entry.getMessage());
				array.add(object);
			}
			return array;
		}
	/*


		Player:
		- resources as object of lowercase names
		- oldDevCards as object, camelCase names
		- newDevCards as object
		- roads as num remaining
		- settlements as num "
		- cities as "
		- soldiers as "
		- victoryPoints as "
		- monuments as "
		- playedDevCard as  bool
		- discarded as bool
		- playerID as num
		- playerIndex as num
		- name
		- color


	 */
}
