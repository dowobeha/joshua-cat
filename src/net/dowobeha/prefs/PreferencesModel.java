package net.dowobeha.prefs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

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
	private final SetMultimap<PreferenceKey, Runnable> actionMap;

	/**
	 * Map from group to preference
	 */
	private final SetMultimap<GroupKey, PreferenceKey> groups;

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
		this.groups = HashMultimap.create();
	}

	
	/**
	 * Gets the names of all groups.
	 * 
	 * @return Set of names of all groups
	 */
	public Set<GroupKey> getGroups() {
		return groups.keySet();
	}

	/**
	 * Gets the names of all preferences in the specified group.
	 * 
	 * @param group
	 *            Name of group
	 * @return Names of all preferences in the specified group
	 */
	public Set<PreferenceKey> getPreferencesInGroup(GroupKey group) {
		return groups.get(group);
	}


	public void setValue(PreferenceKey preferenceKey, String value) {
		preferences.put(preferenceKey.toString(), value);
		for (Runnable action : actionMap.get(preferenceKey)) {
			action.run();
		}
	}

	public String getValue(PreferenceKey preferenceKey) {
		return preferences.get(preferenceKey.toString(), preferenceKey.getDefaultValue());
	}

	public void register(PreferenceKey preferenceKey, Runnable action) {
		actionMap.put(preferenceKey, action);
		groups.put(preferenceKey.getGroup(), preferenceKey);
	}

}
