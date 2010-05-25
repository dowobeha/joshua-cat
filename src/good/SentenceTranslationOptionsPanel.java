package good;

import java.awt.Dimension;
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
public class SentenceTranslationOptionsPanel extends JPanel {

	private final int displayWidth;
	private final int numRows;
	
	public SentenceTranslationOptionsPanel(String[] args, int spanLimit, TranslationOptions... translationsList) {
		this(args,spanLimit,Arrays.asList(translationsList));
	}
	
	public SentenceTranslationOptionsPanel(String[] args, int spanLimit, List<TranslationOptions> translationsList) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.numRows=Math.min(args.length, spanLimit);
		this.displayWidth = getMaxDisplayWidth();
		
		ChildScrollPane panel = new ChildScrollPane(args,spanLimit,translationsList);
		int totalWidth = (int) panel.getPreferredSize().getWidth();
		int extra=displayWidth*2/3;
		List<ChildScrollPane> panels = new ArrayList<ChildScrollPane>();
		panels.add(panel);
		this.add(panel);
		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
			panel = new ChildScrollPane(args,spanLimit,translationsList);
			panels.add(panel);
			this.add(panel);
		}

		Dimension size = new Dimension((int) displayWidth, (int) this.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);

		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
			panels.get(x/displayWidth).getViewport().setViewPosition(new Point(x,0));
		}
	}
	
	protected int getMaxDisplayWidth() {
		return (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getWidth();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;
		

		Iterator<String> iterator = sourceText.iterator();
		iterator.next();
		String line2 = iterator.next();
		String[] sentence = line2.split(" ");
		
		JFrame window = new JFrame();
		SentenceTranslationOptionsPanel parent = new SentenceTranslationOptionsPanel(sentence,spanLimit,translations);
		window.setContentPane(parent);
		window.pack();
		window.setVisible(true);
		
		
	}
	
	private class ChildScrollPane extends JScrollPane {
		
		ChildScrollPane(String[] args, int spanLimit, List<TranslationOptions> translationsList) {
			super(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			setViewportView(new ChildPanel(args,spanLimit,translationsList));
		}
		
	}
	
	private class ChildPanel extends JPanel {

		final JComboBox[][] comboBoxes;
		
		public ChildPanel(String[] args, int spanLimit, List<TranslationOptions> translationsList) {
			super(new GridBagLayout());
			
			for (int i=0; i<args.length*2; i++) {
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = i;
				c.gridy = 0;
				c.gridheight = 1;
				c.gridwidth = 1;
				c.weightx = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				JLabel label = new JLabel("");
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

				JLabel label = new JLabel(args[i], JLabel.CENTER);
				this.add(label,c);
				this.doLayout();
			}
			
			
			{
				 
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
						comboBoxes[row][cell] = comboBox;
						this.add(comboBox,c);
						this.doLayout();
					}

				}

			}
			
		}

	}
}
