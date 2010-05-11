package joshua.cat;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Scratch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Listener listener = new Listener();

		int spanLimit = 4;

		JPanel pane = new JPanel(new GridBagLayout());

		for (int i=0; i<args.length*2; i++) {
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			JLabel label = new JLabel("");
			//			label.setBorder(BorderFactory.createLineBorder(Color.black));
			pane.add(label,c);
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
			//			System.err.println(c.gridx);
			JLabel label = new JLabel(args[i], JLabel.CENTER);
			label.addMouseListener(listener);
			//			label.setBorder(BorderFactory.createLineBorder(Color.black));
			pane.add(label,c);
		}



		// Add rows to chart
		for (int row=0, max=Math.min(args.length, spanLimit); row<max; row+=1) {

			// Add cells to chart
			for (int numCells=args.length-row, cell=0; cell<numCells; cell+=1) {

				GridBagConstraints c = new GridBagConstraints();
				c.gridx = row + cell*2;
				c.gridy = 2 + row;
				c.gridheight = 1;
				c.gridwidth = 2;
				c.weightx = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				//				System.err.println(c.gridx);

				String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
				JComboBox comboBox = new JComboBox(petStrings);
				for (Component component : comboBox.getComponents()) {
					component.addMouseListener(listener);
				}
				//				comboBox.addMouseListener(listener);
				pane.add(comboBox,c);

				comboBox.setEnabled(false);
				//				JLabel label = new JLabel(" R" + row + ",C"+cell);
				//				label.setBorder(BorderFactory.createLineBorder(Color.black));
				//				pane.add(label,c);
			}

		}


		JFrame window = new JFrame();
		window.addMouseListener(listener);
		window.add(pane);
		window.pack();
		window.setVisible(true);

	}

	static class Listener implements MouseListener {

		private boolean comboBoxEvent(MouseEvent e) {
			return e.getComponent().getParent() instanceof JComboBox;
		}
		
		public void mousePressed(MouseEvent e) {
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(true);
				e.getComponent().getParent();
			}
		}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {
			if (comboBoxEvent(e)) {
//				e.getComponent().getParent().setEnabled(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(false);
			}
		}

		public void mouseClicked(MouseEvent e) {

		}

		void saySomething(String eventDescription, MouseEvent e) {
			System.err.println(eventDescription + " detected on "
					+ e.getComponent().getClass().getName()
					+ ".");
		}

	}

}
