package se.lesc.quicksearchpopup.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Panel that allows the user to select/edit the text to search
 */
@SuppressWarnings("serial")
public class TextChooser extends JPanel implements ActionListener {

	private GroupLayout layout = new GroupLayout(this);
	private TextChangedLister textChangedListener;
	
	private JLabel rowsToSearchLabel;
	private JTextArea rowsToSearch;
	private JScrollPane rowsToSearchScrollsPane;
	
	private JRadioButton randomEnlishRadioButton;
	private JRadioButton threadDumpRadioButton;
	private JRadioButton customRadioButton;
	private ButtonGroup buttonGroup;
	private DocumentListener userEditDocumentListener;

	public TextChooser(TextChangedLister textChangedListener) {
		this.textChangedListener = textChangedListener;

		initComponents();
		initLayout();
	}

	private void initComponents() {
		rowsToSearchLabel = new JLabel("Rows to search:");
		rowsToSearch = new JTextArea(10, 80);
		rowsToSearch.setEditable(true);
		
		
		userEditDocumentListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				textChangedByUser();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textChangedByUser();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				textChangedByUser();
			}
		};
		
		rowsToSearch.getDocument().addDocumentListener(userEditDocumentListener);
		
		rowsToSearchScrollsPane = new JScrollPane(rowsToSearch);
		
		randomEnlishRadioButton = new JRadioButton("Random English text");

		randomEnlishRadioButton.addActionListener(this);
		threadDumpRadioButton = new JRadioButton("Java thread dump");
		threadDumpRadioButton.addActionListener(this);
		customRadioButton = new JRadioButton("Custom");
		customRadioButton.addActionListener(this);
		buttonGroup = new ButtonGroup();
		buttonGroup.add(randomEnlishRadioButton);
		buttonGroup.add(threadDumpRadioButton);
		buttonGroup.add(customRadioButton);
		
		randomEnlishRadioButton.doClick();
	}
	
	protected void textChangedByUser() {
		customRadioButton.setSelected(true);
		notifyListener();
	}

	private void initLayout() {
		this.removeAll();
		this.setLayout(layout);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(rowsToSearchLabel)
				.addGroup(layout.createParallelGroup()
						.addComponent(randomEnlishRadioButton)
						.addComponent(threadDumpRadioButton)
						.addComponent(customRadioButton)
				)

				.addComponent(rowsToSearchScrollsPane)
		);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(rowsToSearchLabel)
				.addGroup(layout.createSequentialGroup()
						.addComponent(randomEnlishRadioButton)
						.addComponent(threadDumpRadioButton)
						.addComponent(customRadioButton)
				)				
				.addComponent(rowsToSearchScrollsPane)
		);
	}
	
	private String getExampleData(String filename) {
		try {
			InputStream in = getClass().getResourceAsStream(filename);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == customRadioButton) {
			rowsToSearch.setText("");
		} else if (source == threadDumpRadioButton) {
			loadPredefined("example_jstack.txt");
		} else if (source == randomEnlishRadioButton) {
			loadPredefined("example_random_english.txt");
		}
	}


	private void loadPredefined(String filename) {
		String exampleData = getExampleData(filename);

		
		
		rowsToSearch.getDocument().removeDocumentListener(userEditDocumentListener);
		
		rowsToSearch.setText(exampleData);
		rowsToSearch.setCaretPosition(0);
		rowsToSearch.getDocument().addDocumentListener(userEditDocumentListener);
		notifyListener();
	}

	private void notifyListener() {
		String[] rows = rowsToSearch.getText().split("\n");
		textChangedListener.textChanged(rows);
	}


	/** Callback to call when the user has somehow changed the text */
	public interface TextChangedLister {
		void textChanged(String[] rows);
	}

}
