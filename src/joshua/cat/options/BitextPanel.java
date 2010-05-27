package joshua.cat.options;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import joshua.util.Colors;

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
		
	}

}
