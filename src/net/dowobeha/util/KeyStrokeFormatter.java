package net.dowobeha.util;

import java.text.ParseException;

import javax.swing.KeyStroke;
import javax.swing.JFormattedTextField.AbstractFormatter;

@SuppressWarnings("serial")
public class KeyStrokeFormatter extends AbstractFormatter {

	@Override
	public Object stringToValue(String text) throws ParseException {
		KeyStroke keyStroke = KeyStroke.getKeyStroke(text);
		if (keyStroke==null) {
			throw new ParseException("Unabled to parse \"" + text + "\" as KeyStroke",0);
		} else {
			return keyStroke;
		}
	}

	
	@Override
	public String valueToString(Object value) throws ParseException {
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof KeyStroke) {
			return value.toString();
		} else {
			throw new ParseException("Unabled to parse \"" + value.toString() + "\" as KeyStroke",0);
		}
	}

	
}
