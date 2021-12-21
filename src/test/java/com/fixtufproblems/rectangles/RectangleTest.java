package com.fixtufproblems.rectangles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class RectangleTest {

	@Test
	public void testValidConstructor() {
		
		final int inpBottom = 8;
		final int inpSide = 4; //left side of the rectangle
		final int inpTop = 18;
		final int inpFarSide = 10;
		Rectangle actResult = createGoodRectangle(inpBottom, inpSide, inpTop, inpFarSide);
		assertNotNull(actResult);
		//TODO check some more stuff
	}
	
	@Test
	public void testIntersect_UpperLeft() {
		// The first rectangle is up and to the left of the second rectangle
		final Rectangle inpUpperLeft = createGoodRectangle(10, 4, 12, 8);
		final Rectangle inpLowerRight = createGoodRectangle(9, 6, 11, 6);
		final Point actResult[] = inpUpperLeft.intersects(inpLowerRight);
		assertNotNull("It does intersect, but returned that it is not.", actResult);
		//normalize the results
		final SortedSet<Point> actSortedResult  = sortPoints(actResult);
		final SortedSet<Point> expResult = sortPoints(new Point[] { new Point(6,10), new Point(8,11) });
		compareOrderedPoints("UpperLeft Intersect", expResult, actSortedResult);
	}

	/*
	 * Negative testing 
	 ************************/
	
	@SuppressWarnings("unused")
	@Test
	public void testOutOfOrder1() {
	
		Rectangle actResult = null;
		final int inpBottom = 8;
		final int inpSide = 4; //left side of the rectangle
		final int inpTop = 18;
		final int inpFarSide = 10;
		try {
			actResult = new Rectangle(new Point(inpSide, inpBottom), new Point(inpFarSide, inpTop) 
					, new Point(inpFarSide, inpBottom), new Point(inpSide, inpTop));
			fail("Should have thrown an exception because the vertices form an X.");
		} catch (IllegalArgumentException iae) {
			//success
		}
	}

	/*
	 * Helper functions
	 *************************/

	/**
	 * Create a Rectangle based on edges, not vertices
	 * 
	 * @param inpBottom the Y value of the bottom of the rectangle
	 * @param inpSide the X value of the left side
	 * @param inpTop the Y value of the top of the rectangle
	 * @param inpFarSide the X value of the right side
	 * @return a Rectangle
	 * @throws IllegalArgumentException
	 */
	protected static Rectangle createGoodRectangle(final int inpBottom, final int inpSide, final int inpTop,
			final int inpFarSide) throws IllegalArgumentException {
		Rectangle expResult = new Rectangle(new Point(inpSide, inpBottom), new Point(inpFarSide, inpBottom), 
				new Point(inpFarSide, inpTop), new Point(inpSide, inpTop));
		return expResult;
	}

	protected static SortedSet<Point> sortPoints(Point[] actResult) {
		//Note that this doesn't check for null elements of the array, which would probably cause problems.
		return new TreeSet<Point>(Arrays.asList(actResult));
	}
	
	/**
	 * Use a series of assertions to ensure that the two collections of Points are the equal.
	 * 
	 * @param message the message prefix to use if an assertion fails
	 * @param actual the actual results
	 * @param expected the expected results
	 */
	protected static void compareOrderedPoints(String message, Collection<Point> expected, Collection<Point> actual) {
		if (null == expected) {
			assertNull(message + ": expected null.", actual);
		}
		assertEquals(message + ": returned the wrong number of results.", expected.size(), actual.size());
		Iterator<Point> expIterator = expected.iterator();
		Iterator<Point> actIterator = actual.iterator();
		int idx = 0;
		//Both iterators have to be the same size because we already checked it.
		while (expIterator.hasNext()) {
			assertEquals(message + ": The Point at position " + idx + " doesn't match expected.", 
					expIterator.next(), actIterator.next());
			idx++;
		}
	}
}
