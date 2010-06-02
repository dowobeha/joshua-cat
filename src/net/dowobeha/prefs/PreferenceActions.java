package net.dowobeha.prefs;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.KeyStroke;

import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;

public class PreferenceActions  {

	private final Preferences preferences;
	private final Multimap<String,Runnable> actionMap;
	
	private final Map<String,String> defaults;
	
	public PreferenceActions(Class<?> c) {
		this.preferences = Preferences.userNodeForPackage(c);
		this.actionMap = HashMultimap.create();
		this.defaults = new HashMap<String,String>();
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
	
	public void register(Runnable action, String... preferenceNames) {
		for (String preferenceName : preferenceNames) {
			actionMap.put(preferenceName, action);
		}
		action.run();
	}

}
