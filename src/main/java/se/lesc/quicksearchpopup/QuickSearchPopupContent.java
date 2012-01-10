package se.lesc.quicksearchpopup;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

//import static javax.swing.GroupLayout.DEFAULT_SIZE;

@SuppressWarnings("serial")
public class QuickSearchPopupContent extends JPanel {

	protected DefaultListModel listModel;
	protected JList list;
	protected JScrollPane listScrollPane;
	protected JButton addButton;
	protected JTextComponent searchField;
	protected Searcher searcher;
	private HighlightedLettersListCellRenderer cellRenderer;
	private SelectionListener selectionListener;
	private String searchString;

	public QuickSearchPopupContent(JTextComponent searchField, Searcher searcher, SelectionListener selectionListener) {
		this.searchField = searchField;
		this.searcher = searcher;
		this.selectionListener = selectionListener;
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					addSelected();
				}
			}
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addSelected();
				}
			}
		});
		
		list.setFont(searchField.getFont());
		cellRenderer = new HighlightedLettersListCellRenderer(list.getCellRenderer(), searcher);
		list.setCellRenderer(cellRenderer);
		listScrollPane = new JScrollPane(list);
		
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSelected();
			}
		});

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(listScrollPane)
				.addComponent(addButton)
		);
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(listScrollPane)
				.addComponent(addButton)				
		);
		
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
	}
	
	@Override
    protected void processEvent(AWTEvent e) {
    	super.processEvent(e);
    	
    	if (e instanceof KeyEvent) {
    		list.dispatchEvent(e);
    	}
    }
	
	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}
	
	protected void addSelected() {
		String selectedRow = (String) list.getSelectedValue();
		selectionListener.rowSelected(selectedRow);
	}

	public void setElements(Searcher searcher, String searchString, List<String> rows) {
		listModel.clear();
		for (String row : rows) {
			listModel.addElement(row);
		}
		
		this.searchString = searchString;
		cellRenderer.setSearchString(searchString);
		
//        //Increase speed of the list rendered (avoid size calculation on every cell)
//		if (rows.size() > 0) {
//			list.setPrototypeCellValue(rows.get(0));
//		}
	}

	/** Calculates the preferred sizes */
	public void calculateSizes() {
		
		cellRenderer.setQuickRenderMode(true);
		int maxCellWidth = 0; 
		int maxCellHeigth = 0; 
		
		for (int i = 0; i < listModel.getSize(); i++) {
			String row = (String) listModel.get(i);
	        Component c = cellRenderer.getListCellRendererComponent(list, row, i, false, false);
	        Dimension cellSize = c.getPreferredSize();
	        maxCellWidth = Math.max(cellSize.width, maxCellWidth);
	        maxCellHeigth = Math.max(cellSize.height, maxCellHeigth);
		}

        
        //Add an approximate value for the bold font
        maxCellWidth += (int) (searchString.length() * 0.3);
        
        list.setFixedCellHeight(maxCellHeigth);
        list.setFixedCellWidth(maxCellWidth);
        cellRenderer.setQuickRenderMode(false);
        
        listScrollPane.setMaximumSize(new Dimension(searchField.getSize().width, listScrollPane.getMaximumSize().height));
	}

}
