package se.lesc.quicksearchpopup;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AbstractSearcherTest {

	
	private List<Range> ranges;

	@Before
	public void setUp() {
		ranges = new ArrayList<Range>();
	}

	@Test
	public void testCompress() {
		ranges.add(new Range(5, 10));
		ranges.add(new Range(8, 12));
		
		List<Range> compressedRanges = AbstractSearcher.compress(ranges);
		
		assertEquals(1, compressedRanges.size());
		assertEquals(new Range(5, 12), compressedRanges.get(0));
	}

	@Test
	public void testCompressSeveral() {
		ranges.add(new Range(5, 10));
		ranges.add(new Range(8, 12));
		ranges.add(new Range(10, 12));
		
		List<Range> compressedRanges = AbstractSearcher.compress(ranges);
		
		assertEquals(1, compressedRanges.size());
		assertEquals(new Range(5, 12), compressedRanges.get(0));
	}
	
	
	@Test
	public void testCompressSeveral2() {
		ranges.add(new Range(5, 10));
		ranges.add(new Range(8, 12));
		ranges.add(new Range(15, 20));
		
		List<Range> compressedRanges = AbstractSearcher.compress(ranges);
		
		assertEquals(2, compressedRanges.size());
		assertEquals(new Range(5, 12), compressedRanges.get(0));
		assertEquals(new Range(15, 20), compressedRanges.get(1));
	}
	
}
