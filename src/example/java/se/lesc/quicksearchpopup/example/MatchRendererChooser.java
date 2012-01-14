package se.lesc.quicksearchpopup.example;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import se.lesc.quicksearchpopup.renderer.BoldRenderer;
import se.lesc.quicksearchpopup.renderer.ColoredBackgroundRenderer;
import se.lesc.quicksearchpopup.renderer.MatchRenderer;
import se.lesc.quicksearchpopup.renderer.PlainRenderer;

/**
 * Panel to choose which match cell list renderer the popup should have to highlight matches
 */
@SuppressWarnings("serial")
public class MatchRendererChooser extends JPanel implements ActionListener {

	private GroupLayout layout = new GroupLayout(this);
	
	private RendererChooserListener matchRendererChooserListener;
	private JLabel label;
	private JComboBox rendererComboBox;

	public MatchRendererChooser(RendererChooserListener rendererChooserListener) {
		this.matchRendererChooserListener = rendererChooserListener;
		initComponents();
		initLayout();
	}

	private void initComponents() {
		label = new JLabel("List Cell Renderer:");
		rendererComboBox = new JComboBox(getRenderers());
		
		rendererComboBox.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				((JLabel) component).setText(value.getClass().getSimpleName());
				return component;
			}
		});

		rendererComboBox.setSelectedIndex(1);
		rendererComboBox.addActionListener(this);
	}


	private MatchRenderer[] getRenderers() {
		ArrayList<MatchRenderer> result = new ArrayList<MatchRenderer>();

		result.add(new BoldRenderer());
		result.add(new ColoredBackgroundRenderer());
		result.add(new PlainRenderer());
		
		MatchRenderer resultArray[] = result.toArray(new MatchRenderer[0]);
		return resultArray;
	}

	private void initLayout() {
		this.removeAll();
		this.setLayout(layout);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(label)
				.addComponent(rendererComboBox, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
		);

		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(label)
				.addComponent(rendererComboBox, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
		);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		matchRendererChooserListener.rendererChanged(getSelectedRenderer());
	}
	
	public MatchRenderer getSelectedRenderer() {
		return (MatchRenderer) rendererComboBox.getSelectedItem();
	}

	/** Callback to call when the user has changed any properties */
	public interface RendererChooserListener {
		void rendererChanged(MatchRenderer newRenderer);
	}
	
}
