package joshua.cat.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import joshua.ui.StartupWindow;

import net.dowobeha.prefs.PreferencesView;
import net.dowobeha.util.OperatingSystem;

@SuppressWarnings("serial")
public class TranslateMenu extends JMenuBar {

	public TranslateMenu(final TranslateWindow parent) {
		
		OperatingSystem.defineMacAboutMenu(new Runnable(){

			@Override
			public void run() {
				final StartupWindow splashScreen = parent.getSplashScreen();
				splashScreen.setVisible(true);
				
				splashScreen.addMouseListener(new MouseAdapter(){
					@Override
					public void mouseClicked(MouseEvent e) {
						splashScreen.setVisible(false);
					}
				});
				
			}
			
		});
		
		OperatingSystem.defineMacQuitMenu(new Runnable(){

			@Override
			public void run() {
				System.exit(0);
			}
			
		});
		
		OperatingSystem.defineMacPreferencesMenu(new Runnable(){
			
			final PreferencesView preferencesView = 
				new PreferencesView(parent.getPreferencesModel());
			
			@Override
			public void run() {
				preferencesView.setVisible(true);
			}
		});
		
		JMenu file = new JMenu("File");
		add(file);
		

	}
	
}
