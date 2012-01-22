package se.lesc.quicksearchpopup.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;

/**
 * Randomly inserts line breaks in a text with sentences.
 */
public class RandomLineBreaker {

	public static void main(String[] args) throws IOException {
		
		InputStream in = RandomLineBreaker.class.getResourceAsStream("random_english.txt");
		List<String> lines = IOUtils.readLines(in);
		
		List<String> result = new ArrayList<String>();
		
		for (String row : lines) {
			row = row.trim();
			if (row.isEmpty()) {
				continue;
			}
			
			StringTokenizer st = new StringTokenizer(row, ".");
			
			String resultRow = null;
			while (st.hasMoreTokens()) {

				String sentence = st.nextToken();
				sentence = sentence.trim();

				if (resultRow != null) {
					resultRow = resultRow + ". " + sentence;
				} else {
					resultRow = sentence;
				}
				
				if (Math.random() < 0.5f) {
					result.add(resultRow);
					resultRow = null;
				}
			}
			
			if (resultRow != null) {
				result.add(resultRow);
			}
		}
		
		for (String row : result) {
			System.out.println(row);
		}
	
	}

}
