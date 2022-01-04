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
	 * of one that overlaps a segment of the other, then a SimpleSegment of the overlapping segment is also returned.
	 * <br>This is a symmetric operation: <tt>a.intersection(b) == b.intersection(a)</tt>
	 * <br>and a reflexive operation: <tt>a.intersection(a) == true</tt> (returns all 4 line segments)
	 * @param other the other Rectangle
	 * @return the <tt>Point</tt>'s where <tt>this</tt> Rectangle intersects and the <tt>SimpleSegment</tt>'s
	 * of overlapping line segments with the <tt>other</tt>,
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

	/**
	 * This is a transitive operation: <tt> a.contains(b) && b.contains(c) ==> a.contains(c)</tt>
	 * @param other another <tt>Rectangle</tt>
	 * @return true if all vertices of the given Rectangle are strictly interior to this Rectangle (i.e., no overlapping segments)
	 */
	public boolean contains(Rectangle other) {
		if (null == other) {
			throw new IllegalArgumentException("Can't compare containment to a null Rectangle.");
		}
		return other.getLowerLeft().bothLarger(this.getLowerLeft())
			&& this.getUpperRight().bothLarger(other.getUpperRight());
	}
	
	/**
	 * Two Rectangles are adjacent to each other if neither contains the other, and there exists at least one
	 * line segment that overlaps (same points) or is adjacent to at least one line segment on the other Rectangle.
	 * Here, "adjacent line segments" is defined as parallel to each other at a distance of 1. 
	 * @param other another <tt>Rectangle</tt>
	 * @return true if the other Rectangle is adjacent to this one
	 * @throws IllegalArgumentException if <tt>other</tt> is null
	 */
	public boolean adjacentTo(Rectangle other) {
		if (null == other) {
			throw new IllegalArgumentException("Cannot compare adjacency with a null Rectangle.");
		}
		//Theoretically, we could just compare all segments, but "notInterior()" already went through the trouble...
		final RelativeDirection relDirection = this.notInterior(other);
		//This means that there is an intersection
		if (relDirection == null) {
			return false;
		}
		SimpleSegment closestSegments[] = getClosestSegments(other, relDirection);
		if (null == closestSegments) {
			return false;
		}
		return closestSegments[0].isAdjacent(closestSegments[1]);
	}
	
	/**
	 * Return the two parallel segments from "this" and "other", assuming that "other" is in the given direction.
	 * 
	 * @param other another <tt>Rectangle</tt>
	 * @param direction the relative direction of <tt>other</tt> from <tt>this</tt>
	 * @return an array of <tt>SimpleSegment</tt> where position 0 is from <tt>this</tt> and position 1 is from <tt>other</tt>,
	 * 		or null if something went wrong
	 */
	protected SimpleSegment[] getClosestSegments(Rectangle other, RelativeDirection direction) {
		if (null == other || null == direction) {
			return null;
		}
		switch (direction) {
			case ABOVE:
				return new SimpleSegment[] { new SimpleSegment(this.getUpperLeft(), this.getUpperRight()),
						new SimpleSegment(other.getLowerLeft(), other.getLowerRight())
					};
			case BELOW:
				return new SimpleSegment[] { new SimpleSegment(this.getLowerLeft(), this.getLowerRight()),
						new SimpleSegment(other.getUpperLeft(), other.getUpperRight())
						};
			case LEFT:
				return new SimpleSegment[] { new SimpleSegment(this.getLowerLeft(), this.getUpperLeft()),
						new SimpleSegment(other.getLowerRight(), other.getUpperRight())
						};
			case RIGHT:
				return new SimpleSegment[] { new SimpleSegment(this.getLowerRight(), this.getUpperRight()),
						new SimpleSegment(other.getLowerLeft(), other.getUpperLeft())
						};
			//This should never happen, unless someone adds another value to the enum
			default:
				//Logger.warn("PROGRAMMER ERROR: Someone added a value to Rectangle.RelativeDirection without updating getClosestSegment().");
				return null;
		}
	}
	
//	protected boolean contains(Point aPoint) {
//		return (null != aPoint) && aPoint.bothLarger(this.getLowerLeft())
//				&& this.getUpperRight().bothLarger(aPoint);
//	}
	
	/**
	 * This is different from <tt>!contains(Rectangle)</tt> because it returns true if there are
	 * overlapping line segments.
	 * @param other another <tt>Rectangle</tt>
	 * @return the <tt>RelativeDirection</tt> of the two Rectangles
	 * @see RelativeDirection
	 * @see #contains(Rectangle)
	 */
	protected RelativeDirection notInterior(Rectangle other) {
		if (null == other)
			return null;
		if (other.getLowerLeft().getY() >= this.getUpperRight().getY())
			return RelativeDirection.ABOVE;
		if (other.getUpperRight().getX() <= this.getLowerLeft().getX())
			return RelativeDirection.LEFT;
		if (other.getUpperRight().getY() <= this.getLowerLeft().getY())
			return RelativeDirection.BELOW;
		if (other.getLowerLeft().getX() >= this.getUpperRight().getX())
			return RelativeDirection.RIGHT;
		//If we got here, something is really wrong
		//Logger.warn("PROGRAMMER ERROR: The two rectangles are not in the same Euclidian plane.");
		//TODO throw an IllegalArgumentException?
		return null;
	}
	
	/**
	 * Describes the relationship between Rectangles, as in the following sentence.
	 * <pre>
	 * 	All vertices of "other" are &lt;RelativeDirection&gt; the vertices of "this"
	 * </pre>
	 * Allows for overlapping segments.
	 *
	 */
	protected static enum RelativeDirection {
		ABOVE,
		BELOW, 
		LEFT,
		RIGHT;
	}
}
