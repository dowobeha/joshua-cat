package net.dowobeha.prefs;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.KeyStroke;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class PreferencesModel  {

	private final Preferences preferences;
	private final SetMultimap<String,Runnable> actionMap;
	
	private final Map<String,String> defaults;
	private final SetMultimap<String,String> groups;
	
	public PreferencesModel(Class<?> c) {
		this.preferences = Preferences.userNodeForPackage(c);
		this.actionMap = HashMultimap.create();
		this.defaults = new HashMap<String,String>();
		this.groups = HashMultimap.create();
	}
	
	public void setGroup(String preferenceName, String groupName) {
		groups.put(groupName,preferenceName);
	}
	
	public Set<String> getGroups() {
		return groups.keySet();
	}
	
	public Set<String> getPreferenceNames(String group) {
		return groups.get(group);
	}
	
	public KeyStroke getDefaultKeystroke(String preferenceName) {
		return KeyStroke.getKeyStroke(defaults.get(preferenceName));
	}
	
	public void setDefaultKeystroke(String preferenceName, KeyStroke defaultKeystroke) {
		defaults.put(preferenceName, defaultKeystroke.toString());
	}
	
	public void setKeystroke(String preferenceName, KeyStroke keystroke) {
		preferences.put(preferenceName, keystroke.toString());
		for (Runnable action : actionMap.get(preferenceName)) {
			action.run();
		}
	}
	
	public KeyStroke getKeystroke(String preferenceName) {
		return KeyStroke.getKeyStroke(preferences.get(preferenceName, defaults.get(preferenceName)));
	}
	
	public void register(Runnable action, Collection<String> preferenceNames) {
		for (String preferenceName : preferenceNames) {
			actionMap.put(preferenceName, action);
		}
		action.run();
	}
	
	public void register(Runnable action, String... preferenceNames) {
		register(action,Arrays.asList(preferenceNames));
	}

}
