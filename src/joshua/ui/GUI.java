package joshua.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GUI {

	private GUI() {}
	
	public static void wrapAndShow(final JComponent panel) {
		wrapAndShow("",panel);
	}
	
	public static JFrame wrapAndShow(final String title, final JComponent panel) {

//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
				//Create and set up the window.
				JFrame frame = new JFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//				Dimension oldMin = frame.getMinimumSize();
//				System.out.println("Min frame: " + oldMin);
//
//				Dimension oldPref = frame.getPreferredSize();
//				System.out.println("Pref frame: " + oldPref);
				
				//Add contents to the window.
//				Dimension frameSize = frame.getSize();
				Dimension panelSize = panel.getSize();
//				int diffY = frameSize.height
//				System.out.println("Frame size: " + frame.getSize());
//				System.out.println("Panel size: " + panel.getSize());
				frame.setContentPane(panel);
//				
//				Dimension size = panel.getSize();
//				frame.setSize(size);
//				frame.setMinimumSize(size);
//				frame.add(panel);
//				Dimension panelSize = panel.getSize();
				frame.setSize(new Dimension(panelSize.width*2,panelSize.height*2));
				//Display the window.
//				frame.pack();
				frame.setVisible(true);
//				System.out.println("Frame size: " + frame.getSize());
				
				return frame;
//			}
//		});
	}
}
