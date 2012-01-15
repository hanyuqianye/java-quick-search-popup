package se.lesc.quicksearchpopup.renderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Renders a bold font for every part of the row that matches the search string.
 */ 
public class BoldRenderer extends MatchRenderer {
   
	/**
	 * The additional with to add to a cell when in quick mode. This is because a bold font
	 * takes more space than a plain with. null mean that a new width value should be calculated. 
	 */
    private Integer extraCellWidth;

	public BoldRenderer() {
    }
    
	public void setQuickRenderMode(boolean quickRenderMode) {
		super.setQuickRenderMode(quickRenderMode);
		
		if (quickRenderMode) {
			//Calculate a new extraWidth value during next render
			extraCellWidth = null;
		}
	}
    
	private int calculateExtraWidth(JList list) {
		Font font = list.getFont();
		FontMetrics fontMetrics =  list.getFontMetrics(font);
		
		Font boldFont = font.deriveFont(Font.BOLD);
		FontMetrics boldFontMetrics =  list.getFontMetrics(boldFont);
		
		//Calculate a new extra size for the bold font
		int extraWidth = boldFontMetrics.stringWidth(searchString) - fontMetrics.stringWidth(searchString);
		return extraWidth;
	}

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
//    	((JLabel) defaultRenderer).setPreferredSize(null);
        Component component = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (quickRenderMode) {
        	
        	if (extraCellWidth == null) {
        		extraCellWidth = calculateExtraWidth(list);
//            	System.out.println(extraCellWidth);
            	Dimension componentPreferredSize = component.getPreferredSize();
            	
            	//TODO: does not work very well, the extra width is added for every time the size is calculated
            	component.setPreferredSize(new Dimension(
            			componentPreferredSize.width + extraCellWidth,
                		componentPreferredSize.height));
        	}

        	return component;
        } else {
            try {
            	component = renderHook(value.toString(), component);
            } catch (Exception e) {
            	System.err.println("Search string: " + searchString);
            	System.err.println(value.toString());
            	e.printStackTrace();  	
            }
        	return component;
        }
    }

    protected Component renderHook(String row, Component component) {

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

    			sb.append(row.substring(previousMatchEnd, matchStart));
    			sb.append("<b>");
    			sb.append(row.substring(matchStart, matchEnd));
    			sb.append("</b>");

    			previousMatchEnd = matchEnd;

    		}

    		sb.append(row.substring(matches[matches.length - 1][1], row.length()));
    		sb.append("</html>");

    		//If the originalRenderer is not a subtype of JLabel we have a problem
    		((JLabel) defaultRenderer).setText(sb.toString());
    	}

    	return component;
    }

}
