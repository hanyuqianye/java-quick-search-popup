package se.lesc.quicksearchpopup.example;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import se.lesc.quicksearchpopup.QuickSearchPopup;
import se.lesc.quicksearchpopup.SelectionListener;

/**
 * This is a very simple example that shows basic usage of the QuickSearchPopup
 */
@SuppressWarnings("serial")
public class SimpleExample extends JFrame implements SelectionListener {

	private static final String[] rowsToSearch = {
		"This is the first row",
		"This is the second row",
		"And there are even more rows",
		"Final row!"
	};
	
	public SimpleExample() {
		super("java-quick-search-popup simple example");
		
		JTextField quickSearchField = new JTextField(40);
		QuickSearchPopup quickSearchPopup = new QuickSearchPopup(quickSearchField, this);
		quickSearchPopup.setSearchRows(rowsToSearch);

		add(quickSearchField);
		
		pack();
		setLocationRelativeTo(null); //Center on screen
	}
	
	@Override
	public void rowSelected(String row) {
		System.out.println("User selected row " + row);
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame;
				frame = new SimpleExample();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
