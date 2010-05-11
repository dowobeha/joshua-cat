package joshua.cat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Workspace extends JPanel implements ActionListener, KeyListener {

	protected JTextField textField;
	protected JTextArea textArea;
	private final static String newline = "\n";

	public Workspace() {
		super(new GridBagLayout());

		textField = new JTextField(20);
		textField.addActionListener(this);
		textField.addKeyListener(this);

		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		//Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
	}
	
	@Override	
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		String text = textField.getText();
		char typed = e.getKeyChar();
		if (typed != KeyEvent.CHAR_UNDEFINED) {
			textArea.setText((text + typed).toUpperCase());
		}
	}


	public void actionPerformed(ActionEvent evt) {
		String text = textField.getText();
		textArea.append(text + newline);
		textField.selectAll();

		//Make sure the new text is visible, even if there
		//was a selection in the text area.
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	private static void createAndShowGUI() {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Create and set up the window.
				JFrame frame = new JFrame("Workspace");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				//Add contents to the window.
				frame.add(new Workspace());

				//Display the window.
				frame.pack();
				frame.setVisible(true);
				
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		createAndShowGUI();

	}

}
