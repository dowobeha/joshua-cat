package net.dowobeha.prefs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JFormattedTextField.AbstractFormatter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class PreferencesModel {

	/**
	 * Data store of preference key-value pairs
	 */
	private final Preferences preferences;

	/**
	 * Map from preference name to runnable actions that should be run whenever
	 * the associated preference value changes.
	 */
	private final SetMultimap<String, Runnable> actionMap;

	/**
	 * Map from preference name to default value
	 */
	private final Map<String, String> defaults;

	/**
	 * Map from group name to preference name
	 */
	private final SetMultimap<String, String> groups;

	/**
	 * Map from preference name to text field formatter
	 */
	private final Map<String,AbstractFormatter> formatters;
	
	/**
	 * Map from Java class to preference model
	 */
	private static Map<Class<?>, PreferencesModel> classMap = new HashMap<Class<?>, PreferencesModel>();

	/**
	 * Gets a preference model for the given class.
	 * <p>
	 * This factory method will ensure that only one PreferenceModel instance
	 * will be created for any given class.
	 * 
	 * Calling this method more than once using the same class parameter will
	 * return the same object.
	 * 
	 * @param c
	 *            Java class
	 * @return Preference model for the given class
	 */
	public static PreferencesModel get(Class<?> c) {
		if (!classMap.containsKey(c)) {
			classMap.put(c, new PreferencesModel(c));
		}

		return classMap.get(c);
	}

	/**
	 * Constructs a new preference model for the given class.
	 * 
	 * @param c
	 *            Java class
	 */
	private PreferencesModel(Class<?> c) {
		this.preferences = Preferences.userNodeForPackage(c);
		this.actionMap = HashMultimap.create();
		this.defaults = new HashMap<String, String>();
		this.groups = HashMultimap.create();
		this.formatters = new HashMap<String,AbstractFormatter>();
	}

	/**
	 * Gets the text field formatter for the given preference.
	 * 
	 * @param preference Name of preference
	 * @return Text field formatter for the given preference
	 */
	public AbstractFormatter getFormatter(String preference) {
		return formatters.get(preference);
	}
	
	/**
	 * Gets the names of all groups.
	 * 
	 * @return Set of names of all groups
	 */
	public Set<String> getGroups() {
		return groups.keySet();
	}

	/**
	 * Gets the names of all preferences in the specified group.
	 * 
	 * @param group
	 *            Name of group
	 * @return Names of all preferences in the specified group
	 */
	public Set<String> getPreferencesInGroup(String group) {
		return groups.get(group);
	}

	public String getDefaultValue(String preference) {
		return defaults.get(preference);
	}

	public void setDefaultValue(String preference, String defaultValue) {
		defaults.put(preference, defaultValue);
	}

	public void setValue(String preference, String value) {
		preferences.put(preference, value);
		for (Runnable action : actionMap.get(preference)) {
			action.run();
		}
	}

	public String getValue(String preference) {
		return preferences.get(preference, defaults.get(preference));
	}

	public void register(Runnable action, AbstractFormatter formatter, String group, String preference) {
		actionMap.put(preference, action);
		groups.put(group, preference);
		formatters.put(preference, formatter);
	}

	public static final String GROUP_KEYBOARD_SHORTCUTS = "Keyboard Shortcuts";
}
