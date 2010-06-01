package joshua.cat.view;

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
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import net.dowobeha.ui.ScrollablePanel;


import joshua.cat.SourceText;
import joshua.cat.TranslationOptions;
import joshua.cat.model.SentencePanelModel;
import joshua.cat.model.TranslationOptionsCompletionModel;
import joshua.util.Displays;

@SuppressWarnings("serial")
public class PrimaryPanel extends JScrollPane {
	
	@SuppressWarnings("unused")
	private static final Logger logger = 
		Logger.getLogger(PrimaryPanel.class.getName());
	
	private final List<JTextArea> sourceTextArea;
	private final List<TextCompletionArea> targetTextArea;
	private final List<ChildScrollPane> childScrollPanes;
//	private final List<SentencePanel> sentencePanels;
//	private final int spanLimit;
	
	private final List<TranslationOptions> translationsList;
	
	public PrimaryPanel(SourceText sourceText, TranslationOptionsCompletionModel completionModel, int spanLimit) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		final JPanel contentPane = new ScrollablePanel(this,true,false,4);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		this.setViewportView(contentPane);
		
		this.sourceTextArea = new ArrayList<JTextArea>();
		this.targetTextArea = new ArrayList<TextCompletionArea>();
		this.childScrollPanes = new ArrayList<ChildScrollPane>();
//		this.sentencePanels = new ArrayList<SentencePanel>();
		
//		this.spanLimit = spanLimit;
		this.translationsList = completionModel.getTranslationOptions();
		
		// Create text areas
		for (String sourceSentence : sourceText) {
			sourceTextArea.add(new JTextArea(sourceSentence));
			targetTextArea.add(new TextCompletionArea(completionModel));
			SentencePanelModel sentencePanelModel = new SentencePanelModel(sourceSentence,spanLimit,translationsList);
			childScrollPanes.add(new ChildScrollPane(sentencePanelModel));
//			sentencePanels.add(new SentencePanel(sentencePanelModel));
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
				
				final JPanel sentencePanel = new JPanel();
				ChildScrollPane childScrollPane = childScrollPanes.get(i);
				sentencePanel.add(childScrollPane);
				sentencePanel.setLayout(new BoxLayout(sentencePanel, BoxLayout.PAGE_AXIS));
				contentPane.add(sentencePanel);
				
//				childScrollPane.doLayout();
//				Rectangle bounds = childScrollPane.getViewportBorderBounds();
//				System.out.println(bounds);
//				Dimension d = new Dimension(bounds.width,bounds.height);
//				
//				childScrollPane.getViewport().getView().setPreferredSize(d);
//				PrimaryPanel.this.validate();
//				PrimaryPanel.this.repaint();
//				
				
				final int index = i;
				
				JPanel buttonPanel = new JPanel();
				final JButton more = new JButton("+");
				final JButton less = new JButton("-");
				
				more.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						
						sentencePanel.add(childScrollPanes.get(index)); 
						sentencePanel.setPreferredSize(childScrollPanes.get(index).getPreferredSize());
						{
							int viewportWidth = (int) PrimaryPanel.this.getViewportBorderBounds().getWidth();
							int height = 0;
							for (Component c : contentPane.getComponents()) {
								height += c.getPreferredSize().getHeight();
							}
							contentPane.setPreferredSize(new Dimension(viewportWidth,height));
						}
						
						more.setEnabled(false);
						less.setEnabled(true);
						
						PrimaryPanel.this.validate();
						PrimaryPanel.this.repaint();
						
					}
				});
				
				less.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						sentencePanel.remove(childScrollPanes.get(index));
						sentencePanel.setPreferredSize(new Dimension(0,0));
						{
							int viewportWidth = (int) PrimaryPanel.this.getViewportBorderBounds().getWidth();
							int height = 0;
							for (Component c : contentPane.getComponents()) {
								height += c.getPreferredSize().getHeight();
							}
							contentPane.setPreferredSize(new Dimension(viewportWidth,height));
						}
						
						more.setEnabled(true);
						less.setEnabled(false);
						
						PrimaryPanel.this.validate();
						PrimaryPanel.this.repaint();
					}		
				});
				
				more.setEnabled(false);
				buttonPanel.add(more);
				buttonPanel.add(less);
				
				contentPane.add(buttonPanel);
							
		}
//		contentPane.doLayout();
		
		int displayWidth = Displays.getMaxDisplayWidth();
		Dimension size = new Dimension((int) displayWidth, (int) contentPane.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);
		
		int viewportWidth = (int) this.getViewportBorderBounds().getWidth();
		for (Component c : contentPane.getComponents()) {
			int height = (int) c.getPreferredSize().getHeight();
			c.setPreferredSize(new Dimension(viewportWidth,height));
		}
		
		PrimaryPanel.this.validate();
		PrimaryPanel.this.repaint();
		
	}
	
	private final MouseListener listener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(e);
		}
	};

}
