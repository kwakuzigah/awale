package com.asigbe.awale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Debug;

/**
 * This class represents a game.
 * 
 * @author Delali Zigah
 */
public class Awale {

    /** Event when points change **/
    public static final short  POINTS        = 0;
    /** Event when the territory change **/
    public static final short  TERRITORY     = 1;
    /** Event when the current side **/
    public static final short  CURRENT_SIDE  = 2;

    private static final short INITIAL_SEEDS = 4;

    private static final short NO_SIDE       = -1;
    private static final short NO_POSITION   = -1;

    /**
     * This interface defined which functions must be implemented by listeners
     * of the awale class.
     * 
     * @author Delali Zigah
     */
    public static interface AwaleListener {
	/**
	 * Called when the state of the awale changed.
	 */
	public void awaleChanged(Awale awale, short eventType, short row,
	        short column);
    }

    /** Size of the territory **/
    public short                      size;
    /** The simulation territory used for different functions **/
    private final short[][]           simulateTerritory;
    /** The current territory **/
    public final short[][]            territory;
    /** The current points **/
    public short[]                    points;
    /** The side currently playing **/
    public short                      currentSide;
    /** The current points **/
    private final List<AwaleListener> listeners;

    /**
     * Constructs a game with the size of holes determined by <code>size</code>.
     */
    public Awale(short size) {
	this.listeners = new ArrayList<AwaleListener>();
	this.currentSide = 0;
	this.size = size;
	this.territory = new short[2][size];
	this.simulateTerritory = new short[2][size];
	Arrays.fill(this.territory[0], INITIAL_SEEDS);
	Arrays.fill(this.territory[1], INITIAL_SEEDS);
	this.points = new short[2];
    }

    /**
     * Creates a copy of the given Awale except the listeners.
     */
    public Awale(Awale awale) {
	this.listeners = new ArrayList<AwaleListener>();
	this.currentSide = awale.currentSide;
	this.size = awale.size;
	this.territory = new short[2][size];
	System.arraycopy(awale.territory[0], 0, this.territory[0], 0,
	        awale.territory[0].length);
	System.arraycopy(awale.territory[1], 0, this.territory[1], 0,
	        awale.territory[1].length);
	this.simulateTerritory = new short[2][size];
	System
	        .arraycopy(awale.simulateTerritory[0], 0,
	                this.simulateTerritory[0], 0,
	                awale.simulateTerritory[0].length);
	System
	        .arraycopy(awale.simulateTerritory[1], 0,
	                this.simulateTerritory[1], 0,
	                awale.simulateTerritory[1].length);
	this.points = new short[2];
	System.arraycopy(awale.points, 0, this.points, 0, this.points.length);

    }

    /**
     * Adds a new listener.
     * 
     * @param awaleListener
     */
    public void addListener(AwaleListener awaleListener) {
	this.listeners.add(awaleListener);
    }

    /**
     * Play at the given position on the given side of the game.
     */
    public void play(short side, short position, short[][] territory,
	    boolean simulation) {
	short initial_side = side;
	short initial_position = position;
	// redistribution of seeds
	int left_seeds = territory[side][position];
	territory[side][position] = 0;
	if (!simulation) {
	    fireChanged(TERRITORY, side, position);
	}
	short index = position;
	do {
	    int length = territory[side].length;
	    for (; left_seeds > 0 && index + 1 < length; left_seeds--) {
		index++;
		if (side != initial_side || index != initial_position) {
		    territory[side][index] = (short) (territory[side][index] + 1);
		    if (!simulation) {
			fireChanged(TERRITORY, side, index);
		    }
		}
	    }
	    // reinitialize the position of the distribution and change side
	    if (left_seeds > 0) {
		side = getAdversarySide(side);
		index = -1;
	    }
	} while (left_seeds > 0);

	// the player finishes his turn
	if (initial_side != side) {
	    takeSeeds(getAdversarySide(initial_side), index, territory,
		    simulation);
	}
    }

    private void takeSeeds(short side, short index, short[][] territory,
	    boolean simulation) {
	short seedsTaken = 0;
	while (index >= 0
	        && (territory[side][index] == 2 || territory[side][index] == 3)) {
	    seedsTaken += territory[side][index];
	    territory[side][index] = 0;
	    if (!simulation) {
		fireChanged(TERRITORY, side, index);
	    }
	    index--;
	}
	if (!simulation) {
	    this.points[getAdversarySide(side)] += seedsTaken;
	    fireChanged(POINTS, NO_SIDE, NO_POSITION);
	}
    }

    /**
     * Indicates if the game is over.
     */
    public boolean isGameOver() {
	return false;
    }

    /**
     * Indicates if the move takes all seeds from the adversary.
     */
    public boolean isATakeOver(short side, short position) {

	// redistribution of seeds
	// System.arraycopy(this.territory[0], 0, this.simulateTerritory[0], 0,
	// this.territory[0].length);
	// System.arraycopy(this.territory[1], 0, this.simulateTerritory[1], 0,
	// this.territory[1].length);
	for (int i = 0; i < this.size; i++) {
	    this.simulateTerritory[0][i] = this.territory[0][i];
	    this.simulateTerritory[1][i] = this.territory[1][i];
	}

	play(side, position, this.simulateTerritory, true);
	return isAdversaryHungry(side, this.simulateTerritory);
    }

    /**
     * Gets the adversary side.
     * 
     * @return 0 if <code>side</code> equals 1, otherwise 1
     */
    public static short getAdversarySide(short side) {
	return (short) Math.abs(side - 1);
    }

    /**
     * Indicates if the adversary has no seeds at all.
     */
    public static boolean isAdversaryHungry(short side, short[][] territory) {
	side = getAdversarySide(side);
	boolean isAdversaryHungry = true;
	int length = territory[side].length;
	for (short i = 0; i < length && isAdversaryHungry; i++) {
	    isAdversaryHungry = (territory[side][i] == 0);
	}

	return isAdversaryHungry;
    }

    /**
     * Indicates if the current player has no seeds at all.
     */
    public boolean isCurrentPlayerHungry() {
	boolean isHungry = true;
	int length = this.territory[this.currentSide].length;
	for (short i = 0; i < length && isHungry; i++) {
	    isHungry = (this.territory[this.currentSide][i] == 0);
	}

	return isHungry;
    }

    /**
     * Distributes the seeds on the territory for this side and this position.
     */
    public void distribute(short side, short position) {
	play(side, position, this.territory, false);
    }

    /**
     * Indicates if the player can play on this case.
     */
    public boolean canPlayHere(short side, short position) {
	return this.currentSide == side && this.territory[side][position] != 0;
    }

    private void fireChanged(short eventType, short row, short column) {
	int length = this.listeners.size();
	for (int i = 0; i < length; i++) {
	    this.listeners.get(i).awaleChanged(new Awale(this), eventType, row,
		    column);
	}
    }

    /**
     * Indicates if the awale is empty.
     */
    public boolean isEmpty() {
	for (int i = 0; i < this.size; i++) {
	    if (this.territory[0][i] != 0 || this.territory[1][i] != 0) {
		return false;
	    }
	}

	return true;
    }

    /**
     * Computes points for the game over.
     */
    public void doGameOver() {
	// all players takes seeds left on their side of the game
	for (short i = 0; i < this.size; i++) {
	    if (this.territory[0][i] != 0) {
		this.points[0] += this.territory[0][i];
		this.territory[0][i] = 0;
		fireChanged(TERRITORY, (short) 0, i);
	    }

	    if (this.territory[1][i] != 0) {
		this.points[1] += this.territory[1][i];
		this.territory[1][i] = 0;
		fireChanged(TERRITORY, (short) 1, i);
	    }
	}
	fireChanged(POINTS, NO_SIDE, NO_POSITION);
    }

    /**
     * Changed the current side of the game.
     */
    public void changeCurrentSide() {
	this.currentSide = getAdversarySide(this.currentSide);

	fireChanged(CURRENT_SIDE, NO_SIDE, NO_POSITION);
    }
}
