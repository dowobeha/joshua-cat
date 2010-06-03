package net.dowobeha.util;

import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

@SuppressWarnings("serial")
public class NaturalNumberFormatter extends AbstractFormatter {

	@Override
	public Object stringToValue(String text) throws ParseException {
		try {
			int i = Integer.parseInt(text);
			if (i > 0) {
				return i;
			} else {
				throw new ParseException("Value is " + i + " but natural numbers must be integers greater than zero",0);
			}
		} catch (NumberFormatException e) {
			throw new ParseException(e.getLocalizedMessage(),0);
		}
	}

	
	@Override
	public String valueToString(Object value) throws ParseException {
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Integer) {
			return value.toString();
		} else {
			throw new ParseException("Unabled to parse \"" + value.toString() + "\" as Integer",0);
		}
	}

	
}
