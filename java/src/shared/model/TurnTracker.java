package shared.model;

import java.io.Serializable;

public class TurnTracker implements Serializable {
	private GameStatus status;
	private int currentTurn;
	private int longestRoad;
	private int largestArmy;
	
	// Create a TurnTracker for a new game
	public TurnTracker() {
		this.status= GameStatus.FirstRound;
		this.currentTurn = 0;
		this.longestRoad = -1;
		this.largestArmy = -1;
	}
	
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

	public void nextStatus() {
		switch (status) {
		case FirstRound:
			status = GameStatus.SecondRound;
			break;
		case SecondRound:
			status = GameStatus.Rolling;
			break;
		case Rolling:
			status = GameStatus.Rolling;
			break;
		default:
			break;
		}
		
	}

	public void nextTurn() {
		if(status == GameStatus.FirstRound && currentTurn < 3) {
			currentTurn++;
		}
		else if(status == GameStatus.FirstRound && currentTurn == 3) {
			status = GameStatus.SecondRound;
		}
		else if(status == GameStatus.SecondRound && currentTurn > 0) {
			currentTurn--;
		}
		else if(status == GameStatus.SecondRound && currentTurn == 0) {
			status = GameStatus.Rolling;
		}
		else {
			currentTurn++;
			if (currentTurn == 4) {
				currentTurn = 0;
			}
			status = GameStatus.Rolling;
		}
	}

	public void setupProgression() {
		if (status == GameStatus.FirstRound && currentTurn < 3) {
			currentTurn++;
		}
		else if (status == GameStatus.SecondRound && currentTurn > 0) {
			currentTurn--;
		}
		
	}
}