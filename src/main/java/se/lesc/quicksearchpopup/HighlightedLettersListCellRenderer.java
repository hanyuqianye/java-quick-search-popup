package se.lesc.quicksearchpopup;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Renderer for a list cell to show a string. It renders a bold font for the sub sequence in the
 * string that matches against an input the user has typed.
 * 
 * Note: this is a bit slow. Always use JList.setPrototypeCellValue() to increase speed!
 */ 
public class HighlightedLettersListCellRenderer implements ListCellRenderer {

    private String searchString;
    private ListCellRenderer originalRenderer;
    private Searcher searcher;
   
    /**
     * @param originalRenderer the renderer used in the original JList  
     * @param itemMatcher matcher used to highlight the letters
     */
    public HighlightedLettersListCellRenderer(ListCellRenderer originalRenderer, Searcher searcher) {
        this.originalRenderer = originalRenderer;
        this.searcher = searcher;
    }
    /**
     * @param searchString the text the user has written, it is used as a matcher 
     */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
        Component component = originalRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        String row = value.toString();
        
        int[][] matches = searcher.matchArea(searchString, row);
        
        
        if (matches != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            
            int i = 0;
            int previousMatchEnd = 0;
            for (; i < matches.length; i++) {
            	int[] match = matches[i];
            	int matchStart = match[0];
            	int matchEnd = match[1];
            	
//            	if (previousMatchEnd < 0 || matchStart < 0 || matchStart < previousMatchEnd) {
//            		System.out.println("Error"); //TODO: remove
//            	}
            	
            	sb.append(row.substring(previousMatchEnd, matchStart));
            	sb.append("<b>");
            	sb.append(row.substring(matchStart, matchEnd));
            	sb.append("</b>");
            	
            	previousMatchEnd = matchEnd;
            	
            }
           
            sb.append(row.substring(matches[matches.length - 1][1], row.length()));
            
            sb.append("</html>");
            
            
//            System.out.println(sb.toString());
            
            //If the originalRenderer is not a subtype of JLabel we have a problem
            ((JLabel) originalRenderer).setText(sb.toString());
        }
        return component;
    }

}
