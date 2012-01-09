package se.lesc.quicksearchpopup;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class QuickSearchPopup implements SelectionListener {

	private JTextComponent searchField;
	private PopupFactory factory;
	private String[] rows;
	private Searcher searcher;
	private Popup popup;
	private QuickSearchPopupContent quickSearcherList;
	private GlobalEventListener eventListener;
	private SelectionListener parentSelectionListener;

	public QuickSearchPopup(JTextComponent searchField, SelectionListener selectionListener) {
		this.searchField = searchField;
		this.parentSelectionListener = selectionListener;
		factory = PopupFactory.getSharedInstance();

		searcher = new WordSearcher(); //Default searcher

		createComponents();


		eventListener = new GlobalEventListener();

//		searchField.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				search(e);
//			}
//		});
		
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}
		});

		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKey(e);
			}
		});
	
	}
	
	
	protected void handleKey(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			hidePopup();
		} else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP
				|| keyCode == KeyEvent.VK_PAGE_DOWN || keyCode == KeyEvent.VK_PAGE_UP) {
			quickSearcherList.dispatchEvent(e);
		}
	}



	private void search() {

		String searchString = searchField.getText();
		if (searchString.isEmpty()) {
			hidePopup();
			return;
		}

		if (rows != null && rows.length > 0) {
    		ArrayList<String> matchedRows = new ArrayList<String>();
    		for (String row : rows) {
    			if (searcher.matches(searchString, row)) {
    				matchedRows.add(row);
    			}
    		}
    
    		quickSearcherList.setElements(searcher, searchString, matchedRows);
    		show();
		}
	}

	private void createComponents() {
		quickSearcherList = new QuickSearchPopupContent(searchField, searcher, this);
	}

	private Popup createPopup() {
		Point locationOnScreen = searchField.getLocationOnScreen();

		int x = locationOnScreen.x;
		int y = locationOnScreen.y + searchField.getHeight();

//		System.out.println(x + ", " + y);
		Popup popup = factory.getPopup(searchField, quickSearcherList, x, y);

		eventListener.registerForEvents();

		//		quickSearcherList.addFocusListener(new FocusListener() {
		//
		//			@Override
		//			public void focusLost(FocusEvent e) {
		//				System.out.println("focusLost");
		//			}
		//
		//			@Override
		//			public void focusGained(FocusEvent e) {
		//				System.out.println("focusGained");
		//			}
		//		});

		//		JPopupMenu popup = new JPopupMenu();
		//		popup.add(panel);
		//		popup.show(searchField, 0, 0);

		return popup;
	}

	private void show() {
		hidePopup();
		createPopup();
		popup = createPopup();
		popup.show();
	}

	private void hidePopup() {
		if (popup != null) {
			eventListener.unRegisterForEvents();
			popup.hide();

			popup = null;
//			System.out.println("hide");
		}
	}

	public void setSearchRows(String[] rows) {
		this.rows = rows;
	}

	private class GlobalEventListener implements AWTEventListener {

		public void registerForEvents() {
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			java.security.AccessController.doPrivileged(
					new java.security.PrivilegedAction<Void>() {
						public Void run() {
							toolkit.addAWTEventListener(GlobalEventListener.this,
									AWTEvent.MOUSE_EVENT_MASK |
									AWTEvent.MOUSE_MOTION_EVENT_MASK |
									AWTEvent.MOUSE_WHEEL_EVENT_MASK |
									AWTEvent.WINDOW_EVENT_MASK 
//									AWTEvent.FOCUS_EVENT_MASK,
//									AWTEvent.COMPONENT_EVENT_MASK
									);
//							toolkit.addAWTEventListener(GlobalEventListener.this, 0xffffffffffffffffL);							
							return null;
						}
					}
			);
		}

		public void unRegisterForEvents() {
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			java.security.AccessController.doPrivileged(
					new java.security.PrivilegedAction<Void>() {
						public Void run() {
							toolkit.removeAWTEventListener(GlobalEventListener.this);
							return null;
						}
					}
			);
		}

		@Override
		public void eventDispatched(AWTEvent event) {
//			System.out.println(event);
			
//			if (event instanceof ComponentEvent) {
//				ComponentEvent componentEvent = (ComponentEvent) event;
//				if (event.getID() == ComponentEvent.COMPONENT_MOVED) {
//					hidePopup();
//				}
//			}
			
			if (event instanceof WindowEvent) {
				WindowEvent windowEvent = (WindowEvent) event;
//				System.out.println(windowEvent);
				if (event.getID() == WindowEvent.WINDOW_LOST_FOCUS) {
					hidePopup();
				}
			}

			if (event instanceof MouseEvent) {
				handleMouseEvent((MouseEvent) event);
			}

//			if (event instanceof FocusEvent) {
//				FocusEvent focusEvent = (FocusEvent) event;
////				System.out.println(focusEvent);
//				hidePopup();
//			}
		}

		private void handleMouseEvent(MouseEvent event) {
			MouseEvent mouseEvent = (MouseEvent) event;
			Component source = mouseEvent.getComponent();
			switch (mouseEvent.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				if (isInPopup(source)) {
					return;
				}

//				System.out.println(event);
				hidePopup();
				break;

			case MouseEvent.MOUSE_RELEASED:
				// Do not forward event to MSM, let component handle it
				if (isInPopup(source)) {
					break;
				}
				break;
				//            case MouseEvent.MOUSE_DRAGGED:
					//            	if(!(src instanceof MenuElement)) {
				//            		// For the MOUSE_DRAGGED event the src is
				//            		// the Component in which mouse button was pressed. 
				//            		// If the src is in popupMenu, 
				//            		// do not forward event to MSM, let component handle it.
				//            		if (isInPopup(src)) {
				//            			break;
				//            		}
				//            	}
				//            	MenuSelectionManager.defaultManager().
				//            	processMouseEvent(me);
				//            	break;
			case MouseEvent.MOUSE_WHEEL:
				if (isInPopup(source)) {
					return;
				}
				hidePopup();
				break;
			}
		}

		boolean isInPopup(Component src) {
			for (Component c=src; c!=null; c=c.getParent()) {
				if (c instanceof Applet || c instanceof Window) {
					break;
				} else if (c instanceof QuickSearchPopupContent) {
					return true;
				}
			}
			return false;
		}

	}

	@Override
	public void rowSelected(String row) {
		parentSelectionListener.rowSelected(row);
		hidePopup();
		
	}
}