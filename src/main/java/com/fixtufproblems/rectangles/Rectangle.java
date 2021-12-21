package com.fixtufproblems.rectangles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A Rectangle in the strictest sense. Each side is either horizontal or vertical.
 */
public class Rectangle {
	
	private List<SimpleSegment> segments = new ArrayList<SimpleSegment>();
	private Point lowerLeft, upperRight;
	
	/**
	 * Creates a Rectangle.
	 * @param vertex1
	 * @param vertex2
	 * @param vertex3
	 * @param vertex4
	 * 
	 * @throws IllegalArgumentException if the vertices do not define a path either clockwise or counter-clockwise,
	 * and if line segments are not all parallel to either the x-axis or y-axis.
	 */
	protected Rectangle(Point vertex1, Point vertex2, Point vertex3, Point vertex4) throws IllegalArgumentException {
		this(Arrays.asList(new Point[] {vertex1, vertex2, vertex3, vertex4}));
	}
	
	protected Rectangle(List<Point> points) throws IllegalArgumentException {
		if (null == points || 4 != points.size()) {
			throw new IllegalArgumentException("The points can't be null and must be 4 in length.");
		}
		//Assume it is of length 4
		Iterator<Point> iterator = points.iterator();
//		Point prevPoint = iterator.next();
//		Point firstPoint = prevPoint;
		Point upperCorner = null, lowerCorner = null;
		while(iterator.hasNext()) {
			Point nextPoint = iterator.next();
			if (null == nextPoint) {
				throw new IllegalArgumentException("Rectangle cannot accept a null Point.");
			}
			if (null != upperCorner) {
				if (upperCorner.eitherSmaller(nextPoint)) {
					upperCorner = nextPoint;
				}
			} else {
				upperCorner = nextPoint;
			}
			if (null != lowerCorner) {
				if (nextPoint.eitherSmaller(lowerCorner)) {
					lowerCorner = nextPoint;
				}
			} else {
				lowerCorner = nextPoint;
			}
			//This does the checks for horizontal and vertical
			//TODO maybe surround this try..catch and re-write the message
//			this.segments.add(new SimpleSegment(prevPoint, nextPoint));
//			prevPoint = nextPoint;
		}
//		this.segments.add(new SimpleSegment(prevPoint, firstPoint));
	}

	/**
	 * Returns the points where the Rectangles intersect, or null if they don't intersect. If there is a segment
	 * of one that overlaps a segment of the other, then the end points of the overlapping segment is returned
	 * (not all points along the segment).
	 * @param other the other Rectangle
	 * @return the <tt>Point</tt>'s where <tt>this</tt> Rectangle intersects with the <tt>other</tt>,
	 * or <tt>null</tt> if they don't intersect
	 */
	public Point [] intersects(Rectangle other) {
		List<Point> result = new ArrayList<Point>();
		//Get horizontal and vertical segments
		List<Point> myHoriz = this.getHorizontalSegments();
		List<Point> otherHoriz = other.getHorizontalSegments();
		List<Point> myVertical = this.getVerticalSegments();
		List<Point> otherVertical = other.getVerticalSegments();
		//Compare verticals to horizontals
		//Check for overlapping segments by comparing like to like
		
		return (Point[]) result.toArray();
	}

	protected List<Point> getHorizontalSegments() {
		return null;
	}
	
	protected List<Point> getVerticalSegments() {
		return null;
	}
}
