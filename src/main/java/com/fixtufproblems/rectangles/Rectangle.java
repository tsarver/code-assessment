package com.fixtufproblems.rectangles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Rectangle in the strictest sense. Each side is either horizontal or vertical.
 */
public class Rectangle {
	
	private List<SimpleSegment> segments = new ArrayList<SimpleSegment>(4);
	private Point lowerLeft, upperRight;
	
	/**
	 * Creates a Rectangle.
	 * 
	 * @param lowerLeft
	 * @param upperRight
	 * 
	 * @throws IllegalArgumentException if either Point is null,
	 * 	or <tt>upperRight</tt> is not actually up and to the right of <tt>lowerLeft</tt>
	 */
	protected Rectangle(Point lowerLeft, Point upperRight) {
		if (null == lowerLeft || null == upperRight) {
			throw new IllegalArgumentException("Cannot create a Rectangle from a null Point");
		}
		if (upperRight.equals(lowerLeft)) {
			throw new IllegalArgumentException("Cannot create an empty Rectangle.");
		}
		if (!upperRight.bothLarger(lowerLeft)) {
			throw new IllegalArgumentException("Cannot create the Rectangle because Point " + lowerLeft.toString()
			+ " is not below and to the left of Point " + upperRight.toString());
		}
		this.lowerLeft = lowerLeft;
		this.upperRight = upperRight;
		initializeSegments();
	}
	
	protected void initializeSegments() {
		Point prevPoint = this.getLowerLeft();
		Point nextPoint = this.getLowerRight();
		this.segments.add(new SimpleSegment(prevPoint, nextPoint));
		prevPoint = nextPoint;
		nextPoint = this.getUpperRight();
		this.segments.add(new SimpleSegment(prevPoint, nextPoint));
		prevPoint = nextPoint;
		nextPoint = this.getUpperLeft();
		this.segments.add(new SimpleSegment(prevPoint, nextPoint));
		prevPoint = nextPoint;
		nextPoint = this.getLowerLeft();
		this.segments.add(new SimpleSegment(prevPoint, nextPoint));
	}
	
	protected List<SimpleSegment> getSegments() {
		return Collections.unmodifiableList(this.segments);
	}

	/**
	 * Creates a Rectangle.
	 * @param vertex1
	 * @param vertex2
	 * @param vertex3
	 * @param vertex4
	 * 
	 * @throws IllegalArgumentException if line segments are not all parallel to either the x-axis or y-axis.
	 */
	@Deprecated
	protected Rectangle(Point vertex1, Point vertex2, Point vertex3, Point vertex4) throws IllegalArgumentException {
		this(Arrays.asList(new Point[] {vertex1, vertex2, vertex3, vertex4}));
	}
	
	@Deprecated
	protected Rectangle(List<Point> points) throws IllegalArgumentException {
		if (null == points || 4 != points.size()) {
			throw new IllegalArgumentException("The points can't be null and must be 4 in length.");
		}
		//Assume it is of length 4
		Iterator<Point> iterator = points.iterator();
		Point prevPoint = iterator.next();
		if (null == prevPoint) {
			throw new IllegalArgumentException("Rectangle cannot accept a null Point.");
		}
		Point firstPoint = prevPoint;
		Point upperCorner = prevPoint;
		Point lowerCorner = prevPoint;
		while(iterator.hasNext()) {
			Point nextPoint = iterator.next();
			if (null == nextPoint) {
				throw new IllegalArgumentException("Rectangle cannot accept a null Point.");
			}
			if (upperCorner.eitherSmaller(nextPoint)) {
				upperCorner = nextPoint;
			}
			if (nextPoint.eitherSmaller(lowerCorner)) {
				lowerCorner = nextPoint;
			}
		}
		//TODO check for only horizontal and vertical segments
	}
	
	protected Point getUpperRight() {
		return this.upperRight;
	}
	
	protected Point getLowerLeft() {
		return this.lowerLeft;
	}
	
	protected Point getUpperLeft() {
		return new Point(this.lowerLeft.getX(), this.upperRight.getY());
	}
	
	protected Point getLowerRight() {
		return new Point(this.upperRight.getX(), this.lowerLeft.getY());
	}


	/**
	 * Returns the points where the Rectangles intersect, or null if they don't intersect. If there is a segment
	 * of one that overlaps a segment of the other, then the end points of the overlapping segment is returned
	 * (not all points along the segment).
	 * @param other the other Rectangle
	 * @return the <tt>Point</tt>'s where <tt>this</tt> Rectangle intersects with the <tt>other</tt>,
	 * or <tt>null</tt> if they don't intersect
	 */
	public List<Intersection> intersection(Rectangle other) {
		if (null == other) {
			return null;
		}
		//Avoid duplicates in the response
		SortedSet<Intersection> sortedResult = new TreeSet<>();
		//Assume neither has null segments and they are the same size
		for(SimpleSegment thisSegment : this.segments) {
			for (SimpleSegment otherSegment : other.segments) {
				//They overlap only if they have the same orientation and on the same line
				Intersection overlapSegment = thisSegment.overlaps(otherSegment);
				if (null != overlapSegment) {
					sortedResult.add(overlapSegment);
					continue;
				}
				//They intersect only if they have different orientation
				Point intersectPoint = thisSegment.intersects(otherSegment);
				if (null != intersectPoint ) {
					sortedResult.add(intersectPoint);
				}				
			}
		}
		if (sortedResult.isEmpty()) {
			return null;
		}
		//Now we need to remove points that are represented with segments
		//These got added because a point from one segment lies on another perpendicular segment
		final List<SimpleSegment> segments = new ArrayList<>(sortedResult.size());
		final List<Point> points = new ArrayList<>(sortedResult.size());
		for (Intersection thisInter : sortedResult) {
			if (thisInter.isLineSegment()) {
				segments.add((SimpleSegment) thisInter);
			} else {
				points.add((Point) thisInter);
			}
		}
		for (SimpleSegment overlap : segments) {
			//Assume that the "extra" points can only occur on the ends of the overlap, not in the middle.
			for (Point overlapPoint : overlap.getPoints()) {
				final Iterator<Point> iterPoints = points.iterator();
				while(iterPoints.hasNext()) {
					if (overlapPoint.equals(iterPoints.next())) {
						iterPoints.remove();
					}
				}
			}
		}
		List<Intersection> result = new ArrayList<>(segments.size() + points.size());
		result.addAll(points);
		result.addAll(segments);
		return result;
	}

	public boolean contains(Rectangle other) {
		if (null == other) {
			throw new IllegalArgumentException("Can't compare to a null Rectangle.");
		}
		return other.getLowerLeft().bothLarger(this.getLowerLeft())
			&& this.getUpperRight().bothLarger(other.getUpperRight());
	}
	
	public boolean adjacent(Rectangle other) {
		if (this.contains(other))  //TODO call simple intersect
			return false;
		return false;
	}
	
	protected boolean contains(Point aPoint) {
		return (null != aPoint) && aPoint.bothLarger(this.getLowerLeft())
				&& this.getUpperRight().bothLarger(aPoint);
	}
}
