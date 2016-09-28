package client.model;

public class TurnTracker {
	private GameStatus status;
	private int currentTurn;
	private int longestRoad;
	private int largestArmy;
	
	public GameStatus getStatus() {
		return status;
	}
	public int getCurrentTurn() {
		return currentTurn;
	}
	public int getLongestRoad() {
		return longestRoad;
	}
	public int getLargestArmy() {
		return largestArmy;
	}
	
}