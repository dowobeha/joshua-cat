package net.dowobeha.prefs;

import java.util.prefs.Preferences;

import javax.swing.KeyStroke;

public class PreferenceActions  {

	
	
	public PreferenceActions() {
		
	}
	
	public void registerDefault(Class<?> c, String preferenceName, KeyStroke defaultKeystroke) {
		Preferences.userNodeForPackage(c).put(preferenceName, defaultKeystroke.toString());
	}
	
	public void register(Class<?> c, Runnable action, String... preferenceNames) {
		
	}

}
