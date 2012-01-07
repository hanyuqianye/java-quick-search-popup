package se.lesc.quicksearchpopup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;


public class RangeTest {

	private Range range = new Range(10, 12); //Base
	private Range i1 = new Range(8, 13); //Completely covers 
	private Range i2 = new Range(8, 11); //Covers lower bound
	private Range i3 = new Range(11, 13); //Covers upper bound
	private Range i4 = new Range(11, 11); //Inside
	
	private Range o1 = new Range(8, 9); //Too low
	private Range o2 = new Range(13, 14); //Too high
	
	@Test
	public void testInside() {
		assertTrue(range.inside(10));
		assertTrue(range.inside(11));
		assertTrue(range.inside(12));

		assertFalse(range.inside(9));
		assertFalse(range.inside(13));
	}
	
	@Test
	public void testIntersects() {
		assertTrue(range.intersects(i1));
		assertTrue(range.intersects(i2));
		assertTrue(range.intersects(i3));
		assertTrue(range.intersects(i4));
		
		assertFalse(range.intersects(o1));
		assertFalse(range.intersects(o2));
	}
	
	@Test
	public void testJoin() {
		Range joinedRange;
		
		joinedRange = Range.join(range, i1);
		assertEquals(8, joinedRange.from);
		assertEquals(13, joinedRange.to);
		
		joinedRange = Range.join(range, i2);
		assertEquals(8, joinedRange.from);
		assertEquals(12, joinedRange.to);
		
		joinedRange = Range.join(range, i3);
		assertEquals(10, joinedRange.from);
		assertEquals(13, joinedRange.to);
	
		joinedRange = Range.join(range, i4);
		assertEquals(10, joinedRange.from);
		assertEquals(12, joinedRange.to);
	}
	
	@Test
	public void testSort() {
	
		ArrayList<Range> ranges = new ArrayList<Range>();
		ranges.add(range);
		ranges.add(i1);
		ranges.add(i2);
		ranges.add(i3);
		ranges.add(i4);
		
		Collections.sort(ranges);
		
		assertEquals(i1, ranges.get(0));
		assertEquals(i2, ranges.get(1));
		assertEquals(range, ranges.get(2));
		assertEquals(i3, ranges.get(3));
		assertEquals(i4, ranges.get(4));
	}
	
}



