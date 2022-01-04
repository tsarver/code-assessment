package com.fixtufproblems.rectangles;

import static org.junit.Assert.*;

import java.util.Collection;
import org.junit.Test;

import com.fixtufproblems.util.TestingHelper;

public class PointTest {

	@Test
	public void testCompareTo_XDiff() {
		Point inpThis = new Point(4,7);
		Point inpThat = new Point(8, 7);
		int actResult = inpThis.compareTo(inpThat);
		assertEquals("CompareTo is wrong: " + inpThis + " " + actResult + " " + inpThat, -4, actResult);
	}

	@Test
	public void testCompareTo_YDiff() {
		Point inpThis = new Point(4, 8);
		Point inpThat = new Point(7, 8);
		int actResult = inpThis.compareTo(inpThat);
		assertEquals("CompareTo is wrong: " + inpThis + " " + actResult + " " + inpThat, -3, actResult);
	}

	/**
	 * Use a series of assertions to ensure that the two collections of Points are the equal.
	 * 
	 * @param message the message prefix to use if an assertion fails
	 * @param actual the actual results
	 * @param expected the expected results
	 */
	protected static void compareOrderedPoints(String message, Collection<Point> expected, Collection<Point> actual) {
		TestingHelper.compareOrderedComparable(message, "Point", expected, actual);
	}
}
