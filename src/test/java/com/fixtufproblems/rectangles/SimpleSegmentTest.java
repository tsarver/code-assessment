package com.fixtufproblems.rectangles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import com.fixtufproblems.util.TestingHelper;

public class SimpleSegmentTest {

	@Test
	public void testConstructor_succeedHorizontal() {
		SimpleSegment actResult = new SimpleSegment(new Point(2,8), new Point(4, 8));
		//Assume that a constructor can't return null
		assertEquals("The segment should be Horizontal", true, actResult.isHorizontal());
	}

	@Test
	public void testIntersects_Middle() {
		final Point inpLeftPoint = new Point(4, 7);
		final Point inpRightPoint = new Point(10, 7);
		final Point inpBottomPoint = new Point(6, 2);
		final Point inpTopPoint = new Point(6,12);
		final SimpleSegment inpHorizSegment = new SimpleSegment(inpLeftPoint, inpRightPoint);
		final SimpleSegment inpVerticalSegment = new SimpleSegment(inpBottomPoint, inpTopPoint);
		final Point actResult = inpHorizSegment.intersects(inpVerticalSegment);
		assertNotNull("The intersect shouldn't be null.", actResult);
		assertEquals("The intersection point is incorrect.", new Point(6,7), actResult);
	}

	@Test
	public void testIntersects_VerticalT() {
		final Point inpLeftPoint = new Point(2, 4);
		final Point inpRightPoint = new Point(5, 4);
		final Point inpBottomPoint = new Point(3, 4);
		final Point inpTopPoint = new Point(3,12);
		final SimpleSegment inpHorizSegment = new SimpleSegment(inpLeftPoint, inpRightPoint);
		final SimpleSegment inpVerticalSegment = new SimpleSegment(inpBottomPoint, inpTopPoint);
		final Point actResult = inpHorizSegment.intersects(inpVerticalSegment);
		assertNotNull("The intersect shouldn't be null.", actResult);
		assertEquals("The intersection point is incorrect.", new Point(3,4), actResult);
	}
	
	@Test
	public void testHorizIntersects_OutsideTheBox() {
		//See RectangleTest testIntersect_OutsideTheBox()
		final SimpleSegment inpRect1Top = new SimpleSegment(new Point(0,10), new Point(10,10));
		final SimpleSegment inpRect1Bottom = new SimpleSegment(new Point(0,4), new Point(10, 4));
		final SimpleSegment inpRect2Left = new SimpleSegment(new Point(2,2), new Point(2,14));
		final SimpleSegment inpRect2Right = new SimpleSegment(new Point(8,2), new Point(8,14));
		
		check_intersectHorizVertical("Top X Left", inpRect1Top, inpRect2Left, new Point(2,10));
		check_intersectHorizVertical("Top X Right", inpRect1Top, inpRect2Right, new Point(8,10));
		check_intersectHorizVertical("Bottom X Left", inpRect1Bottom, inpRect2Left, new Point(2,4));
		check_intersectHorizVertical("Bottom X Right", inpRect1Bottom, inpRect2Right, new Point(8,4));
	}

	protected void check_intersectHorizVertical(final String message, final SimpleSegment inpSegment1, final SimpleSegment inpSegment2, final Point expResult) {
		Point actResult = SimpleSegment.intersectHorizVertical(inpSegment1, inpSegment2);
		assertEquals(buildMessageIntersectSegments(message, inpSegment1, inpSegment2), expResult, actResult);
	}

	protected static String buildMessageIntersectSegments(final String message, final SimpleSegment inpSegment1, final SimpleSegment inpSegment2) {
		StringBuffer result = new StringBuffer();
		result.append(message);
		result.append(": The returned intersection of segment (");
		result.append(inpSegment1.getLowerPoint());
		result.append(", ");
		result.append(inpSegment1.getHigherPoint());
		result.append(") and segment (");
		result.append(inpSegment2.getLowerPoint());
		result.append(", ");
		result.append(inpSegment2.getHigherPoint());
		result.append(") is not correct. ");
		return result.toString();
	}
	
	/*
	 * Negative testing
	 ************/

	@Test
	public void testConstructor_notOnAnAxis() {
		try {
			new SimpleSegment(new Point(2,4), new Point(3, 5));
			fail("Constructor should fail: The Points to do not lie on a vertical or horizontal line.");
		} catch (IllegalArgumentException iae) {
			//succeed
		}
	}

	/**
	 * Execute a series of assertions to ensure that two collections of <tt>SimpleSegment</tt>'s are equal.
	 * 
	 * @param message the message prefix to use if an assertion fails
	 * @param expected the expected segments, sorted
	 * @param actual the actual segments, sorted
	 */
	protected static void compareOrderedSegments(String message, Collection<SimpleSegment> expected,
			Collection<SimpleSegment> actual) {
		//TODO add a closure to compareOrderedComparable so that the caller can add arbitrary code to the iteration
		TestingHelper.compareOrderedComparable(message, "SimpleSegment", expected, actual);
		//make sure that getPoints() works
		Iterator<SimpleSegment> expIterator = expected.iterator();
		Iterator<SimpleSegment> actIterator = actual.iterator();
		int idx = 0;
		while (expIterator.hasNext()) {
			//Assume that getPoints() returns sorted points
			PointTest.compareOrderedPoints(message + "getPoints() for segment " + idx + " is wrong.",
					expIterator.next().getPoints(), actIterator.next().getPoints());
			idx++;
		}
	}
}
