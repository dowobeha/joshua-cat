package joshua.cat;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import joshua.corpus.suffix_array.ParallelCorpusGrammarFactory;
import joshua.corpus.vocab.SymbolTable;
import joshua.decoder.ff.tm.Rule;
import joshua.decoder.ff.tm.RuleCollection;
import joshua.decoder.ff.tm.Trie;
import joshua.prefix_tree.ExtractRules;
import joshua.prefix_tree.PrefixTree;


public class Scratch {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		String joshDirName = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		ExtractRules extractRules = new ExtractRules();
		extractRules.setJoshDir(joshDirName);
		
		ParallelCorpusGrammarFactory parallelCorpus = extractRules.getGrammarFactory();
		SymbolTable vocab = parallelCorpus.getSuffixArray().getVocabulary();
		int[] sentence = vocab.addTerminals(args);
		
		PrefixTree prefixTree = new PrefixTree(parallelCorpus);
//		prefixTree.setPrintStream(System.out);
		prefixTree.add(sentence);
		
		Trie grammar = prefixTree.getTrieRoot();
//		RuleExtractor ruleExtractor = parallelCorpus.getRuleExtractor();
//		ruleExtractor.extractRules(sourceHierarchicalPhrases);
		
		Listener listener = new Listener();

		int spanLimit = 5;

		JPanel pane = new JPanel(new GridBagLayout());

		JFrame window = new JFrame();
		System.err.println("window.getContentPane().getWidth() = " + window.getContentPane().getWidth());
		System.err.println("window.getWidth() = " + window.getWidth());
		window.addMouseListener(listener);
		JScrollPane scrollPane = new JScrollPane(pane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		System.err.println("pane.getWidth() = " + pane.getWidth());
//		System.err.println("scrollPane.getViewport().getView().getWidth() = " + scrollPane.getViewport().getView().getWidth());
		window.add(scrollPane);
//		window.add(pane);
		window.setVisible(true);
		
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
			pane.add(label,c);
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
			label.addMouseListener(listener);
			//			label.setBorder(BorderFactory.createLineBorder(Color.black));
			pane.add(label,c);
			pane.doLayout();
			window.setVisible(true);
			System.err.println("label["+i+"] is at ("+label.getX()+","+label.getY()+") - ("+(label.getX()+label.getWidth())+","+(label.getY()+label.getHeight())+")");
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

					String[] strings;
					{
						Trie trieNode = grammar;
						for (int wordIndex=cell, lastIndex=cell+row; wordIndex<=lastIndex; wordIndex++) {
							if (trieNode==null) {
								break;
							} else {
								trieNode = trieNode.matchOne(sentence[wordIndex]);
							}
						}

						if (trieNode == null) {
							strings = new String[0];
						} else {
							RuleCollection ruleCollection = trieNode.getRules();
							List<Rule> rules = ruleCollection.getRules();
							strings = new String[rules.size()];
							for (int i=0, n=rules.size(); i<n; i++) {
								int[] translation = rules.get(i).getEnglish();
								strings[i] = vocab.getWords(translation);
							}
						}
					}

					//				String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
					JComboBox comboBox = new JComboBox(strings);
					comboBoxes[row][cell] = comboBox;
					for (Component component : comboBox.getComponents()) {
						component.addMouseListener(listener);
					}
					//				comboBox.addMouseListener(listener);
					pane.add(comboBox,c);
					pane.doLayout();
//					window.doLayout();
//					window.validate();
					window.setVisible(true);
					
//					Component component = comboBox.getEditor().getEditorComponent();
					System.err.println("comboBox["+row+"]["+cell+"] is at ("+comboBox.getX()+","+comboBox.getY()+") - ("+(comboBox.getX()+comboBox.getWidth())+","+(comboBox.getY()+comboBox.getHeight())+")");
//					System.err.println("comboBox["+row+"]["+cell+"] is at + " + comboBox.getVisibleRect());
					
					comboBox.setEnabled(false);
					//				JLabel label = new JLabel(" R" + row + ",C"+cell);
					//				label.setBorder(BorderFactory.createLineBorder(Color.black));
					//				pane.add(label,c);
				}

			}
		}
		System.err.println("pane.getWidth() = " + pane.getWidth());
		
		
		System.err.println("window.getContentPane().getWidth() = " + window.getContentPane().getWidth());
		System.err.println("window.getWidth() = " + window.getWidth());
		window.pack();
		double displayWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getWidth();
		System.err.println("Display width = " + displayWidth);

		window.setSize((int) displayWidth, window.getHeight());
		window.setVisible(true);
		System.err.println("pane.getWidth() = " + pane.getWidth());
//		System.err.println("scrollPane.getViewport().getView().getWidth() = " + scrollPane.getViewport().getView().getWidth());
		System.err.println("window.getContentPane().getWidth() = " + window.getContentPane().getWidth());
		System.err.println("window.getWidth() = " + window.getWidth());
//		System.err.println("Maximized bounds = " + window.getMaximizedBounds().getWidth());
		
		// Print final widths
		{
			
			int numRows=Math.min(args.length, spanLimit); 
			
			System.err.println("\n\n\n\n\n\n");
			
			// Add rows to chart
			for (int row=0; row<numRows; row+=1) {
				
				int numCells=args.length-row;
				
				// Add cells to chart
				for (int cell=0; cell<numCells; cell+=1) {
					JComboBox comboBox = comboBoxes[row][cell];
					System.err.println("comboBox["+row+"]["+cell+"] is at ("+comboBox.getX()+","+comboBox.getY()+") - ("+(comboBox.getX()+comboBox.getWidth())+","+(comboBox.getY()+comboBox.getHeight())+")");
//					
				}
				
			}
			
			
			
			
		}
		
	}

	static class Listener implements MouseListener {

		private boolean comboBoxEvent(MouseEvent e) {
			return e.getComponent().getParent() instanceof JComboBox;
		}
		
		public void mousePressed(MouseEvent e) {
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(true);
				e.getComponent().getParent();
			}
		}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {
			if (comboBoxEvent(e)) {
//				e.getComponent().getParent().setEnabled(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (comboBoxEvent(e)) {
				e.getComponent().getParent().setEnabled(false);
			}
		}

		public void mouseClicked(MouseEvent e) {

		}

		void saySomething(String eventDescription, MouseEvent e) {
			System.err.println(eventDescription + " detected on "
					+ e.getComponent().getClass().getName()
					+ ".");
		}

	}

}
