package se.lesc.quicksearchpopup.example;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Very simple panel to choose a font
 */
@SuppressWarnings("serial")
public class FontChooser extends JPanel implements ActionListener {

	private GroupLayout layout = new GroupLayout(this);
	
	private FontChangedLister fontChangedListener;
	
	private JLabel fontChooserLabel;
	private JComboBox fontChooserComboBox;

	private JLabel fontSizeLabel;
	private JComboBox fontSizeComboBox;

	public FontChooser(FontChangedLister fontChangedListener) {
		this.fontChangedListener = fontChangedListener;
		initComponents();
		initLayout();
	}

	private void initComponents() {
		Font defaultFont = getFont();
		
		fontChooserLabel = new JLabel("Font:");
		fontChooserComboBox = new JComboBox(getFontFamilyNames());

		fontChooserComboBox.setSelectedItem(defaultFont.getFamily());
		
		fontSizeLabel = new JLabel("Size:");
		fontSizeComboBox = new JComboBox(getFontSizes());
		fontSizeComboBox.setSelectedItem(Integer.valueOf(defaultFont.getSize()));
		
		fontChooserComboBox.addActionListener(this);
		fontSizeComboBox.addActionListener(this);
	}
	
	private Integer[] getFontSizes() {
		List<Integer> fontSizes = new ArrayList<Integer>();
		fontSizes.add(10);
		fontSizes.add(11);
		fontSizes.add(12);
		fontSizes.add(14);
		fontSizes.add(16);
		Collections.sort(fontSizes);
		return fontSizes.toArray(new Integer[0]);
	}

	private void initLayout() {
		this.removeAll();
		this.setLayout(layout);
		
		layout.setVerticalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(fontChooserLabel)
						.addComponent(fontChooserComboBox, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(fontSizeLabel)
						.addComponent(fontSizeComboBox, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
		);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()				
						.addComponent(fontChooserLabel)
						.addComponent(fontChooserComboBox, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addPreferredGap(RELATED)
				.addGroup(layout.createParallelGroup()
					.addComponent(fontSizeLabel)
					.addComponent(fontSizeComboBox, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
		);
	}

	private static String [] getFontFamilyNames() {
		List<String> fontNames = new ArrayList<String>();
		fontNames.add(Font.DIALOG);
		fontNames.add(Font.DIALOG_INPUT);
		fontNames.add(Font.MONOSPACED);
		fontNames.add(Font.SERIF);
		fontNames.add(Font.SANS_SERIF);
		Collections.sort(fontNames);
		return fontNames.toArray(new String[0]);
	}
	
	private Font getCurrentFont() {
		String name = (String) fontChooserComboBox.getSelectedItem(); 
		int size = (Integer) fontSizeComboBox.getSelectedItem();
		new Font(Font.MONOSPACED, Font.PLAIN, 13);
		
		return new Font(name, Font.PLAIN, size);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		fontChangedListener.fontChanged(getCurrentFont());
	}
	
	/** Callback to call when the user has changed any properties */
	public interface FontChangedLister {
		void fontChanged(Font newFont);
	}
	
}

