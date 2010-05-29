package joshua.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

@SuppressWarnings("serial")
public class ScrollablePanel extends JPanel implements Scrollable {
	
	private static final Logger logger = 
		Logger.getLogger(ScrollablePanel.class.getName());
	
	private final JScrollPane scrollPane;
	private final boolean disableHorizontalScrolling;
	private final boolean disableVerticalScrolling;
	private final int componentsPerBlock;
	
	public ScrollablePanel(JScrollPane scrollPane, 
			boolean disableHorizontalScrolling, 
			boolean disableVerticalScrolling,
			int componentsPerBlock) {
		
		this.scrollPane = scrollPane;
		this.disableHorizontalScrolling = disableHorizontalScrolling;
		this.disableVerticalScrolling = disableVerticalScrolling;
		this.componentsPerBlock = componentsPerBlock;
		
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		// Start with a sensible default
		int scrollIncrement = 241;
		
		int s = getComponent(0).getHeight() + getComponent(1).getHeight() + getComponent(2).getHeight() + getComponent(3).getHeight();
		System.out.println(s);
		Component firstChild = this.getComponent(0);
		
		if (firstChild != null) {
			scrollIncrement = this.getComponent(0).getHeight();

			
			int y = (int) scrollPane.getViewport().getViewPosition().getY();
			if (logger.isLoggable(Level.FINEST)) logger.finest("Position of view is " + y);

			if (direction > 0) {
				// Scrolling down
				
				Component firstVisible = null;
				for (Component c : getComponents()) {
					Rectangle bounds = c.getBounds();

					if (firstVisible==null) {
						if (bounds.y >= y) {
							// This is the first component that's visible,
							//    and its top is not hidden:
							//    No need for extra scrolling
							scrollIncrement = bounds.height;
							if (logger.isLoggable(Level.FINEST)) logger.finest("Breaking:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
							break;
						} else if (bounds.y + bounds.height > y ){
							// This is the first component that's visible,
							//    but it is only partly visible (its top is hidden):
							//    We'll need a bit of extra scrolling
							//    to hide the partially visible component
							firstVisible = c;
							scrollIncrement = (bounds.y + bounds.height - y);
							if (logger.isLoggable(Level.FINEST)) logger.finest("Partially visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						}
					} else {
						scrollIncrement += bounds.height;
						if (logger.isLoggable(Level.FINEST)) logger.finest("Next visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						break;
					}
				}
				
			} else {
				
				// Scrolling up
				ArrayList<Component> list = new ArrayList<Component>(Arrays.asList(getComponents()));
				Collections.reverse(list);
				
				Component lastVisible = null;
				
				for (Component c : list) {
					Rectangle bounds = c.getBounds();

					if (lastVisible==null) {
						if (bounds.y == y) {
							// This is the uppermost component that's visible,
							//    and its top is not hidden:
							//    No need for extra scrolling
							lastVisible = c;
							scrollIncrement = 0;
							if (logger.isLoggable(Level.INFO)) logger.info("Breaking:\tBounds is " + bounds);
						} else if (bounds.y < y ){
							// This is the uppermost component that's visible,
							//    but it is only partly visible (its top is hidden):
							//    We'll need a bit of extra scrolling
							//    to hide the partially visible component
							lastVisible = c;
							scrollIncrement = (bounds.height - (y - bounds.y));
							if (logger.isLoggable(Level.INFO)) logger.info("Partially visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						}
					} else {
						scrollIncrement += bounds.height;
						if (logger.isLoggable(Level.INFO)) logger.info("Next visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						break;
					}
				}
			}
		}
		
		return scrollIncrement;
		
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return disableVerticalScrolling;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return disableHorizontalScrolling;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		// Start with a sensible default
		int scrollIncrement = 16;
		
		Component firstChild = this.getComponent(0);
		if (firstChild != null) {
			scrollIncrement = this.getComponent(0).getHeight();

			
			int y = (int) scrollPane.getViewport().getViewPosition().getY();
			if (logger.isLoggable(Level.FINEST)) logger.finest("Position of view is " + y);

			if (direction > 0) {
				// Scrolling down
				
				Component firstVisible = null;
				for (Component c : getComponents()) {
					Rectangle bounds = c.getBounds();

					if (firstVisible==null) {
						if (bounds.y >= y) {
							// This is the first component that's visible,
							//    and its top is not hidden:
							//    No need for extra scrolling
							scrollIncrement = bounds.height;
							if (logger.isLoggable(Level.FINEST)) logger.finest("Breaking:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
							break;
						} else if (bounds.y + bounds.height > y ){
							// This is the first component that's visible,
							//    but it is only partly visible (its top is hidden):
							//    We'll need a bit of extra scrolling
							//    to hide the partially visible component
							firstVisible = c;
							scrollIncrement = (bounds.y + bounds.height - y);
							if (logger.isLoggable(Level.FINEST)) logger.finest("Partially visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						}
					} else {
						scrollIncrement += bounds.height;
						if (logger.isLoggable(Level.FINEST)) logger.finest("Next visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						break;
					}
				}
				
			} else {
				
				// Scrolling up
				ArrayList<Component> list = new ArrayList<Component>(Arrays.asList(getComponents()));
				Collections.reverse(list);
				
				Component lastVisible = null;
				
				for (Component c : list) {
					Rectangle bounds = c.getBounds();

					if (lastVisible==null) {
						if (bounds.y == y) {
							// This is the uppermost component that's visible,
							//    and its top is not hidden:
							//    No need for extra scrolling
							lastVisible = c;
							scrollIncrement = 0;
							if (logger.isLoggable(Level.INFO)) logger.info("Breaking:\tBounds is " + bounds);
						} else if (bounds.y < y ){
							// This is the uppermost component that's visible,
							//    but it is only partly visible (its top is hidden):
							//    We'll need a bit of extra scrolling
							//    to hide the partially visible component
							lastVisible = c;
							scrollIncrement = (bounds.height - (y - bounds.y));
							if (logger.isLoggable(Level.INFO)) logger.info("Partially visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						}
					} else {
						scrollIncrement += bounds.height;
						if (logger.isLoggable(Level.INFO)) logger.info("Next visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
						break;
					}
				}
			}
		}
		
		return scrollIncrement;
		
	}

}
