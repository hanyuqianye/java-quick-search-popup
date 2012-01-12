package se.lesc.quicksearchpopup;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WordSearcherTest {
	
	private WordSearcher searcher;
	private String JOHN_TO_MOVIE;
	
	@Before
	public void setUp() {
		JOHN_TO_MOVIE = "John went to the movie";
		searcher = new WordSearcher();
	}

	@Test
	public void testMatches() {
		assertTrue(searcher.matches("John", JOHN_TO_MOVIE));
		assertFalse(searcher.matches("Sarah", JOHN_TO_MOVIE));
		
		assertTrue(searcher.matches("John movie", JOHN_TO_MOVIE)); //Check the AND works
		assertFalse(searcher.matches("John film", JOHN_TO_MOVIE)); //Check that it is not a OR.
	}
	
	@Test
	public void testPartialMatch() {
		int[][] matches = searcher.matchArea("John", JOHN_TO_MOVIE);
		assertEquals(1, matches.length);
		assertEquals(0, matches[0][0]);
		assertEquals(4, matches[0][1]);
		
		matches = searcher.matchArea("to the", JOHN_TO_MOVIE);
		assertEquals(2, matches.length);
		assertEquals(10, matches[0][0]);
		assertEquals(12, matches[0][1]);
		assertEquals(13, matches[1][0]);
		assertEquals(16, matches[1][1]);
	}
	
	@Test
	public void testMatchPartialSeveralTimes() {
		int[][] matches = searcher.matchArea("o", JOHN_TO_MOVIE);
		assertEquals(3, matches.length);
	}
	
	@Test
	public void testMakeSureMatchAreaIsSorted() {
		int[][] matches = searcher.matchArea("movie John", JOHN_TO_MOVIE);
		assertEquals(2, matches.length);
		
		assertEquals(0, matches[0][0]); //"John (as pos 0) should be first
		assertEquals(17, matches[1][0]); //"movie" as pos 17 should be second
	}
	
	@Test
	public void testUnicodeCaseInsensitivity() {
		searcher.setCaseSensitive(false);
		assertTrue(searcher.matches("ö", "Ö"));
		
		searcher.setCaseSensitive(true);
		assertFalse(searcher.matches("ö", "Ö"));
	}
	
	@Test
	public void testJoinSameMatchArea() {
		 int[][] matches = searcher.matchArea("John ohn", JOHN_TO_MOVIE);
		 assertEquals(1, matches.length); //Since "ohn" is part of "John" it should be the same match
	}
	
	
	@Test
	public void testSpaceShouldNotMatch() {
		assertFalse(searcher.matches("  ", JOHN_TO_MOVIE));
	}
	
}
