package joshua.cat.view;

import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
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
		final Runnable quitAction = new Runnable(){

			@Override
			public void run() {
				System.exit(0);
			}
			
		};
		OperatingSystem.defineMacQuitMenu(quitAction);
		
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
		
		JMenuItem open = new JMenuItem("Open...");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,controlMask));
		open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		file.add(open);
		
		JMenuItem saveAs = new JMenuItem("Save As...");
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,controlMask));
		saveAs.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (OperatingSystem.isMac()) {
					FileDialog fileDialog = new FileDialog(parent,"Save As...",FileDialog.SAVE);
					fileDialog.setVisible(true);
					String fileName = fileDialog.getFile();
					String dirName = fileDialog.getDirectory();
					if (fileName != null) {
						parent.getPrimaryPanel().saveTargetText(new File(dirName,fileName));
					}
				} else {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Save As...");
					if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						if (file != null) {
							parent.getPrimaryPanel().saveTargetText(file);
						}
					}
				}
			}
			
		});
		file.add(saveAs);
		
		if (! OperatingSystem.isMac()) {
			JMenuItem quit = new JMenuItem("Quit");
			quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,controlMask));
			quit.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					quitAction.run();
				}
				
			});
			file.add(quit);
		}
		
		JMenu edit = new JMenu("Edit");
		add(edit);
				
		JMenuItem cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,controlMask));
		cut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					TextCompletionArea inFocus = 
						parent.getPrimaryPanel().getFocusedTextCompletionArea();

					inFocus.cut();
				} catch (NullPointerException ex) {
					// This space intentionally left blank
				}
			}		
		});
		edit.add(cut);
		
		JMenuItem copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,controlMask));
		copy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					TextCompletionArea inFocus = 
						parent.getPrimaryPanel().getFocusedTextCompletionArea();

					inFocus.copy();
				} catch (NullPointerException ex) {
					// This space intentionally left blank
				}
			}		
		});
		edit.add(copy);
		
		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,controlMask));
		paste.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					TextCompletionArea inFocus = 
						parent.getPrimaryPanel().getFocusedTextCompletionArea();

					inFocus.paste();
				} catch (NullPointerException ex) {
					// This space intentionally left blank
				}
			}
		});
		edit.add(paste);
		
		
		
	}
	
}
