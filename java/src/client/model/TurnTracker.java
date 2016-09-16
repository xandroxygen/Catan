package client.model;

public class TurnTracker {
	private GameStatus status;
	private int currentTurn;
	private DevCard longestRoad;
	private DevCard largestArmy;
	
	public GameStatus getStatus() {
		return status;
	}
	public int getCurrentTurn() {
		return currentTurn;
	}
	public DevCard getLongestRoad() {
		return longestRoad;
	}
	public DevCard getLargestArmy() {
		return largestArmy;
	}
	
}