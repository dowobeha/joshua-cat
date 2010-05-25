package good;


import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
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
					parent.setVisible(true);
				}

			}

		}
		
	}

}
