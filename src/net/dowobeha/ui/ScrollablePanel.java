package net.dowobeha.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.google.common.base.Preconditions;


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
		if (orientation==SwingConstants.VERTICAL) {
			if (direction > 0) {
				// Scrolling down
				return getScrollDownIncrement(visibleRect,componentsPerBlock);
			} else {
				// Scrolling up
				int value = getScrollUpIncrement(visibleRect,componentsPerBlock);
				System.out.println("Scrolling up by " + value);
				return value;
			}
		} else {
			if (direction > 0) {
				// Scrolling right
				
			} else {
				// Scrolling left
				
			}
		}
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return disableVerticalScrolling;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return disableHorizontalScrolling;
	}

	protected Iterable<Component> getScrollableComponents() {
		return new ArrayList<Component>(Arrays.asList(getComponents()));
	}

	
	/**
	 * 
	 * @param visibleRect The view area visible within the viewport
	 * @param numComponents The number of (partially or completely) visible components
	 *                      which should be scrolled out of view 
	 * @return
	 */
	private int getScrollDownIncrement(Rectangle visibleRect, int numComponents) {
		Preconditions.checkArgument(numComponents >= 0);
		
		int y = (int) scrollPane.getViewport().getViewPosition().getY();
		if (logger.isLoggable(Level.FINEST)) logger.finest("Position of view is " + y);

		int scrollIncrement = 0;
		int index = 0;
		int componentsMeasured = 0;
		int componentsToMeasure = 0;
		
		for (Component c : getScrollableComponents()) {
			Rectangle bounds = c.getBounds();

			if (componentsMeasured==0) {
				if (bounds.y >= y) {
					// This is the first component that's visible,
					//    and its top is not hidden:
					//    No need for extra scrolling
					scrollIncrement = bounds.height;
					if (logger.isLoggable(Level.FINEST)) logger.finest("Breaking:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
					componentsToMeasure = getNumComponentsToScrollDown(numComponents, index);
					componentsMeasured = 1;
				} else if (bounds.y + bounds.height > y ){
					// This is the first component that's visible,
					//    but it is only partly visible (its top is hidden):
					//    We'll need a bit of extra scrolling
					//    to hide the partially visible component
					scrollIncrement = (bounds.y + bounds.height - y);
					if (logger.isLoggable(Level.FINEST)) logger.finest("Partially visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
					componentsToMeasure = getNumComponentsToScrollDown(numComponents, index);
					componentsMeasured = 1;
				}
			} else if (componentsToMeasure > componentsMeasured ){
				scrollIncrement += bounds.height;
				if (logger.isLoggable(Level.FINEST)) logger.finest("Next visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
				componentsMeasured += 1;
			} else {
				break;
			}
						
			index += 1;
		}

		return scrollIncrement;

	}
	
	/**
	 * 
	 * @param visibleRect The view area visible within the viewport
	 * @param numComponents The number of (partially or completely) visible components
	 *                      which should be scrolled out of view 
	 * @return
	 */
	private int getScrollUpIncrement(Rectangle visibleRect, int numComponents) {
		Preconditions.checkArgument(numComponents >= 0);
		
		int y = (int) scrollPane.getViewport().getViewPosition().getY();
		if (logger.isLoggable(Level.FINEST)) logger.finest("Position of view is " + y);

		int scrollIncrement = 0;
		
		int index = 0;
		int componentsToMeasure = 0;
		int partialMeasured = 0;
		Stack<Component> stack = new Stack<Component>();
		
		for (Component c : getScrollableComponents()) {
			Rectangle bounds = c.getBounds();

			if (bounds.y >= y) {
				// This is the first component that's visible,
				//    and its top is not hidden:
				//    No need for extra scrolling
				scrollIncrement = 0;
				if (logger.isLoggable(Level.FINEST)) logger.finest("Breaking:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
				componentsToMeasure = getNumComponentsToScrollUp(numComponents, index);
				break;
			} else if (bounds.y < y  && bounds.y + bounds.height > y){
				// This is the first component that's visible,
				//    but it is only partly visible (its top is hidden):
				//    We'll need a bit of extra scrolling
				//    to hide the partially visible component
				scrollIncrement = (y - bounds.y);
				if (logger.isLoggable(Level.FINEST)) logger.finest("Partially visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
				componentsToMeasure = getNumComponentsToScrollUp(numComponents, index+1);
				partialMeasured += 1;
				break;
			} else {
				stack.push(c);
			}
			
			index += 1;
		}
			
		for (int componentsMeasured=partialMeasured; 
				componentsMeasured<componentsToMeasure && !stack.isEmpty(); 
				componentsMeasured+=1) {
			
			Component c = stack.pop();
			Rectangle bounds = c.getBounds();
			scrollIncrement += bounds.height;
			if (logger.isLoggable(Level.FINEST)) logger.finest("Next visible:\tBounds is " + bounds+"\tscrollIncrement=="+scrollIncrement);
		}

		return scrollIncrement;

	}

	static int getNumComponentsToScrollDown(int numComponents, int firstVisibleIndex) {
		return numComponents - (firstVisibleIndex % numComponents);
	}
	
	static int getNumComponentsToScrollUp(int numComponents, int firstVisibleIndex) {
		int remainder = firstVisibleIndex % numComponents;
		
		if (remainder==0) {
			return numComponents;
		} else {
			return remainder;
		}
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		
		if (orientation==SwingConstants.VERTICAL) {
			if (direction > 0) {
				// Scrolling down
				return getScrollDownIncrement(visibleRect,1);
			} else {
				// Scrolling up
				int value = getScrollUpIncrement(visibleRect,1);
				System.out.println("Scrolling up by " + value);
				return value;
			}
		} else {
			if (direction > 0) {
				// Scrolling right
				
			} else {
				// Scrolling left
				
			}
		}
		
		
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
