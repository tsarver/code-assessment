package com.fixtufproblems.rectangles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a line segment that can only be either horizontal or vertical.
 *
 */
public class SimpleSegment {

	private List<Point> points;
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
		this.points.add(p1);
		this.points.add(p2);
	}
	
	protected boolean isVertical() {
		return !this.isHorizontal;
	}
	
	protected boolean isHorizontal() {
		return this.isHorizontal;
	}
	
	protected List<Point> getPoints() {
		return Collections.unmodifiableList(this.points);
	}
	
	protected List<Point> intersects(SimpleSegment other) {
		List<Point> result = new ArrayList<Point>();
		SimpleSegment horizSegment = null;
		SimpleSegment vertSegment = null;
		if (this.isHorizontal() && other.isHorizontal()) {
			//check for overlap
		}
		if (this.isVertical() && other.isVertical()) {
			//check for overlap
		}
		//At this point we know they aren't parallel
		if (this.isHorizontal) {
			horizSegment = this;
			vertSegment = other;
		} else {
			horizSegment = other;
			vertSegment = this;
		}
		Point intersect = intersectHorizVertical(horizSegment, vertSegment);
		for (Point otherPoint : other.getPoints()) {
			if (onSegment(otherPoint)) {
				result.add(otherPoint);
			}
		}
		return result;
	}
	
	protected Point intersectHorizVertical(SimpleSegment horizSegment, SimpleSegment vertSegment) {
		
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
}
