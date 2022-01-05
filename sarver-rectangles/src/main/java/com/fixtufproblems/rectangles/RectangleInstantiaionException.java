package com.fixtufproblems.rectangles;

/**
 * The exception thrown there is a problem creating a Rectangle.
 */
public class RectangleInstantiaionException extends Exception {

	private static final long serialVersionUID = -8917426974210512737L;

	public RectangleInstantiaionException() {
		super();
	}

	/**
	 * @param message
	 */
	public RectangleInstantiaionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RectangleInstantiaionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RectangleInstantiaionException(String message, Throwable cause) {
		super(message, cause);
	}
}
