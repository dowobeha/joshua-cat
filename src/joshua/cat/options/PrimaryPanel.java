package joshua.cat.options;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import joshua.util.Displays;

@SuppressWarnings("serial")
public class PrimaryPanel extends JScrollPane {
	
	private final List<JTextArea> sourceTextArea;
	private final List<TextCompletionArea> targetTextArea;
	private final List<SentencePanel> sentencePanels;
//	private final int spanLimit;
	
	private final List<TranslationOptions> translationsList;
	
	public PrimaryPanel(SourceText sourceText, TranslationOptionsCompletionModel completionModel, int spanLimit) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		this.setViewportView(contentPane);
		
		this.sourceTextArea = new ArrayList<JTextArea>();
		this.targetTextArea = new ArrayList<TextCompletionArea>();
		this.sentencePanels = new ArrayList<SentencePanel>();
		
//		this.spanLimit = spanLimit;
		this.translationsList = completionModel.getTranslationOptions();
		
		// Create text areas
		for (String sourceSentence : sourceText) {
			sourceTextArea.add(new JTextArea(sourceSentence));
			targetTextArea.add(new TextCompletionArea(completionModel));
			sentencePanels.add(new SentencePanel(new SentencePanelModel(sourceSentence,spanLimit,translationsList)));
		}
		
		// Configure and lay out text areas
		Iterator<JTextArea> sourceTextAreas = sourceTextArea.iterator();
		Iterator<TextCompletionArea> targetTextAreas = targetTextArea.iterator();
		for (int i=0; sourceTextAreas.hasNext() && targetTextAreas.hasNext(); i++) {
			
				final JTextArea sourceSentenceArea = sourceTextAreas.next();
				sourceSentenceArea.addMouseListener(listener);
				sourceSentenceArea.setLineWrap(true);
				sourceSentenceArea.setWrapStyleWord(true);
				sourceSentenceArea.setEditable(false);
				contentPane.add(sourceSentenceArea);
			

				final TextCompletionArea targetSentenceArea = targetTextAreas.next();
				targetSentenceArea.setLineWrap(true);
				contentPane.add(targetSentenceArea);
				
//				final SentencePanel sentencePanel = sentencePanels.get(i);
				final JPanel sentencePanel = new JPanel();
				sentencePanel.setLayout(new BoxLayout(sentencePanel, BoxLayout.PAGE_AXIS));
				contentPane.add(sentencePanel);
				
				final int index = i;
				
				JPanel buttonPanel = new JPanel();
				final JButton more = new JButton("+");
				final JButton less = new JButton("-");
				
				more.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
//						System.out.println("contentPane Before:\t"+contentPane.getHeight());
//						System.out.println("sentencePanel Before:\t"+sentencePanel.getHeight());
						sentencePanel.add(sentencePanels.get(index));
						{
							int height = 0;
							for (Component c : contentPane.getComponents()) {
								height += c.getPreferredSize().getHeight();
							}
							contentPane.setPreferredSize(new Dimension((int)contentPane.getPreferredSize().getWidth(),height));
						}
//						sentencePanel.setSize(sentencePanel.getPreferredSize());
//						sentencePanel.setMinimumSize(sentencePanel.getPreferredSize());
//						System.out.println("contentPane After:\t"+contentPane.getHeight());
//						System.out.println("sentencePanel After:\t"+sentencePanel.getHeight());
						more.setEnabled(false);
						less.setEnabled(true);
//						PrimaryPanel.this.setViewportView(null);
//						contentPane.validate();
//						contentPane.repaint();
//						sentencePanel.revalidate();
//						contentPane.revalidate();
//						PrimaryPanel.this.revalidate();
//						sentencePanel.doLayout();
//						contentPane.doLayout();
//						PrimaryPanel.this.doLayout();
//						for (Component c : contentPane.getComponents()) {
//							c.validate();
//							c.repaint();
//						}
//						contentPane.validate();
//						contentPane.repaint();
//						PrimaryPanel.this.setViewportView(contentPane);
						PrimaryPanel.this.validate();
						PrimaryPanel.this.repaint();
//						
//						System.out.println("contentPane Finally:\t"+contentPane.getHeight());
//						System.out.println("sentencePanel Finally:\t"+sentencePanel.getHeight());
					}
				});
				
				less.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						sentencePanel.remove(sentencePanels.get(index));
						more.setEnabled(true);
						less.setEnabled(false);
						{
							int height = 0;
							for (Component c : contentPane.getComponents()) {
								height += c.getPreferredSize().getHeight();
							}
							contentPane.setPreferredSize(new Dimension((int)contentPane.getPreferredSize().getWidth(),height));
						}
//						sentencePanel.revalidate();
//						contentPane.revalidate();
						PrimaryPanel.this.validate();
						PrimaryPanel.this.repaint();
					}		
				});
				
				less.setEnabled(false);
				buttonPanel.add(more);
				buttonPanel.add(less);
				
				
				
				contentPane.add(buttonPanel);
				

			
		}
		contentPane.doLayout();
		
		int displayWidth = Displays.getMaxDisplayWidth();
		Dimension size = new Dimension((int) displayWidth, (int) contentPane.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);
		
		
		
//		this.setMinimumSize(size);
//		this.setPreferredSize(size);
//		this.setSize(size);
	}
	
	private final MouseListener listener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(e);
		}
	};
	
}
