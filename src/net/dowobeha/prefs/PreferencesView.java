package net.dowobeha.prefs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class PreferencesView extends JFrame {

	private final PreferencesModel model;
	
	public PreferencesView(PreferencesModel model) {
		this.model = model;
		
		JTabbedPane tabs = new JTabbedPane();
		
		for (String group : model.getGroups()) {
			
			JPanel panel = new JPanel();
			
			for (String preferenceName : model.getPreferenceNames(group)) {
				panel.add(new PreferencePane(preferenceName));
			}
			
			tabs.addTab(group, panel);
		}
	}
	
	private final class PreferencePane extends JPanel implements ActionListener {
		
		private final String preferenceName;
		private final JTextField textField;
		private String lastValidValue;
		
		PreferencePane(String preferenceName) {
			this.preferenceName = preferenceName;
			
			this.textField = new JTextField();
			this.lastValidValue = model.getKeystroke(preferenceName).toString();
			textField.setText(lastValidValue);
			textField.addActionListener(this);
			
			add(new JLabel(preferenceName));
			add(textField);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = textField.getText();
			if (text != null && text.trim().isEmpty()) {
				KeyStroke keystroke = KeyStroke.getKeyStroke(text);
				if (keystroke != null) {
					model.setKeystroke(preferenceName, keystroke);
					lastValidValue = keystroke.toString();
				} else {
					textField.setText(lastValidValue);
				}
			} else {
				textField.setText(model.getDefaultKeystroke(preferenceName).toString());
			}
		}

	}
	
}
