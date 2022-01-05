package com.fixtufproblems.rectangles;

import java.util.List;

public interface Intersection {

	/**
	 * 
	 * @return true if the intersection is a line segment, false if it is a point
	 */
	boolean isLineSegment();
	
	/**
	 * 
	 * @return either the single point of intersection, or the points that represent the overlapping line segment.
	 */
	List<Point> getPoints();
}
