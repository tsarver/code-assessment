package com.fixtufproblems.rectangles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
		Rectangle actResult = createGoodRectangle(inpSide, inpBottom, inpFarSide, inpTop);
		assertNotNull(actResult);
		Point actPoint = actResult.getLowerLeft();
		assertNotNull("The lower left point shouldn't be null.", actPoint);
		final Point expLowerLeft = new Point(inpSide, inpBottom);
		assertEquals("The lower left point is incorrect.", expLowerLeft, actPoint );
		actPoint = actResult.getLowerRight();
		assertNotNull("The lower right point shouldn't be null.", actPoint);
		final Point expLowerRight = new Point(inpFarSide, inpBottom);
		assertEquals("The lower right point is incorrect.", expLowerRight, actPoint );
		actPoint = actResult.getUpperRight();
		assertNotNull("The upper right point shouldn't be null.", actPoint);
		final Point expUpperRight = new Point(inpFarSide, inpTop);
		assertEquals("The upper right point is incorrect.", expUpperRight, actPoint );
		actPoint = actResult.getUpperLeft();
		assertNotNull("The upper left point shouldn't be null.", actPoint);
		final Point expUpperLeft = new Point(inpSide, inpTop);
		assertEquals("The upper left point is incorrect.", expUpperLeft, actPoint );
		
	}
	
	/*
	 * These are cases where a single vertex of rectangle A is contained inside other rectangle B.
	 * Rectangle B receives the "intersection" message.
	 * They are named for the direction of the opposite vertex of the rectangle A.
	 * We don't need 4 tests because we can call "intersection" on each pair, and the results should be identical.
	 * In other words, "intersection()" is reflexive. [f(x,y) == f(y,x)].
	 * We do end up calling the method under test 4 times, but we only need to setup the input and expected results twice.
	 */
	@Test
	public void testIntersect_UpperLeft() {
		final Rectangle inpUpperLeft = createGoodRectangle(6, 6, 12, 12);
		final Rectangle inpLowerRight = createGoodRectangle(8, 4, 14, 8);
		List<Intersection> actResult = inpLowerRight.intersection(inpUpperLeft);
		assertNotNull("UpperLeft does intersect, but indicated that it does not.", actResult);
		//normalize the results
		List<Point> actSortedResult  = sortPointsIgnoreSegments(actResult);
		final SortedSet<Point> expResult = sortPoints(new Point[] { new Point(8,6), new Point(12,8) });
		compareOrderedPoints("UpperLeft Intersect", expResult, actSortedResult);
		actResult = inpUpperLeft.intersection(inpLowerRight);
		assertNotNull("LowerRight does intersect, but indicated that it does not.", actResult);
		//normalize the results
		actSortedResult  = sortPointsIgnoreSegments(actResult);
		compareOrderedPoints("LowerRight Intersect", expResult, actSortedResult);
	}
	
	@Test
	public void testIntersect_UpperRight() {
		final Rectangle inpUpperRight = createGoodRectangle(6, 6, 12, 12);
		final Rectangle inpLowerLeft = createGoodRectangle(4, 4, 8, 8);
		List<Intersection> actResult = inpLowerLeft.intersection(inpUpperRight);
		assertNotNull("UpperRight does intersect, but indicated that it does not.", actResult);
		//normalize the results
		List<Point> actSortedResult  = sortPointsIgnoreSegments(actResult);
		final SortedSet<Point> expResult = sortPoints(new Point[] { new Point(8, 6), new Point(6, 8) });
		compareOrderedPoints("UpperRight Intersect", expResult, actSortedResult);
		actResult = inpUpperRight.intersection(inpLowerLeft);
		assertNotNull("LowerLeft does intersect, but indicated that it does not.", actResult);
		//normalize the results
		actSortedResult  = sortPointsIgnoreSegments(actResult);
		compareOrderedPoints("LowerLeft Intersect", expResult, actSortedResult);
	}

	@Test
	public void testIntersect_OutsideTheBox() {
		//This is a test where one Rectangle is compared to another one,
		//where every vertex is outside the other.
		final Rectangle inpOutsideX = createGoodRectangle(0, 4, 10, 10);
		final Rectangle inpOutsideY = createGoodRectangle(2, 2, 8, 14);
		final List<Intersection> actResult = inpOutsideX.intersection(inpOutsideY);
		assertNotNull("OutsideTheBox does intersect, but indicated that it does not.", actResult);
		//normalize the results
		List<Point> actSortedResult  = sortPointsIgnoreSegments(actResult);
		final SortedSet<Point> expResult = sortPoints(
				new Point[] { new Point(2, 4), new Point(8, 4), new Point(2, 10), new Point(8, 10) });
		compareOrderedPoints("OutsideTheBox Intersect", expResult, actSortedResult);
	}

	/*
	 * One Overlapping segment, plus an intersecting point.
	 */
	@Test
	public void testIntersect_Overlap() {
		final Rectangle inpTheBox = createGoodRectangle(2, 2, 12, 10);
		final Rectangle inpOverlapBox = createGoodRectangle(7, 7, 14, 12);
		final List<Intersection> actResult = inpTheBox.intersection(inpOverlapBox);
		assertNotNull("Overlap does intersect, but indicated that it does not.", actResult);
		//normalize the results
		comparePointsAndSegments("Overlap Intersect", new Point[] { new Point(10, 7) },
				new SimpleSegment[] {new SimpleSegment(new Point(7, 12), new Point(10, 12))}, actResult);
	}

	/*
	 * Negative testing 
	 ************************/

	@Test
	public void testConstructor_ZeroArea() {
		Rectangle actRectangle = null;
		try {
			actRectangle = new Rectangle(new Point(10, 10), new Point(10, 10));
			fail("Should have thrown an exception if attemtping to create a Rectangle with zero area.");
		} catch (IllegalArgumentException iae) {
			//success
			assertNull("The constructor should have thrown an exception.", actRectangle);
		}
	}
	
	@SuppressWarnings("unused")
	//Ignore
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
	protected static Rectangle createGoodRectangle(final int inpSide, final int inpBottom, final int inpFarSide
			, final int inpTop) throws IllegalArgumentException {
//		Rectangle expResult = new Rectangle(new Point(inpSide, inpBottom), new Point(inpFarSide, inpBottom), 
//				new Point(inpFarSide, inpTop), new Point(inpSide, inpTop));
		Rectangle expResult = new Rectangle(new Point(inpSide, inpBottom), new Point(inpFarSide, inpTop));
		return expResult;
	}

	protected static SortedSet<Point> sortPoints(Point[] points) {
		//Note that this doesn't check for null elements of the array, which would probably cause problems.
		return new TreeSet<Point>(Arrays.asList(points));
	}

	protected static SortedSet<SimpleSegment> sortSegments(SimpleSegment[] segments) {
		return new TreeSet<SimpleSegment>(Arrays.asList(segments));
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

	protected static void comparePointsAndSegments(String message, Point[] expPoints, SimpleSegment expSegments[],
			List<Intersection> actResult) {
		//sort the expected points
		SortedSet<Point> expSortedPoints = sortPoints(expPoints);
		SortedSet<SimpleSegment> expSortedSegments = sortSegments(expSegments);
		List<Point> actSortedPoints = sortPointsIgnoreSegments(actResult);
		List<SimpleSegment> actSortedSegments = sortSegmentsIgnorePoints(actResult);
		//Iterate expSortedPoints and actSortedPoints
		
		//Iterate expSortedSegments and actSortedSegments
		
	}

	/**
	 * 
	 * @param intersections <tt>List&lt;Point&gt;</tt>, probably the result of <tt>Rectangle.intersections()</tt>
	 * @return all of the single <tt>Point</tt>'s in the <tt>List</tt>, ignore any SimpleSegments
	 */
	public static List<Point> sortPointsIgnoreSegments(List<Intersection> intersections) {
		SortedSet<Point> sortedResult = new TreeSet<Point>();
		for (Intersection thisOne : intersections) {
			if (!thisOne.isLineSegment()) {
				sortedResult.addAll(thisOne.getPoints());
			}
		}
		List<Point> result = new ArrayList<Point>(sortedResult.size());
		result.addAll(sortedResult);
		return result;
	}
	
	protected static List<SimpleSegment> sortSegmentsIgnorePoints(List<Intersection> intersections) {
		SortedSet<SimpleSegment> sortedResult = new TreeSet<SimpleSegment>();
		for (Intersection thisOne : intersections) {
			if (thisOne.isLineSegment()) {
				sortedResult.add((SimpleSegment)thisOne);
			}
		}
		List<SimpleSegment> result = new ArrayList<SimpleSegment>(sortedResult.size());
		result.addAll(sortedResult);
		return result;
	}
}
