package com.fixtufproblems.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Iterator;

public class TestingHelper {
	/**
	 * Execute a series of assertions to ensure that two collections of <tt>Comparable</tt>'s are equal.
	 * 
	 * @param message the message prefix to use if an assertion fails
	 * @param type the type of the object being compared, simply for the output message
	 * @param expected the expected segments, sorted
	 * @param actual the actual segments, sorted
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void compareOrderedComparable(String message, String type, Collection<? extends Comparable> expected,
			Collection<? extends Comparable> actual) {
		if (null == expected) {
			assertNull(message + ": expected null.", actual);
		}
		assertEquals(message + ": returned the wrong number of results.", expected.size(), actual.size());
		Iterator<Comparable> expIterator = (Iterator<Comparable>) expected.iterator();
		Iterator<Comparable> actIterator = (Iterator<Comparable>) actual.iterator();
		int idx = 0;
		//Both iterators have to be the same size because we already checked it.
		while (expIterator.hasNext()) {
			assertEquals(message + ": The " + type + " at position " + idx + " doesn't match expected.", 
					expIterator.next(), actIterator.next());
			idx++;
		}
	}

}
