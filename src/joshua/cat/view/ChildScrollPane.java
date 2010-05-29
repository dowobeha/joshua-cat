package joshua.cat.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import joshua.cat.model.SentencePanelModel;
import joshua.util.Colors;


@SuppressWarnings("serial")
public class ChildScrollPane extends JScrollPane {
	
	private static final Logger logger =
		Logger.getLogger(ChildScrollPane.class.getName());
	
	private final SentencePanelModel model;
	private final ComboBoxMouseListener mouseListener;
	
	public ChildScrollPane(SentencePanelModel model) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.model = model;
		this.mouseListener = new ComboBoxMouseListener();
		setViewportView(new ChildPanel(model.getWords()));
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
				label.setOpaque(true);
				label.setBackground(Colors.LIGHT_RED);
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
		
//		private final int row;
//		private final int cell;
		
		private ComboBox(int row, int cell) {
			super(model.getComboBoxModel(row, cell));
			setEditable(false);
			setEnabled(false);
//			this.row = row;
//			this.cell = cell;
			
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
//			if (comboBoxEvent(e)) {
//				e.getComponent().getParent().setEnabled(true);
//			}
		}

		public void mouseExited(MouseEvent e) {
			log("mouseExited",e);
//			if (comboBoxEvent(e)) {
//				e.getComponent().getParent().setEnabled(false);
//			}
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
