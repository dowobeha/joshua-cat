package joshua.cat.model;

import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.MaskFormatter;

import net.dowobeha.prefs.GroupKey;
import net.dowobeha.prefs.PreferenceKey;
import net.dowobeha.util.KeyStrokeFormatter;
import net.dowobeha.util.NaturalNumberFormatter;

public class Preferences {


	public enum Group implements GroupKey {
		KEYBOARD_SHORTCUTS("Keyboard Shortcuts"),
		OPTIONS("Options");

		private final String name;

		private Group(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}
	
	public enum Key implements PreferenceKey {
				
		FORWARD_FOCUS_TRAVERSAL(
				"Forward focus traversal", 
				Group.KEYBOARD_SHORTCUTS,
				KeyStrokeFormatter.getInstance(),
				"TAB"
		),
				
		BACKWARD_FOCUS_TRAVERSAL(
				"Backward focus traversal", 
				Group.KEYBOARD_SHORTCUTS,
				KeyStrokeFormatter.getInstance(),
				"shift TAB"
		),
		
		SCROLL_SENTENCE_PANEL_LEFT(
				"Scroll sentence panel left",
				Group.KEYBOARD_SHORTCUTS,
				KeyStrokeFormatter.getInstance(),
				"shift LEFT"
		),
		
		SCROLL_SENTENCE_PANEL_RIGHT(
				"Scroll sentence panel right",
				Group.KEYBOARD_SHORTCUTS,
				KeyStrokeFormatter.getInstance(),
				"shift RIGHT"
		),
		
		SCROLL_PAGE_DOWN(
				"Scroll page down",
				Group.KEYBOARD_SHORTCUTS,
				KeyStrokeFormatter.getInstance(),
				"PAGE_DOWN"
		),
		
		SCROLL_PAGE_UP(
				"Scroll page up",
				Group.KEYBOARD_SHORTCUTS,
				KeyStrokeFormatter.getInstance(),
				"PAGE_UP"
		),
		
		SENTENCE_PANEL_HORIZONTAL_SCROLL_FACTOR(
				"Sentence panel horizontal scroll factor",
				Group.OPTIONS,
				getFactorFormatter(),
				"1.0"
		),
		
		SPAN_LIMIT(
				"Span limit",
				Group.OPTIONS,
				NaturalNumberFormatter.getInstance(),
				"5"
		);
		
		private static final AbstractFormatter getFactorFormatter() {
			try {
				return new MaskFormatter("#.#");
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		private final String name;
		private final Group group;
		private final AbstractFormatter formatter;
		private final String defaultValue;
		
		private Key(String name, Group group, AbstractFormatter formatter, String defaultValue) {
			this.name = name;
			this.group = group;
			this.formatter = formatter;
			this.defaultValue = defaultValue;
		}
		
		public Group getGroup() {
			return this.group;
		}
		
		public AbstractFormatter getFormatter() {
			return this.formatter;
		}
		
		public String getDefaultValue() {
			return this.defaultValue;
		}
		
		public String toString() {
			return name;
		}
	}

}
