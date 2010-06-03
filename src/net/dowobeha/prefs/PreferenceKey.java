package net.dowobeha.prefs;

import javax.swing.JFormattedTextField.AbstractFormatter;

public interface PreferenceKey {

	public GroupKey getGroup();
	public AbstractFormatter getFormatter();
	public String toString();
	public String getDefaultValue();
	
}
