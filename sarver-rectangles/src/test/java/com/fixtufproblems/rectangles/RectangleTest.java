package com.fixtufproblems.rectangles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
		final Point expLowerLeft = new Point(inpSide, inpBottom);
		assertEquals("The lower left point is incorrect.", expLowerLeft, actPoint );
		actPoint = actResult.getLowerRight();
		final Point expLowerRight = new Point(inpFarSide, inpBottom);
		assertEquals("The lower right point is incorrect.", expLowerRight, actPoint );
		actPoint = actResult.getUpperRight();
		final Point expUpperRight = new Point(inpFarSide, inpTop);
		assertEquals("The upper right point is incorrect.", expUpperRight, actPoint );
		actPoint = actResult.getUpperLeft();
		final Point expUpperLeft = new Point(inpSide, inpTop);
		assertEquals("The upper left point is incorrect.", expUpperLeft, actPoint );
		
	}
		
	@Test
	public void testConstructor_Thorough() {
		//Wrote this test because while debugging testIntersect_Overlap,
		//found a rectangle with only one point
		final int inpLeftSideX = 7;
		final int inpBottomY = 7;
		final int inpRightSideX = 14;
		final int inpTopY = 12;
		final Rectangle inpShouldBeGood = createGoodRectangle(inpLeftSideX, inpBottomY, inpRightSideX, inpTopY);
		assertNotNull("Shouldn't be null.", inpShouldBeGood);
		final Point actLowerLeft = inpShouldBeGood.getLowerLeft();
		assertEquals("The lower left point is wrong.", new Point(inpLeftSideX, inpBottomY), actLowerLeft);
		final Point actLowerRight = inpShouldBeGood.getLowerRight();
		assertEquals("The lower left point is wrong.", new Point(inpRightSideX, inpBottomY), actLowerRight);
		final Point actUpperRight = inpShouldBeGood.getUpperRight();
		assertEquals("The upper right point is wrong.", new Point(inpRightSideX, inpTopY), actUpperRight);
		final Point actUpperLeft = inpShouldBeGood.getUpperLeft();
		assertEquals("The upper left point is wrong.", new Point(inpLeftSideX, inpTopY), actUpperLeft);
		final List<SimpleSegment> actSegments = inpShouldBeGood.getSegments();
		assertNotNull("The segment list shouldn't be null.", actSegments);
		assertEquals("There should be 4 segments in a Rectangle.", 4, actSegments.size());
		final SortedSet<SimpleSegment> expSortedSegments = sortSegments(new SimpleSegment[] {
				new SimpleSegment(actLowerLeft, actLowerRight),
				new SimpleSegment(actLowerRight, actUpperRight),
				new SimpleSegment(actUpperRight, actUpperLeft),
				new SimpleSegment(actUpperLeft, actLowerLeft)
				});
		final SortedSet<SimpleSegment> actSortedSegments = sortSegments((SimpleSegment[]) actSegments.toArray(new SimpleSegment[4]));
		SimpleSegmentTest.compareOrderedSegments("Constructor Thorough", expSortedSegments, actSortedSegments);
	}

	@Test
	public void testContains_Success() {
		final Rectangle inpOuter = createGoodRectangle(-10, -3, 5, 10);
		final Rectangle inpInner = createGoodRectangle(-4, -1, 3, 7);
		assertTrue("The outer Rectangle should contain the inner Rectangle.", inpOuter.contains(inpInner));
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
		PointTest.compareOrderedPoints("UpperLeft Intersect", expResult, actSortedResult);
		actResult = inpUpperLeft.intersection(inpLowerRight);
		assertNotNull("LowerRight does intersect, but indicated that it does not.", actResult);
		//normalize the results
		actSortedResult  = sortPointsIgnoreSegments(actResult);
		PointTest.compareOrderedPoints("LowerRight Intersect", expResult, actSortedResult);
		//Just for grins
		assertFalse("OutsideTheBox the upper Left rectangle should not contain the one to the Right.", inpUpperLeft.contains(inpLowerRight));
		assertFalse("OutsideTheBox the lower Right rectangle should not contain the upper one.", inpLowerRight.contains(inpUpperLeft));
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
		PointTest.compareOrderedPoints("UpperRight Intersect", expResult, actSortedResult);
		actResult = inpUpperRight.intersection(inpLowerLeft);
		assertNotNull("LowerLeft does intersect, but indicated that it does not.", actResult);
		//normalize the results
		actSortedResult  = sortPointsIgnoreSegments(actResult);
		PointTest.compareOrderedPoints("LowerLeft Intersect", expResult, actSortedResult);
		//Just for grins
		assertFalse("OutsideTheBox the upper Right rectangle should not contain the one to the left.", inpUpperRight.contains(inpLowerLeft));
		assertFalse("OutsideTheBox the lower left rectangle should not contain the upper one.", inpLowerLeft.contains(inpUpperRight));
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
		PointTest.compareOrderedPoints("OutsideTheBox Intersect", expResult, actSortedResult);
		//Just for grins
		assertFalse("OutsideTheBox the upper rectangle should not contain the one to the left.", inpOutsideX.contains(inpOutsideY));
		assertFalse("OutsideTheBox the left rectangle should not contain the lower.", inpOutsideY.contains(inpOutsideX));
	}

	/*
	 * One Overlapping segment, plus an intersecting point.
	 */
	@Test
	public void testIntersect_Overlap() {
		final Rectangle inpTheBox = createGoodRectangle(2, 2, 10, 12);
		final Rectangle inpOverlapBox = createGoodRectangle(7, 7, 14, 12);
		final List<Intersection> actResult = inpTheBox.intersection(inpOverlapBox);
		assertNotNull("Overlap does intersect, but indicated that it does not.", actResult);
		//normalize the results
		comparePointsAndSegments("Overlap Intersect", new Point[] { new Point(10, 7) },
				new SimpleSegment[] {new SimpleSegment(new Point(7, 12), new Point(10, 12))}, actResult);
	}
	
	@Test
	public void testIntersect_SinglePoint() {
		final Rectangle inpTheBox = createGoodRectangle(2, 2, 10, 12);
		final Rectangle inpOverlapBox = createGoodRectangle(10, -4, 14, 2);
		final List<Intersection> actResult = inpTheBox.intersection(inpOverlapBox);
		assertNotNull("Overlap does intersect, but indicated that it does not.", actResult);
		final Point expResult = new Point(10, 2);
		assertEquals("Should only return one result.", 1, actResult.size());
		Intersection actFirstResult = actResult.get(0);
		assertFalse("The result should be a Point.", actFirstResult.isLineSegment());
		assertEquals("The overlapping point is incorect.", expResult, actFirstResult);
		assertTrue("Two Rectangles that share a vertex are adjacent.", inpTheBox.adjacentTo(inpOverlapBox));
	}

	@Test
	public void testAdjacent_PartialOverlapRight() throws Exception {
		//Test adjacent with a partially overlapping segment
		final int inp1RightX = 12;
		final int inp1BottomY = -2;
//		final Point inp1LowerRight = new Point(inp1RightX, inp1BottomY);
		final Point inp1UpperRight = new Point(inp1RightX, 18);
		final Point inp1LowerLeft = new Point(-3, inp1BottomY);
		
		final int inp2TopY = 10;
		final int inp2BottomY = 5;
		final Point inp2LowerLeft = new Point(inp1RightX, inp2BottomY);
		final Point inp2UpperRight = new Point(21, inp2TopY);
		
		final Rectangle inpRect1 = new Rectangle(inp1LowerLeft, inp1UpperRight);
		final Rectangle inpRect2 = new Rectangle(inp2LowerLeft, inp2UpperRight);
		
		assertTrue("The rectangles should be adjacent and overlap on the right side of the first Rectangle.",
				inpRect1.adjacentTo(inpRect2));
	}

	//Test adjacent with a completely overlapping segment
	@Test
	public void testAdjacent_CompleteOverlapTop() throws Exception {
		final int inpRightX = 18;
		final int inpLeftX = 10;
		final int inpSharedY = 8;
		final Rectangle inpLowerRect = new Rectangle(new Point(inpLeftX, 2), new Point(inpRightX, inpSharedY));
		final Rectangle inpUpperRect = new Rectangle(new Point(inpLeftX, inpSharedY), new Point(inpRightX, 14));
		assertTrue("The rectangles should be adjacent and overlap completely on the top segment.", inpLowerRect.adjacentTo(inpUpperRect));
		assertFalse("Two identical triangles should not be adjacent.", inpLowerRect.adjacentTo(inpLowerRect));
	}

	//Test adjacent with a partial segment that is 1 away from the other rectangle
//	@Test
//	public void testAdjacent_Partial1AwayLeft() throws Exception {
//		fail("Partial 1-Away segment on the Left");
//	}

	//Test adjacent with a complete segment that is 1 away from the other rectangle
//	@Test
//	public void testAdjacent_Complete1AwayBottom() throws Exception {
//		fail("Adjacent: Complete segment 1-away on the Bottom");
//	}
	
	//Test adjacent and intersect on two identical rectangles
//	@Test
//	public void testAdjacent_Identical() throws Exception {
//		fail("Adjacent and Intersect: 2 identical Rectangle's");
//	}
	
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
	
//	@Test
//	public void testAdjacent_JustInside() throws Exception {
//		fail("Test for a rectangle that intersects because one segment is adjacent to another, but interior");
//	}
	
//	@Test
//	public void testAdjacent_ShareVertex() {
//		fail("Test for to rectangles that share a vertex, should return false.");
//	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	//Ignore
	public void testOutOfOrder1() {
		//This should probably be removed
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
	 * Helper methods
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
	
	protected static void comparePointsAndSegments(String message, Point[] expPoints, SimpleSegment expSegments[],
			List<Intersection> actResult) {
		//sort the expected points
		SortedSet<Point> expSortedPoints = sortPoints(expPoints);
		SortedSet<SimpleSegment> expSortedSegments = sortSegments(expSegments);
		List<Point> actSortedPoints = sortPointsIgnoreSegments(actResult);
		List<SimpleSegment> actSortedSegments = sortSegmentsIgnorePoints(actResult);
		//Iterate expSortedPoints and actSortedPoints
		PointTest.compareOrderedPoints(message, expSortedPoints, actSortedPoints);
		
		//Iterate expSortedSegments and actSortedSegments
		SimpleSegmentTest.compareOrderedSegments(message, expSortedSegments, actSortedSegments);
	}

	/**
	 * 
	 * @param intersections <tt>List&lt;Point&gt;</tt>, probably the result of <tt>Rectangle.intersections()</tt>
	 * @return all of the single <tt>Point</tt>'s in the <tt>List</tt>, ignore any SimpleSegments
	 */
	protected static List<Point> sortPointsIgnoreSegments(List<Intersection> intersections) {
		SortedSet<Point> sortedResult = new TreeSet<Point>();
		for (Intersection thisOne : intersections) {
			if (!thisOne.isLineSegment()) {
				sortedResult.addAll(thisOne.getPoints());
			}
		}
		return new ArrayList<Point>(sortedResult);
	}
	
	protected static List<SimpleSegment> sortSegmentsIgnorePoints(List<Intersection> intersections) {
		SortedSet<SimpleSegment> sortedResult = new TreeSet<SimpleSegment>();
		for (Intersection thisOne : intersections) {
			if (thisOne.isLineSegment()) {
				sortedResult.add((SimpleSegment)thisOne);
			}
		}
		return new ArrayList<SimpleSegment>(sortedResult);
	}
}