package joshua.cat.view;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
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
	
	private final List<TranslationOptions> translationsList;
	
	public PrimaryPanel(SourceText sourceText, TranslationOptionsCompletionModel completionModel, int spanLimit) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		final JPanel contentPane = new ScrollablePanel(this,true,false,4);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		this.setViewportView(contentPane);
		
		this.sourceTextArea = new ArrayList<JTextArea>();
		this.targetTextArea = new ArrayList<TextCompletionArea>();
		this.childScrollPanes = new ArrayList<ChildScrollPane>();

		this.translationsList = completionModel.getTranslationOptions();
		
		// Create text areas
		for (String sourceSentence : sourceText) {
			sourceTextArea.add(new JTextArea(sourceSentence));
			targetTextArea.add(new TextCompletionArea(completionModel));
			SentencePanelModel sentencePanelModel = new SentencePanelModel(sourceSentence,spanLimit,translationsList);
			childScrollPanes.add(new ChildScrollPane(sentencePanelModel));
		}
		
		Set<? extends AWTKeyStroke> forwardFocusKeys = Collections.singleton(KeyStroke.getKeyStroke("TAB"));
		Set<? extends AWTKeyStroke> backwardFocusKeys = Collections.singleton(KeyStroke.getKeyStroke("shift TAB"));

		
		// Configure and lay out text areas
		Iterator<JTextArea> sourceTextAreas = sourceTextArea.iterator();
		Iterator<TextCompletionArea> targetTextAreas = targetTextArea.iterator();
		for (int i=0; sourceTextAreas.hasNext() && targetTextAreas.hasNext(); i++) {
			
				final JTextArea sourceSentenceArea = sourceTextAreas.next();
				sourceSentenceArea.setFocusable(false);
				sourceSentenceArea.addMouseListener(listener);
				sourceSentenceArea.setLineWrap(true);
				sourceSentenceArea.setWrapStyleWord(true);
				sourceSentenceArea.setEditable(false);
				contentPane.add(sourceSentenceArea);
			

				final TextCompletionArea targetSentenceArea = targetTextAreas.next();
				targetSentenceArea.setFocusable(true);
				targetSentenceArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardFocusKeys);
				targetSentenceArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardFocusKeys);
				targetSentenceArea.setLineWrap(true);
				contentPane.add(targetSentenceArea);
				
				final JPanel sentencePanel = new JPanel();
				sentencePanel.setLayout(new BoxLayout(sentencePanel, BoxLayout.PAGE_AXIS));
				contentPane.add(sentencePanel);
				
				final int index = i;
				
				JPanel buttonPanel = new JPanel();
				final JButton more = new JButton("+");
				final JButton less = new JButton("-");
				
				more.setFocusable(false);
				less.setFocusable(false);
				
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
				
				more.setEnabled(true);
				less.setEnabled(false);
				buttonPanel.add(more);
				buttonPanel.add(less);
				
				targetSentenceArea.addFocusListener(new FocusListener(){

					@Override
					public void focusGained(FocusEvent e) {
						more.doClick(0);
					}

					@Override
					public void focusLost(FocusEvent e) {
						less.doClick(0);
					}
					
				});
				
				contentPane.add(buttonPanel);
							
		}
		
		int displayWidth = Displays.getMaxDisplayWidth();
		Dimension size = new Dimension((int) displayWidth, (int) contentPane.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);
		
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
