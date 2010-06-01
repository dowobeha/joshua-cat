/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.options;

//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Point;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.swing.BoxLayout;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.ScrollPaneConstants;
//
//import joshua.util.Displays;

@SuppressWarnings("serial")
public class SentencePanel extends JPanel {
//
//	private static final Logger logger =
//		Logger.getLogger(SentencePanel.class.getName());
//	
//	private static final Color LIGHT_RED = new Color(255,0,0,100);
//	
//	private final int displayWidth;
//	
//	private final SentencePanelModel model;
//	
//	final ComboBoxMouseListener mouseListener;
//
//	public SentencePanel(SentencePanelModel model) {
//		this.mouseListener = new ComboBoxMouseListener();
//		
//		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//		this.displayWidth = Displays.getMaxDisplayWidth();
//		
//		this.model = model;
////		
////		JTextArea sourceTextArea = new JTextArea(model.getSentence());
////		sourceTextArea.setLineWrap(true);
////		sourceTextArea.setWrapStyleWord(true);
////		sourceTextArea.setEditable(false);
////		this.add(new JScrollPane(sourceTextArea));
//		
//		ChildScrollPane panel = new ChildScrollPane(model.getWords());
//		int totalWidth = (int) panel.getPreferredSize().getWidth();
//		int extra=displayWidth*2/3;
//		List<ChildScrollPane> panels = new ArrayList<ChildScrollPane>();
//		panels.add(panel);
//		this.add(panel);
//		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
//			panel = new ChildScrollPane();
//			panels.add(panel);
//			this.add(panel);
//		}
//
//		Dimension size = new Dimension((int) displayWidth, (int) this.getPreferredSize().getHeight());
//		this.setMinimumSize(size);
//		this.setPreferredSize(size);
//		this.setSize(size);
//
//		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
//			panels.get(x/displayWidth).getViewport().setViewPosition(new Point(x,0));
//		}
//		
//
//	}
//
//	
//	
//	
//	

}


