package se.lesc.quicksearchpopup.example;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import se.lesc.quicksearchpopup.QuickSearchPopup;
import se.lesc.quicksearchpopup.SelectionListener;

/**
 * Example application the shows how the Quick Search Popup works. 
 */
@SuppressWarnings("serial")
public class Example extends JFrame implements SelectionListener {

	private GroupLayout layout = new GroupLayout(this.getContentPane());
	
	private JLabel rowsToSearchLabel;
	private JTextArea rowsToSearch;
	private JScrollPane rowsToSearchScrollsPane;
		
	private JLabel quickSearchLabel;
	private JTextField quickSearchField; 
	private QuickSearchPopup quickSearchPopup;
	
	private JLabel addedRowsLabel;
	private JTextArea addedRows;
	private JScrollPane addedRowsScrollsPane;
	
	private FontChooser fontChooser;

	private String[] rows;

	public Example() throws IOException {
		super("java-quick-search-popup example");
		
		initComponents();
		setRowsToSearch();
		initLayout();
		pack();
		setLocationRelativeTo(null); //Center on screen
	}

	private void initComponents() {
		rowsToSearchLabel = new JLabel("Rows to search:");
		rowsToSearch = new JTextArea(10, 80);
		rowsToSearch.setEditable(false);
		rowsToSearchScrollsPane = new JScrollPane(rowsToSearch);

		quickSearchLabel = new JLabel("Quick Search:");
		quickSearchField = new JTextField(15);
		quickSearchField.setToolTipText("Write search word, separated with spaces");

		quickSearchPopup = new QuickSearchPopup(quickSearchField, this);
		
		addedRowsLabel = new JLabel("Selected rows:");
		addedRows = new JTextArea(10, 80);
		addedRowsScrollsPane = new JScrollPane(addedRows);
		
		fontChooser = new FontChooser(new FontChooser.FontChangedLister() {
			@Override
			public void fontChanged(Font newFont) {
				quickSearchField.setFont(newFont);
				validate();
			}
		});
	}
	
	private void setRowsToSearch() {
		String exampleData = getExampleData();
		rows = exampleData.split("\n");
		
		quickSearchPopup.setSearchRows(rows);
		rowsToSearch.setText(exampleData);
		rowsToSearch.setCaretPosition(0);
	}

	private String getExampleData() {
		try {
			InputStream in = getClass().getResourceAsStream("example_jstack.txt");
			int ch;
			StringBuffer sb = new StringBuffer();

			while ((ch = in.read()) != -1) {
				sb.append((char) ch);
			}

			String exampleData = sb.toString();
			return exampleData;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void initLayout() {
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(layout);

		layout.setAutoCreateContainerGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(rowsToSearchLabel)
						.addComponent(rowsToSearchScrollsPane)
				)
				.addComponent(fontChooser)
				.addPreferredGap(RELATED)				
				.addGroup(layout.createSequentialGroup()
						.addComponent(quickSearchLabel)						
						.addComponent(quickSearchField, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addPreferredGap(RELATED)
				.addGroup(layout.createSequentialGroup()
						.addComponent(addedRowsLabel)
						.addComponent(addedRowsScrollsPane)
				)
		);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(rowsToSearchLabel)
						.addComponent(rowsToSearchScrollsPane)
				)
				.addComponent(fontChooser)
				.addGroup(layout.createParallelGroup()
						.addComponent(quickSearchLabel)
						.addComponent(quickSearchField)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(addedRowsLabel)
						.addComponent(addedRowsScrollsPane)
				)
		);
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame;
				try {
					frame = new Example();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void rowSelected(String row) {
		addedRows.setText(addedRows.getText() + row + "\n");
	}
}

