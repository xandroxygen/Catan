package client.roll;

import client.base.*;
import client.model.Game;
import client.model.GameStatus;
import client.model.InvalidActionException;
import client.model.Model;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController, Observer {

	private IRollResultView resultView;

	private static final int DICE_MIN = 1;
	private static final int DICE_MAX_INCLUSIVE = 7;
	private static final long ROLL_TIMER_INTERVAL = 3000; // 3s in milliseconds

	private Timer rollTimer;
	private boolean hasRolled;

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);

		rollTimer = new Timer();
		hasRolled = false;
		
		setResultView(resultView);
		Model.getInstance().addObserver(this);
	}
	
	public IRollResultView getResultView() {
		return resultView;
	}
	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}

	/**
	 * Called either by the Timer reaching 0 or by the user clicking Roll.
	 * Calculates dice roll and sends it off to the server.
	 */
	@Override
	public void rollDice() {

		try {
			hasRolled = true;
			//int roll = rollOneDie() + rollOneDie();
			int roll = 7;
			Model.getInstance().getServer().rollNumber(roll);
			getResultView().setRollValue(roll);
			getRollView().closeModal();
			getResultView().showModal();
		}
		catch (InvalidActionException e) {
			System.out.print(e.message);
			getRollView().setMessage("The roll failed.");
		}
	}

	/**
	 * Starts a 3 second timer before automatically rolling the dice.
	 * If needed, can be refactored to display a countdown to 0.
	 */
	public void startTimer() {
			rollTimer.schedule(new TimerTask() {
				@Override
				public void run() {

					if (!hasRolled) {

						// if needed, implement countdown timer here
						rollDice();
					}
				}
			}, ROLL_TIMER_INTERVAL);
	}

	/**
	 * Returns a random number between 1 and 6, the equivalent of rolling a die.
	 * @return int dice number rolled
	 */
	private int rollOneDie() {
		return ThreadLocalRandom.current().nextInt(DICE_MIN, DICE_MAX_INCLUSIVE);
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(java.util.Observable o, Object arg) {

		Game game = (Game) arg;

		/**
		 * When player's turn and status is rolling,
		 * start the timer and check for when they roll
		 */
		if (game.isMyTurn() && (game.getTurnTracker().getStatus() == GameStatus.Rolling)) {

			getRollView().showModal();

			hasRolled = false;
			startTimer();
		}


	}
}

