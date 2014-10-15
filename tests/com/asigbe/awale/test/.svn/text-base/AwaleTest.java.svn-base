package com.asigbe.awale.test;

import java.util.Arrays;

import com.asigbe.awale.AlphaBeta;
import com.asigbe.awale.Awale;

import android.test.IsolatedContext;

import junit.framework.TestCase;

public class AwaleTest extends TestCase {

	public void testNotHungry() {
		Awale awale = new Awale((short) 6);
		assertFalse(awale.isAdversaryHungry((short) 0, awale.territory));
		assertFalse(awale.isAdversaryHungry((short) 1, awale.territory));
	}

	public void testIsEmpty() {
		Awale awale = new Awale((short) 6);
		Arrays.fill(awale.territory[0], (short) 0);
		Arrays.fill(awale.territory[1], (short) 0);
		
		assertTrue(awale.isEmpty());

		awale.territory[0] = new short[] { 1, 0, 0, 0, 0, 0 };
		Arrays.fill(awale.territory[1], (short) 0);
		assertFalse(awale.isEmpty());
		
		Arrays.fill(awale.territory[0], (short) 0);
		awale.territory[1] = new short[] { 1, 0, 0, 0, 0, 0 };
		assertFalse(awale.isEmpty());
	}

	public void testIsHungry() {
		Awale awale = new Awale((short) 6);
		Arrays.fill(awale.territory[1], (short) 0);
		assertTrue(awale.isAdversaryHungry((short) 0, awale.territory));
		assertFalse(awale.isAdversaryHungry((short) 1, awale.territory));

		awale = new Awale((short) 6);
		Arrays.fill(awale.territory[0], (short) 0);
		assertFalse(awale.isAdversaryHungry((short) 0, awale.territory));
		assertTrue(awale.isAdversaryHungry((short) 1, awale.territory));

		awale = new Awale((short) 6);
		Arrays.fill(awale.territory[0], (short) 0);
		Arrays.fill(awale.territory[1], (short) 0);
		assertTrue(awale.isAdversaryHungry((short) 0, awale.territory));
		assertTrue(awale.isAdversaryHungry((short) 1, awale.territory));
	}

	public void testIsATakeOver() {
		Awale awale = new Awale((short) 6);
		assertFalse(awale.isATakeOver((short) 0, (short) 0));
		assertFalse(awale.isATakeOver((short) 1, (short) 0));

		awale.territory[0] = new short[] { 2, 2, 0, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 0, 0, 0, 3, 0 };

		assertTrue(awale.isATakeOver((short) 1, (short) 4));

		awale.territory[0] = new short[] { 2, 2, 1, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 0, 0, 0, 3, 0 };

		assertFalse(awale.isATakeOver((short) 1, (short) 4));

		awale.territory[1] = new short[] { 2, 2, 0, 0, 0, 0 };
		awale.territory[0] = new short[] { 0, 0, 0, 0, 3, 0 };

		assertTrue(awale.isATakeOver((short) 0, (short) 4));

		awale.territory[1] = new short[] { 2, 2, 1, 0, 0, 0 };
		awale.territory[0] = new short[] { 0, 0, 0, 0, 3, 0 };

		assertFalse(awale.isATakeOver((short) 0, (short) 4));

	}

	public void testIA() {
		Awale awale = new Awale((short) 6);
		AlphaBeta alphaBeta = new AlphaBeta();

		short searchBestPlay;

		// test if it choose the solution which take more seeds
		awale.territory[0] = new short[] { 0, 0, 0, 3, 2, 2 };
		awale.territory[1] = new short[] { 0, 0, 0, 0, 7, 1 };

		searchBestPlay = alphaBeta.searchBestPlay(awale, (short) 1);
		assertEquals(4, searchBestPlay);

		// test with the opponent hungry
		awale.territory[0] = new short[] { 0, 0, 0, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 1, 0, 0, 7, 0 };

		searchBestPlay = alphaBeta.searchBestPlay(awale, (short) 1);
		assertEquals(4, searchBestPlay);

		// test with the opponent hungry
		awale.territory[0] = new short[] { 0, 0, 0, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 7, 0, 0, 1, 0 };

		searchBestPlay = alphaBeta.searchBestPlay(awale, (short) 1);
		assertEquals(1, searchBestPlay);

		// test when the opponent will starve
		awale.territory[0] = new short[] { 1, 1, 0, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 6, 0, 0, 4, 0 };

		searchBestPlay = alphaBeta.searchBestPlay(awale, (short) 1);
		assertEquals(4, searchBestPlay);

		// test when the opponent will starve
		awale.territory[0] = new short[] { 1, 1, 0, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 6, 0, 0, 3, 0 };

		searchBestPlay = alphaBeta.searchBestPlay(awale, (short) 1);
		assertEquals(1, searchBestPlay);

		// test when the opponent is hungry and can't be replenish
		awale.territory[0] = new short[] { 0, 0, 0, 0, 0, 0 };
		awale.territory[1] = new short[] { 0, 2, 0, 1, 1, 0 };

		searchBestPlay = alphaBeta.searchBestPlay(awale, (short) 1);
		assertTrue(searchBestPlay != Short.MIN_VALUE);
	}
}
