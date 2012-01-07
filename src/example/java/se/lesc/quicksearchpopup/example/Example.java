package se.lesc.quicksearchpopup.example;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import se.lesc.quicksearchpopup.QuickSearcher;
import se.lesc.quicksearchpopup.SelectionListener;

public class Example extends JFrame implements SelectionListener {

	private GroupLayout layout = new GroupLayout(this.getContentPane());
	private JTextField quickSearchTestField; 
	
	private JTextArea addedRows;
	
	private QuickSearcher quickSearecher;
	private String[] rows;
	private JScrollPane addedRowsScrollsPane;

	public Example() throws IOException {
		super("java-quick-search-popup");
		
		addedRows = new JTextArea(10, 100);
		addedRowsScrollsPane = new JScrollPane(addedRows);
		
		initLayout();
		pack();
		setLocationRelativeTo(null);

		quickSearchTestField.setToolTipText("Write search word, separated with spaces");
		quickSearchTestField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

		quickSearecher = new QuickSearcher(quickSearchTestField, this);
		

		
		String exampleData = getExampleData();
		rows = exampleData.split("\n");

//		String firstRow = rows[1];
//		rows = new String[1];
//		rows[0] = firstRow;
		quickSearecher.setSearchRows(rows);
	}

	private String getExampleData() throws IOException {
		InputStream in = getClass().getResourceAsStream("example_jstack.txt");
		int ch;
		StringBuffer sb = new StringBuffer();
		while ((ch = in.read()) != -1) {
			sb.append((char) ch);
		}
		String exampleData = sb.toString();
		return exampleData;
	}

	private void initLayout() {
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(layout);

		JLabel helloLabel = new JLabel("Hello!");
		quickSearchTestField = new JTextField(15);
		
		JCheckBox checkbox1 = new JCheckBox("This is checkbox 1");
		
		JCheckBox checkbox2 = new JCheckBox("This is checkbox 2");

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(checkbox1)
				.addComponent(helloLabel)
				.addComponent(quickSearchTestField)
				.addComponent(checkbox2)
				.addComponent(addedRowsScrollsPane)
		);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(checkbox1)
				.addComponent(helloLabel)
				.addComponent(quickSearchTestField)
				.addComponent(checkbox2)
				.addComponent(addedRowsScrollsPane)			
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
	public void add(String row) {
		
		addedRows.setText(addedRows.getText() + row + "\n");
	}

}
