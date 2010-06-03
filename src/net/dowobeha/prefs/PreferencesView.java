package net.dowobeha.prefs;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;

@SuppressWarnings("serial")
public class PreferencesView extends JFrame {

	/**
	 * Map from preference name to last valid preference value.
	 */
	private final Map<String,String> lastValidValues;
	
	/**
	 * Constructs a new view for the provided model.
	 * 
	 * @param model Preferences model
	 */
	public PreferencesView(final PreferencesModel model) {
		this.lastValidValues = new HashMap<String,String>();
		
		JTabbedPane tabs = new JTabbedPane();
		
		for (String group : model.getGroups()) {
			
			JPanel panel = new JPanel(new GridLayout(0,2));
			
			for (final String preferenceName : model.getPreferencesInGroup(group)) {

				final AbstractFormatter formatter = model.getFormatter(preferenceName);
				
				AbstractFormatterFactory formatterFactory = new AbstractFormatterFactory() {
					@Override
					public AbstractFormatter getFormatter(JFormattedTextField tf) {
						return formatter;
					}

				};
				String defaultValue = model.getDefaultValue(preferenceName);
				final JFormattedTextField textField = new JFormattedTextField(formatterFactory,defaultValue); 
		
				String lastValidValue = model.getValue(preferenceName);
				this.lastValidValues.put(preferenceName, lastValidValue);
				textField.setText(lastValidValue);
				
				if (! textField.isEditValid()) {
					textField.setValue(defaultValue);
					this.lastValidValues.put(preferenceName, defaultValue);
				}
				
				textField.addPropertyChangeListener("textFormatter",new PropertyChangeListener(){
					@Override
					public void propertyChange(PropertyChangeEvent evt) {

						String text = textField.getText();
						if (text == null || text.trim().isEmpty()) {
							textField.setText(model.getDefaultValue(preferenceName));
						} else {
							if (textField.isEditValid()) {
								model.setValue(preferenceName, text);
								lastValidValues.put(preferenceName, text);
							} else {
								textField.setText(lastValidValues.get(preferenceName));
							}
						}

					}		
				});
						
				panel.add(new JLabel(preferenceName));
				panel.add(textField);
			}
			
			tabs.addTab(group, panel);
		}
		
		this.add(tabs);
		this.pack();
		this.setResizable(false);
	}
	
}
