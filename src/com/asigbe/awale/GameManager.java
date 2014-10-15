package com.asigbe.awale;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * This class manages game progress.
 * 
 * @author Delali Zigah
 */
public class GameManager {

    /** current awale managed by this class **/
    public Awale                         awale;
    private final ArtificialIntelligence artificialIntelligence = new AlphaBeta();
    private final Activity               activity;
    private boolean                      isPlayer1_Computer     = false;
    private boolean                      isPlayer2_Computer     = true;

    private class GameOverTask extends AsyncTask<Awale, Object, Object> {

	@Override
	protected Object doInBackground(Awale... awale) {
	    awale[0].doGameOver();
	    return null;
	}

	@Override
	protected void onPostExecute(Object result) {
	    GameManager.this.activity
		    .showDialog(PlayActivity.END_OF_GAME_DIALOG);
	}
    }

    private class PlayTask extends AsyncTask<Object, Object, Short> {

	private static final short ACTION_GAME_OVER     = 0;
	private static final short ACTION_IS_STARVING   = 1;
	private static final short ACTION_WILL_STARVE   = 2;
	private static final short ACTION_COMPUTER_LOST = 3;
	private static final short ACTION_NEXT_TURN     = 4;

	@Override
	protected Short doInBackground(Object... params) {
	    Awale awale = (Awale) params[0];
	    ArtificialIntelligence artificialIntelligence = (ArtificialIntelligence) params[1];
	    short side = (Short) params[2];
	    short position = (Short) params[3];

	    // if this is the computer we let him play, otherwise we verify
	    // that the player
	    // has chosen a correct case
	    if ((awale.currentSide == 0 && GameManager.this.isPlayer1_Computer)
		    || (awale.currentSide == 1 && GameManager.this.isPlayer2_Computer)) {
		// the current player is the computer
		position = artificialIntelligence.searchBestPlay(new Awale(
		        awale), awale.currentSide);
		// the computer has found no proper choice, it
		// should not happen !!!!
		if (position == Short.MIN_VALUE) {
		    return ACTION_COMPUTER_LOST;
		}
	    } else {
		// the current player is a human
		if (Awale.isAdversaryHungry(side, awale.territory)) {
		    // the adversary is hungry so the player must give him
		    // some seeds
		    if (awale.territory[side][position] <= (awale.size - 1)
			    - position) {
			// the player must play an other play if this is
			// possible
			boolean isThereAnotherChoice = false;
			for (int i = awale.size - 1; i >= 0
			        && !isThereAnotherChoice; i--) {
			    if (i != position) {
				isThereAnotherChoice = (awale.territory[side][i] > (awale.size - 1)
				        - i);
			    }
			}

			// there is another choice beside the starvation of
			// the adversary let's prevent the user
			if (isThereAnotherChoice) {
			    return ACTION_IS_STARVING;
			}
		    }
		} else {
		    // we verify that the human player is not doing a
		    // takeover
		    // the player can't starve the adversary
		    if (awale.isATakeOver(side, position)) {
			// if there is no other choice, we play but don't
			// take seeds
			boolean isThereAnotherChoice = false;
			for (short i = 0; i < awale.size
			        && !isThereAnotherChoice; i++) {
			    if (i != position && awale.territory[side][i] > 0) {
				isThereAnotherChoice = !awale.isATakeOver(side,
				        i);
			    }
			}

			// there is another choice beside the starvation of
			// the adversary
			// let's prevent the user
			if (isThereAnotherChoice) {
			    return ACTION_WILL_STARVE;
			}
		    }
		}
	    }

	    awale.distribute(side, position);

	    awale.changeCurrentSide();

	    if (awale.isEmpty()) {
		return ACTION_GAME_OVER;
	    }

	    // the player is hungry on his turn, this is an end of game
	    if (awale.isCurrentPlayerHungry()) {
		return ACTION_GAME_OVER;
	    }

	    return ACTION_NEXT_TURN;
	}

	@Override
	protected void onPostExecute(Short result) {
	    switch (result) {
	    case ACTION_GAME_OVER:
		gameOver();
		return;
	    case ACTION_WILL_STARVE:
		displayWillStarve();
		return;
	    case ACTION_IS_STARVING:
		displayIsStarving();
		return;
	    case ACTION_COMPUTER_LOST:
		displayComputerIsLost();
		return;
	    case ACTION_NEXT_TURN:
		nextTurn();
	    default:
		break;
	    }
	}

    }

    /**
     * Creates a standard game manager for the given awale and notifies users
     * via the given activity.
     */
    public GameManager(Activity activity, Awale awale) {
	this.activity = activity;
	this.awale = awale;
	// for tests purpose
	// this.awale.territory[0] = new short[] { 0, 0, 0, 0, 0, 1 };
	// this.awale.territory[1] = new short[] { 0, 1, 1, 1, 1, 1 };
//	this.awale.points[0] = 48;
//	this.awale.points[1] = 48;
    }

    // Manage the next turn
    private void nextTurn() {

	// if ((this.awale.currentSide == 0 && !this.isPlayer1_Computer)
	// || (this.awale.currentSide == 1 && !this.isPlayer2_Computer)) {
	// this.vibrator.vibrate(100);
	// }
	if ((this.awale.currentSide == 0 && this.isPlayer1_Computer)
	        || (this.awale.currentSide == 1 && this.isPlayer2_Computer)) {
	    // if this is the turn of the computer we launch the play
	    play(this.awale.currentSide, (short) -1);
	}
    }

    /**
     * Executes a turn of the game.
     */
    public void play(short side, short position) {
	new PlayTask().execute(this.awale, this.artificialIntelligence, side,
	        position);
    }

    /**
     * Displays an end of game dialog.
     */
    public void gameOver() {
	new GameOverTask().execute(this.awale);
    }

    /**
     * Displays an dialog which indicates that the opponent is starving.
     */
    private void displayIsStarving() {
	this.activity.showDialog(PlayActivity.IS_STARVING_DIALOG);
    }

    /**
     * Displays an dialog which indicates that the opponent will starve.
     */
    private void displayWillStarve() {
	this.activity.showDialog(PlayActivity.WILL_STARVE_DIALOG);
    }

    /**
     * Displays a dialog which indicates that the computer is lost.
     */
    private void displayComputerIsLost() {
	this.activity.showDialog(PlayActivity.COMPUTER_NO_CHOICE_DIALOG);
    }

}
