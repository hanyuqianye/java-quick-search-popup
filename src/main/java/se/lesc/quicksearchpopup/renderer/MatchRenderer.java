package se.lesc.quicksearchpopup.renderer;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import se.lesc.quicksearchpopup.Searcher;

/**
 * Renderer for a list cell to show how the user search string has matched the row.  
 */
public abstract class MatchRenderer implements ListCellRenderer {
	
    protected ListCellRenderer defaultRenderer;

    protected String searchString;
    protected Searcher searcher;
    protected boolean quickRenderMode = false;

    public MatchRenderer() {
    	this.defaultRenderer = new JList().getCellRenderer();
    }
    
    /**
     * Informs the renderer what the user has searched for and with which searcher
     */
	public void setSearchInforomation(String searchString, Searcher searcher) {
		this.searchString = searchString;
		this.searcher = searcher;
	}
    
    /**
     * Enabling quick more rendering, useful because some cell renderings may consume a lot of time
     */
	public void setQuickRenderMode(boolean quickRenderMode) {
		this.quickRenderMode = quickRenderMode;
	}
	
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
        Component component = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (quickRenderMode) {
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

    /** Called by base class for the actual rendering */
	protected abstract Component renderHook(String string, Component component);
    
}
