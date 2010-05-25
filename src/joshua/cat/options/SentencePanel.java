package joshua.cat.options;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class SentencePanel extends JPanel {

	private static final Logger logger =
		Logger.getLogger(SentencePanel.class.getName());
	
	private final int displayWidth;
	
	private final SentencePanelModel model;
	
	final ComboBoxMouseListener mouseListener;

	public SentencePanel(SentencePanelModel model) {
		this.mouseListener = new ComboBoxMouseListener();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.displayWidth = getMaxDisplayWidth();
		
		this.model = model;
		
		
		ChildScrollPane panel = new ChildScrollPane(model.getWords());
		int totalWidth = (int) panel.getPreferredSize().getWidth();
		int extra=displayWidth*2/3;
		List<ChildScrollPane> panels = new ArrayList<ChildScrollPane>();
		panels.add(panel);
		this.add(panel);
		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
			panel = new ChildScrollPane(model.getWords());
			panels.add(panel);
			this.add(panel);
		}

		Dimension size = new Dimension((int) displayWidth, (int) this.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);

		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
			panels.get(x/displayWidth).getViewport().setViewPosition(new Point(x,0));
		}
		

	}
	
	protected int getMaxDisplayWidth() {
		return (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getWidth();
	}
	
	
	private class ChildScrollPane extends JScrollPane {
		
		private ChildScrollPane(String[] args) {
			super(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			setViewportView(new ChildPanel(args));
		}
		
	}
	
	private class ChildPanel extends JPanel {

		final JComboBox[][] comboBoxes;
		
		private ChildPanel(String[] args) {
			super(new GridBagLayout());
			addMouseListener(mouseListener);
			
			for (int i=0; i<args.length*2; i++) {
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = 0;
				c.gridheight = 1;
				c.gridwidth = 1;
				c.weightx = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				JLabel label = new JLabel("");
				this.add(label,c);
			}

			// Add words
			for (int i=0; i<args.length; i+=1) {
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i*2;
				c.gridy = 1;
				c.gridheight = 1;
				c.gridwidth = 2;
				c.weightx = 1;
				c.fill = GridBagConstraints.HORIZONTAL;

				JLabel label = new JLabel(args[i], JLabel.CENTER);
				this.add(label,c);
				this.doLayout();
			}
			
			
			{
				int numRows = model.getNumRows();
				
				comboBoxes = new JComboBox[numRows][];

				// Add rows to chart
				for (int row=0; row<numRows; row+=1) {
					
					int numCells=args.length-row;
					comboBoxes[row] = new JComboBox[numCells];
					
					// Add cells to chart
					for (int cell=0; cell<numCells; cell+=1) {

						GridBagConstraints c = new GridBagConstraints();
						c.gridx = row + cell*2;
						c.gridy = 2 + row;
						c.gridheight = 1;
						c.gridwidth = 2;
						c.weightx = 1;
						c.fill = GridBagConstraints.HORIZONTAL;

						
						comboBoxes[row][cell] = new ComboBox(row,cell);
						this.add(comboBoxes[row][cell],c);
						
						this.doLayout();
					}

				}

			}
			
		}

	}
	
	private class ComboBox extends JComboBox {
		
		private final int row;
		private final int cell;
		
		private ComboBox(int row, int cell) {
			super(model.getComboBoxModel(row, cell));
			setEditable(false);
			setEnabled(false);
			this.row = row;
			this.cell = cell;
			
			for (Component component : getComponents()) {
				component.addMouseListener(mouseListener);
			}
		}
	}
	
	
	private class ComboBoxMouseListener implements MouseListener {

		private boolean comboBoxEvent(MouseEvent e) {
			return e.getComponent().getParent() instanceof JComboBox;
		}
		
		public void mousePressed(MouseEvent e) {
			log("mousePressed",e);
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(true);
				e.getComponent().getParent();
			}
		}

		public void mouseReleased(MouseEvent e) {
			log("mouseReleased",e);
		}

		public void mouseEntered(MouseEvent e) {
			log("mouseEntered",e);
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			log("mouseExited",e);
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(false);
			}
		}

		public void mouseClicked(MouseEvent e) {
			log("mouseClicked",e);
		}

		void log(String eventDescription, MouseEvent e) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine(eventDescription + " detected on "
					+ e.getComponent().getClass().getName()
					+ ".");
			}
		}

	}
}


