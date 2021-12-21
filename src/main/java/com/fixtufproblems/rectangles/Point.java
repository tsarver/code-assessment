package com.fixtufproblems.rectangles;

/**
 * This is a point in 2D integer space. It is immutable.
 */
public class Point implements Comparable<Point> {
	
	private int x,y;
	
	protected Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	/**
	 * Implements the ideal semantics in which the absolute value of the result is relative to how
	 * different/far away the rectangles are.
	 * 
	 * @param other the other Rectangle
	 * @return negative if this Rectangle is left or below the other one
	 */
	public int compareTo(Point other) {
		if (null == other) {
			//Null always come first
			//Chose not to throw an exception in this case
			return Integer.MAX_VALUE;
		}
		final int xDiff = other.x - this.x;
		if (0 != xDiff) {
			return xDiff;
		}
		final int yDiff = other.y - this.y;
		if (0 != yDiff) {
			return yDiff;
		}
		//They must be equal
//		if (this.x == other.x && this.y == other.y ) {
			return 0;
//		}
	}
	
	@Override
	public boolean equals(Object otherObj) {
		if (null == otherObj) {
			return false;
		}
		if (!(otherObj instanceof Point)) {
			return false;
		}
		Point other = (Point)otherObj;
		return (this.x == other.x && this.y == other.y);
	}

	/**
	 * 
	 * @param other another Point, not null
	 * @return true if the given Point is up and to the right of this one.
	 */
	public boolean bothLarger(Point other) {
		if (null == other)
			return false;
		return this.getX() < other.getX() && this.getY() < other.getY();
		
	}

	/**
	 * 
	 * @param other another Point, not null
	 * @return true if the given point is down or to the left of this one.
	 */
	public boolean eitherSmaller(Point other) {
		if (null == other)
			return false;
		return other.getX() < this.getX() || other.getY() < this.getY();
	}
}
