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
	private final Map<PreferenceKey,String> lastValidValues;
	
	/**
	 * Constructs a new view for the provided model.
	 * 
	 * @param model Preferences model
	 */
	public PreferencesView(final PreferencesModel model) {
		this.lastValidValues = new HashMap<PreferenceKey,String>();
		
		JTabbedPane tabs = new JTabbedPane();
		
		for (GroupKey group : model.getGroups()) {
			
			JPanel panel = new JPanel(new GridLayout(0,2));
			
			for (final PreferenceKey preference : model.getPreferencesInGroup(group)) {

				final AbstractFormatter formatter = preference.getFormatter();//model.getFormatter(preferenceName);
				
				AbstractFormatterFactory formatterFactory = new AbstractFormatterFactory() {
					@Override
					public AbstractFormatter getFormatter(JFormattedTextField tf) {
						return formatter;
					}

				};
				String defaultValue = preference.getDefaultValue();//model.getDefaultValue(preferenceName);
				final JFormattedTextField textField = 
					new JFormattedTextField(formatterFactory,defaultValue); 
		
				String lastValidValue = model.getValue(preference);
				this.lastValidValues.put(preference, lastValidValue);
				textField.setText(lastValidValue);
				
				if (! textField.isEditValid()) {
					textField.setValue(defaultValue);
					this.lastValidValues.put(preference, defaultValue);
				}
				
//				textField.addPropertyChangeListener("textFormatter",new PropertyChangeListener(){
				textField.addPropertyChangeListener(new PropertyChangeListener(){

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String text = textField.getText();
						if (! lastValidValues.get(preference).equals(text)) {
							if (text == null || text.trim().isEmpty()) {
								textField.setText(preference.getDefaultValue());
							} else {
								if (textField.isEditValid()) {
									model.setValue(preference, text);
									lastValidValues.put(preference, text);
								} else {
									textField.setText(lastValidValues.get(preference));
								}
							}
						}
					}		
				});
						
				panel.add(new JLabel(preference.toString()));
				panel.add(textField);
			}
			
			tabs.addTab(group.toString(), panel);
		}
		
		this.add(tabs);
		this.pack();
		this.setResizable(false);
	}
	
}
