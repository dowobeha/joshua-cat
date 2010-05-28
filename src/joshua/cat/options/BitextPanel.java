package joshua.cat.options;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import joshua.util.Displays;

@SuppressWarnings("serial")
public class BitextPanel extends JPanel {

	private final List<JTextArea> sourceTextArea;
	private final List<TextCompletionArea> targetTextArea;
	
	
	public BitextPanel(SourceText sourceText, TextCompletionModel completionModel) {
		this.setLayout(new GridLayout(0,2));
		
		this.sourceTextArea = new ArrayList<JTextArea>();
		this.targetTextArea = new ArrayList<TextCompletionArea>();
		
		// Create text areas
		for (String sourceSentence : sourceText) {
			sourceTextArea.add(new JTextArea(sourceSentence));
			targetTextArea.add(new TextCompletionArea(completionModel));
		}
		
		// Configure and lay out text areas
		Iterator<JTextArea> sourceTextAreas = sourceTextArea.iterator();
		Iterator<TextCompletionArea> targetTextAreas = targetTextArea.iterator();
		for (int i=0; sourceTextAreas.hasNext() && targetTextAreas.hasNext(); i++) {
			
//			Color background = (i%2==0) ? Color.WHITE : Colors.VERY_LIGHT_BLUE;
			
			
				final JTextArea sourceSentenceArea = sourceTextAreas.next();
//				sourceSentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				sourceSentenceArea.setLineWrap(true);
				sourceSentenceArea.setWrapStyleWord(true);
				sourceSentenceArea.setEditable(false);
//				sourceSentenceArea.setBackground(background);
				this.add(new JScrollPane(sourceSentenceArea));
			{
//				GridBagConstraints c = new GridBagConstraints();
//				c.gridx = 0;
//				c.gridy = i;
//				c.gridheight = 1;
//				c.gridwidth = 1;
//				c.weightx = 0.5;
////				c.weighty = 0.5;
//				c.fill = GridBagConstraints.HORIZONTAL;
////				this.add(new JScrollPane(sourceSentenceArea),c);
//				this.add(sourceSentenceArea,c);
			}

			
				final TextCompletionArea targetSentenceArea = targetTextAreas.next();
//				targetSentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				targetSentenceArea.setLineWrap(true);
//				targetSentenceArea.setBackground(background);
				this.add(new JScrollPane(targetSentenceArea));
//				targetSentenceArea.setPreferredSize(sourceSentenceArea.getPreferredSize());
//			{
//				GridBagConstraints c = new GridBagConstraints();
//				c.gridx = 1;
//				c.gridy = i;
//				c.gridheight = 1;
//				c.gridwidth = 1;
//				c.weightx = 0.5;
////				c.weighty = 0.5;
//				c.fill = GridBagConstraints.HORIZONTAL;
////				this.add(new JScrollPane(targetSentenceArea),c);
//				this.add(targetSentenceArea,c);
//			}

			
		}
		
		this.doLayout();
		
		int displayWidth = Displays.getMaxDisplayWidth();
		Dimension size = new Dimension((int) displayWidth, (int) this.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);
		
		this.doLayout();
		
		double height = 0;
		for (JTextArea sourceSentenceArea : sourceTextArea) {
			height += sourceSentenceArea.getPreferredSize().getHeight();
		}
		size = new Dimension((int) displayWidth, (int) height);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);
	}

}
