package se.lesc.quicksearchpopup.example;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.Font;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import se.lesc.quicksearchpopup.QuickSearchPopup;
import se.lesc.quicksearchpopup.SelectionListener;
import se.lesc.quicksearchpopup.example.TextChooser.TextChangedLister;
import se.lesc.quicksearchpopup.renderer.MatchRenderer;

/**
 * Example application the shows how the Quick Search Popup works. 
 */
@SuppressWarnings("serial")
public class Example extends JFrame implements SelectionListener, TextChangedLister {

	private GroupLayout layout = new GroupLayout(this.getContentPane());

	private TextChooser textChooser;

	private JLabel quickSearchLabel;
	private JTextField quickSearchField; 
	private QuickSearchPopup quickSearchPopup;
	
	private JLabel addedRowsLabel;
	private JTextArea addedRows;
	private JScrollPane addedRowsScrollsPane;
	
	private FontChooser fontChooser;
	private MatchRendererChooser highlighterChooser;

	public Example() throws IOException {
		super("java-quick-search-popup example");
		
		initComponents();
		initLayout();
		pack();
		setLocationRelativeTo(null); //Center on screen
		quickSearchField.requestFocus();
	}

	private void initComponents() {
		quickSearchLabel = new JLabel("Quick Search:");
		quickSearchField = new JTextField(15);
		quickSearchField.setToolTipText("Write search word, separated with spaces");

		quickSearchPopup = new QuickSearchPopup(quickSearchField, this);
		
		textChooser = new TextChooser(this);
		
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
		
		highlighterChooser = new MatchRendererChooser(new MatchRendererChooser.RendererChooserListener() {
			@Override
			public void rendererChanged(MatchRenderer newRenderer) {
				quickSearchPopup.setMatchRenderer(newRenderer);
			}
		});
		quickSearchPopup.setMatchRenderer(highlighterChooser.getSelectedRenderer());
	}

	private void initLayout() {
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(layout);

		layout.setAutoCreateContainerGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(textChooser)
				.addPreferredGap(RELATED)
				.addGroup(layout.createParallelGroup()
						.addComponent(highlighterChooser)
						.addComponent(fontChooser)
				)
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
				.addComponent(textChooser)
				.addGroup(layout.createSequentialGroup()
						.addComponent(highlighterChooser)
						.addPreferredGap(RELATED)
						.addComponent(fontChooser)
				)
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

	@Override
	public void textChanged(String[] rows) {
		quickSearchPopup.setSearchRows(rows);
	}
}

