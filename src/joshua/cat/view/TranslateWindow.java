package joshua.cat.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import joshua.ui.StartupWindow;


@SuppressWarnings("serial")
public class TranslateWindow extends JFrame {

	private final StartupWindow splashScreen;
	
	public TranslateWindow() {
		
		BufferedImage image;
		try {                
			image = ImageIO.read(new File("splash.jpg"));
		} catch (IOException ex) {
			image = null;
		}
	       
		this.splashScreen = 
			new StartupWindow("Translate","Lane Schwartz","2010",image,Color.BLACK,5);
		
		this.splashScreen.setVisible(true);
		
		
		this.setJMenuBar(new TranslateMenu(this));
		
		
		
	}
	
	private PrimaryPanel primaryPanel;
	
	public void setContent(PrimaryPanel panel) {
		this.primaryPanel = panel;
		Container contentPane = this.getContentPane();
		contentPane.removeAll();
		contentPane.add(panel);
		this.pack();
		this.splashScreen.setVisible(false);
		this.setVisible(true);
	}
		
	public PrimaryPanel getPrimaryPanel() {
		return primaryPanel;
	}
	
	public StartupWindow getSplashScreen() {
		return this.splashScreen;
	}
	
}
