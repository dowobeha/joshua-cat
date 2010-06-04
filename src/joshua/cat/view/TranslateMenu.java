package joshua.cat.view;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import joshua.ui.StartupWindow;

import net.dowobeha.prefs.PreferencesModel;
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
						
			@Override
			public void run() {
				PreferencesView preferencesView = 
					new PreferencesView(PreferencesModel.get(TranslateWindow.class.getClass()));
				
				preferencesView.setVisible(true);
			}
		});
		
		final int controlMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		JMenu file = new JMenu("File");
		add(file);
		
		JMenu edit = new JMenu("Edit");
		add(file);
		
		JMenuItem cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,controlMask));
		cut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				TextCompletionArea inFocus = 
					parent.getPrimaryPanel().getFocusedTextCompletionArea();
				
				inFocus.cut();
			}
			
		});
		
		JMenuItem copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,controlMask));
		copy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				TextCompletionArea inFocus = 
					parent.getPrimaryPanel().getFocusedTextCompletionArea();
				
				inFocus.copy();
			}
			
		});
		
		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,controlMask));
		paste.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				TextCompletionArea inFocus = 
					parent.getPrimaryPanel().getFocusedTextCompletionArea();
				
				inFocus.paste();
			}
			
		});
		
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		
		add(edit);
		
	}
	
}
