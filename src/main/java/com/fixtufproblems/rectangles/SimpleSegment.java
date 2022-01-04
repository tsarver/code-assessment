package com.fixtufproblems.rectangles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This is a line segment that can only be either horizontal or vertical.
 *
 */
public class SimpleSegment implements Comparable<SimpleSegment>, Intersection {

	//This ensures that the first one is either left of or below the second one
	private List<Point> points = new ArrayList<>();
	private boolean isHorizontal = false;
	
	/**
	 * Creates a <tt>SimpleSegment</tt> between the two points
	 * @param p1
	 * @param p2
	 * @throws IllegalArgumentException if the line is not strictly either horizontal or vertical
	 */
	protected SimpleSegment(Point p1, Point p2) throws IllegalArgumentException {
		if (null == p1 || null == p2) {
			throw new IllegalArgumentException("Cannot create a SimpleSegment from a null Point.");
		}
		if (p1.equals(p2)) {
			throw new IllegalArgumentException("Cannon creae a zero-length SimpleSegment.");
		}
		boolean isSimple = false;
		if (p1.getX() == p2.getX()) {
			isHorizontal = false;
			isSimple = true;
		}
		final boolean sameY = p1.getY() == p2.getY();
		if (isSimple && sameY) {
			throw new IllegalArgumentException("The 2 points can't be the same.");
		}
		if (sameY) {
			isHorizontal = true;
			isSimple = true;
		}
		if (!isSimple) {
			throw new IllegalArgumentException(
					"The line segment must be either horizontal or vertical: " + p1 + ", " + p2);
		}
		Point sorted[] = new Point[] {p1, p2};
		Arrays.sort(sorted);
		this.points.add(sorted[0]);
		this.points.add(sorted[1]);
		
	}
	
	protected boolean isVertical() {
		return !this.isHorizontal;
	}
	
	protected boolean isHorizontal() {
		return this.isHorizontal;
	}
	
	protected Iterator<Point> iteratePoints() {
		return this.points.iterator();
	}
	
	public List<Point> getPoints() {
		return Collections.unmodifiableList(this.points);
	}
	
	protected Point getLowerPoint() {
		return this.points.get(0);
	}
	
	protected Point getHigherPoint() {
		return this.points.get(1);
	}
	
	/**
	 * 
	 * @param other another SimpleSegment, not null
	 * @return the point at which this segment intersects the <tt>other</tt> one,
	 * 		or null if they don't, or if <tt>other</tt> is null
	 * @see #overlaps(SimpleSegment)
	 */
	protected Point intersects(SimpleSegment other) {
		if (null == other) {
			return null;
		}
		SimpleSegment horizSegment = null;
		SimpleSegment vertSegment = null;
		//Check that they aren't parallel
		if (this.isHorizontal) {
			if (other.isHorizontal) {
				//Both horizontal, The intersection is not a single point, use "overlaps()"
				return null;
			}
			horizSegment = this;
			vertSegment = other;
		} else {
			if (!other.isHorizontal) {
				//Both vertical, The intersection is not a single point, use "overlaps()"
				return null;
			}
			horizSegment = other;
			vertSegment = this;
		}
		return intersectHorizVertical(horizSegment, vertSegment);
	}
	
	/**
	 * The intersect could be a shared vertex, or a vertex of one segment somewhere in the other segment.
	 * 
	 * @param horizSegment a horizontal <tt>SimpleSegment</tt>
	 * @param vertSegment a vertical <tt>SimpleSegment</tt>
	 * @return the intersection between two <tt>SimpleSegment</tt>'s
	 */
	protected static Point intersectHorizVertical(SimpleSegment horizSegment, SimpleSegment vertSegment) {
		//Yes, this could be an instance method, but it wouldn't be nearly as clear which is horiz and which is vertical
		//Note that this doesn't check the pre-condition re: the orientation of each segment.
		Point leftHoriz = horizSegment.getLowerPoint();
		Point rightHoriz = horizSegment.getHigherPoint();
		Point bottomVertical = vertSegment.getLowerPoint();
		Point topVertical = vertSegment.getHigherPoint();
		
		//Check that they intersect
		//If the y-value of the horizontal line is somewhere between the top and bottom of the vertical line
		if (leftHoriz.getY() >= bottomVertical.getY() && leftHoriz.getY() <= topVertical.getY()
				//And the x-value of the vertical line is somewhere between the left and right of the horizontal line
				&& bottomVertical.getX() >= leftHoriz.getX() && bottomVertical.getX() <= rightHoriz.getX()) {
			//If they intersect, it would be here
			Point result = new Point(bottomVertical.getX(), leftHoriz.getY());
			//or, equivalently:
			//Point result = new Point(topVertical.getX(), rightHoriz.getY());
			return result;
		}
		return null;
	}

	/**
	 * 
	 * @param other another SimpleSegment, not null
	 * @return the line segment represents the overlap, or null if they don't overlap,
	 * 	or a Point if only overlap as a point
	 */
	protected Intersection overlaps(SimpleSegment other) {
		if (null == other) {
			return null;
		}
		if (this.isHorizontal() && other.isHorizontal()) {
			final Point thisLeft = this.getLowerPoint();
			final Point otherLeft = other.getLowerPoint();
			//If they aren't on the same line
			if (thisLeft.getY() != otherLeft.getY()) {
				return null;
			}
			//If they are on the same Y line and the other.left isn't right of this.right
			//and other.right isn't left of this.left
			final int thisRightX = this.getHigherPoint().getX();
			final int otherRightX = other.getHigherPoint().getX();
			if (!(otherLeft.getX() > thisRightX || otherRightX < thisLeft.getX())) {
				Point resultLeft = new Point(Math.max(otherLeft.getX(), thisLeft.getX()), thisLeft.getY());
				Point resultRight = new Point(Math.min(otherRightX, thisRightX), thisLeft.getY());
				if (resultLeft.equals(resultRight)) {
					//Can't build a SimpleSegment from a single point,
					return resultLeft;
				}
				return new SimpleSegment(resultLeft, resultRight);
			}
			//We know they are both horizontal, but don't overlap
			return null;
		}
		if (this.isVertical() && other.isVertical()) {
			//check for overlap
			final Point thisBottom = this.getLowerPoint();
			final Point otherBottom = other.getLowerPoint();
			//If they aren't on the same line
			if (thisBottom.getX() != otherBottom.getX()) {
				return null;
			}
			final int thisTopY = this.getHigherPoint().getY();
			final int otherTopY = other.getHigherPoint().getY();
			//If they are on the same X line and other.bottom isn't above this.top
			//and other.top isn't below this.bottom
			if (!(otherBottom.getY() > thisTopY || thisBottom.getY() > otherTopY)) {
				Point resultBottom = new Point(thisBottom.getX(), Math.max(thisBottom.getY(), otherBottom.getY()));
				Point resultTop = new Point(thisBottom.getX(), Math.min(otherTopY, thisTopY));
				if (resultBottom.equals(resultTop)) {
					return resultBottom;
				}
				return new SimpleSegment(resultBottom, resultTop);
			}
		}
		return null;
	}
	
	/**
	 * Returns the vertex that is shared between this segment and the <tt>other</tt>.
	 * If there are two shared vertices, then the segments are equivalent.
	 * @param other another <tt>SimpleSegment<tt>, not null
	 * @return the shared vertex between this and the <tt>other</tt> line segment,
	 * 		or null if there is none
	 * @throws IllegalArgumentException if the two segments are equivalent, or <tt>other</tt> is null
	 */
	protected Point hasSharedVertex(SimpleSegment other) {
		if (null == other) {
			throw new IllegalArgumentException("Cannot compare to a null line segment.");
		}
		Point result = null;
		for (Point onThis : this.points) {
			for (Point onOther : other.points) {
				if (onThis.equals(onOther)) {
					if (null == result) {
						//recall that Point is immutable, therefore we aren't worried about the caller changing it
						result = onThis;
					} else {
						//both segments are equivalent
						throw new IllegalArgumentException("The two segments are equivalent.");
					}
				}
			}
		}
		return result;
	}
	
	public boolean onSegment(Point otherPoint) {
		Point firstPoint = this.points.get(0);
		Point secPoint = this.points.get(1);
		
		return onSegmentPoint(firstPoint, secPoint, otherPoint);
	}

	protected boolean onSegmentPoint(Point firstPoint, Point secPoint, Point otherPoint) {
		return otherPoint.getX() <= Math.max(firstPoint.getX(), secPoint.getX()) && otherPoint.getX() >= Math.min(firstPoint.getX(),  secPoint.getX())
				&& otherPoint.getY() <= Math.max(firstPoint.getY(), secPoint.getY()) && otherPoint.getY() >= Math.min(firstPoint.getY(),  secPoint.getY());
	}

	@Override
	public boolean isLineSegment() {
		return true;
	}

	public boolean equals(Object otherObj) {
		if (otherObj == null || !(otherObj instanceof SimpleSegment)) {
			return false;
		}
		SimpleSegment other = (SimpleSegment)otherObj;
		//If this is true, there's probably a coding problem
		if (null == other.points || other.points.size() != this.points.size()) {
			//logger.info("PROGRAMMER ERROR: No SimpleSegment should have null \"points\", or a size other than 2.");
			return false;			
		}
		//Assume these are sorted and non-null
		Iterator<Point> otherIterator = other.iteratePoints();
		for (Point thisPoint : this.points ) {
			Point otherPoint = otherIterator.next();
			if (!thisPoint.equals(otherPoint)) {
				return false;
			}
		}
		//At this point, all thisPoint and otherPoint are equal
		return true;
	}

	@Override
	public int compareTo(SimpleSegment other) {
		if (null == other) {
			//null's come first, by a long shot
			return Integer.MAX_VALUE;
		}
		//If this is true, there's probably a coding problem
		if (null == other.points || other.points.size() != this.points.size()) {
			//logger.info("PROGRAMMER ERROR: No SimpleSegment should have null \"points\", or a size other than 2.");
			return Integer.MAX_VALUE;
		}
		//Assume these are sorted and non-null
		Iterator<Point> otherIterator = other.iteratePoints();
		for (Point thisPoint : this.points ) {
			Point otherPoint = otherIterator.next();
			int compared = thisPoint.compareTo(otherPoint);
			if (compared != 0) {
				return compared;
			}
		}
		return 0;
	}
}