package good;


import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;



@SuppressWarnings("serial")
public class Panel extends JPanel {

	final JScrollPane scrollPane;
	
	public Panel(Container parent, String[] args, int spanLimit, TranslationOptions... translationsList) {
		this(parent, args, spanLimit, Arrays.asList(translationsList));
	}
	
	public Panel(Container parent, String[] args, int spanLimit, List<TranslationOptions> translationsList) {
		super(new GridBagLayout());
		
		scrollPane = new JScrollPane(this);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//		int emptyScrollPaneHeight = scrollPane.getHeight();
		
		parent.add(scrollPane);
		
		for (int i=0; i<args.length*2; i++) {
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			JLabel label = new JLabel("");
			//			label.setBorder(BorderFactory.createLineBorder(Color.black));
			this.add(label,c);
		}

		// Add words
		for (int i=0; i<args.length; i+=1) {
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i*2;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			//			System.err.println(c.gridx);
			JLabel label = new JLabel(args[i], JLabel.CENTER);
//			label.addMouseListener(listener);
			//			label.setBorder(BorderFactory.createLineBorder(Color.black));
			this.add(label,c);
			this.doLayout();
//			parent.setVisible(true);
			//System.err.println("label["+i+"] is at ("+label.getX()+","+label.getY()+") - ("+(label.getX()+label.getWidth())+","+(label.getY()+label.getHeight())+")");
//			
		}
		
		JComboBox[][] comboBoxes;
		{
			int numRows=Math.min(args.length, spanLimit); 
			comboBoxes = new JComboBox[numRows][];

			// Add rows to chart
			for (int row=0; row<numRows; row+=1) {
				
				int numCells=args.length-row;
				comboBoxes[row] = new JComboBox[numCells];
				
				// Add cells to chart
				for (int cell=0; cell<numCells; cell+=1) {

					GridBagConstraints c = new GridBagConstraints();
					c.gridx = row + cell*2;
					c.gridy = 2 + row;
					c.gridheight = 1;
					c.gridwidth = 2;
					c.weightx = 1;
					c.fill = GridBagConstraints.HORIZONTAL;
					//				System.err.println(c.gridx);

					StringBuilder sourcePhraseBuilder = new StringBuilder();
					for (int wordIndex=cell, lastIndex=cell+row; wordIndex<=lastIndex; wordIndex++) {
						if (wordIndex>cell) {
							sourcePhraseBuilder.append(' ');
						}
						sourcePhraseBuilder.append(args[wordIndex]);
					}
					Model model = new Model(sourcePhraseBuilder.toString(), translationsList);
					JComboBox comboBox = new JComboBox(model);
					comboBox.setEditable(true);
//					comboBox.addActionListener(l);
					comboBoxes[row][cell] = comboBox;
//					for (Component component : comboBox.getComponents()) {
//						component.addMouseListener(listener);
//					}
					//				comboBox.addMouseListener(listener);
					this.add(comboBox,c);
					this.doLayout();
//					window.doLayout();
//					window.validate();
					parent.setVisible(true);
					
//					Component component = comboBox.getEditor().getEditorComponent();
					//System.err.println("comboBox["+row+"]["+cell+"] is at ("+comboBox.getX()+","+comboBox.getY()+") - ("+(comboBox.getX()+comboBox.getWidth())+","+(comboBox.getY()+comboBox.getHeight())+")");
//					System.err.println("comboBox["+row+"]["+cell+"] is at + " + comboBox.getVisibleRect());
					
					//comboBox.setEnabled(false);
					//				JLabel label = new JLabel(" R" + row + ",C"+cell);
					//				label.setBorder(BorderFactory.createLineBorder(Color.black));
					//				pane.add(label,c);
				}

			}

			
			
		}
		
		
		
//		int height = emptyScrollPaneHeight + (int) parent.getMinimumSize().getHeight();
//		int width = (int) scrollPane.getPreferredSize().getWidth();
//		Dimension size = new Dimension(width, height);
//		
//		scrollPane.setPreferredSize(size);
//		scrollPane.setSize(size);
		
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
	
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;
		
		JFrame window = new JFrame();
		JPanel parent = new JPanel();
		parent.setLayout(new BoxLayout(parent, BoxLayout.PAGE_AXIS));
		window.setContentPane(parent);
//		window.setVisible(true);
		
		Iterator<String> iterator = sourceText.iterator();
		iterator.next();
		String line2 = iterator.next();
		
		int displayWidth = (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getWidth();
//		System.out.println("displayWidth=="+displayWidth);
		Panel panel = new Panel(parent,line2.split(" "),spanLimit,translations);
		int totalWidth = (int) panel.getPreferredSize().getWidth();
//		System.out.println(panel.getPreferredSize());
//		System.out.println(panel.getSize());
//		window.pack();
//		int totalWidth=panel.getWidth();
		int extra=displayWidth*2/3;
//		window.setVisible(false);
		List<Panel> panels = new ArrayList<Panel>();
		panels.add(panel);
		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
//			System.out.println("x=="+x+"\ttotalWidth=="+totalWidth);
			panel = new Panel(parent,line2.split(" "),spanLimit,translations);
			panels.add(panel);
		}
		
		window.pack();
//		System.out.println(panel.getSize());
		window.setSize((int) displayWidth, window.getHeight());
//		System.out.println(panel.getSize());
		
//		System.out.println(panel.getSize());
		
		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
//			panel = new Panel(parent,line2.split(" "),spanLimit,translations);
//			window.pack();
//			window.setSize((int) displayWidth, window.getHeight());
//			window.setVisible(true);
			panels.get(x/displayWidth).scrollPane.getViewport().setViewPosition(new Point(x,0));
		}
		window.setVisible(true);
//		
//		
//		Panel panel2 = new Panel(parent,line2.split(" "),spanLimit,translations);
//		Panel panel3 = new Panel(parent,line2.split(" "),spanLimit,translations);
//		Panel panel4 = new Panel(parent,line2.split(" "),spanLimit,translations);
//
//		
//		
//		System.err.println("Before:\t" + panel2.scrollPane.getViewport().getViewRect().getWidth());
////		System.err.println("Visrec:\t" + panel2.scrollPane.getVisibleRect());
//		
////		panel2.scrollPane.scrollRectToVisible(new Rectangle(1276*2,0,1276*2+1,0));
//		System.out.println(panel1.getSize());
//		panel1.scrollPane.getViewport().setViewPosition(new Point(0,0));
//		panel2.scrollPane.getViewport().setViewPosition(new Point(1276,0));
//		panel3.scrollPane.getViewport().setViewPosition(new Point(1276*2,0));
//		panel4.scrollPane.getViewport().setViewPosition(new Point(1276*3,0));
//		
////		System.err.println("After:\t" + panel2.scrollPane.getViewport().getViewRect());
////		System.err.println("Visrec:\t" + panel2.scrollPane.getVisibleRect());
		
	}
}
