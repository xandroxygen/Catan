package shared.model;

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

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public void setLongestRoad(int longestRoad) {
		this.longestRoad = longestRoad;
	}

	public void setLargestArmy(int largestArmy) {
		this.largestArmy = largestArmy;
	}
}