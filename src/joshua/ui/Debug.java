package joshua.ui;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Debug  {


	JFrame window = new JFrame();
	JLabel label = new JLabel("Hello, world");
	JPanel panel = new JPanel();

	JComboBox comboBox = new JComboBox(new String[]{ "Bird", "Cat", "Dog", "Rabbit", "Pig" });
	
	public Debug() {
		
		window.validate();
		
		window.add(panel);
		window.doLayout();
//		panel.add(label);
		panel.add(comboBox);

		panel.doLayout();
		panel.validate();
		window.validate();
		
		window.setVisible(true);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Debug debug = new Debug();
		
		debug.label = debug.label;
	}

}
