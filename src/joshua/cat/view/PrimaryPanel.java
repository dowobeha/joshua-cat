package joshua.cat.view;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import net.dowobeha.prefs.PreferencesModel;
import net.dowobeha.ui.ScrollablePanel;

import joshua.cat.SourceText;
import joshua.cat.TranslationOptions;
import static joshua.cat.model.Preferences.Key.*;
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
	
	private final Collection<TranslationOptions> translationsList;
	
	
	public PrimaryPanel(final SourceText sourceText, TranslationOptionsCompletionModel completionModel) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		final JPanel contentPane = new ScrollablePanel(this,true,false,4);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		this.setViewportView(contentPane);
		
		final PreferencesModel preferencesModel = PreferencesModel.get(TranslateWindow.class.getClass());
				
		this.sourceTextArea = new ArrayList<JTextArea>();
		this.targetTextArea = new ArrayList<TextCompletionArea>();
		this.childScrollPanes = new ArrayList<ChildScrollPane>();

		this.translationsList = completionModel.getTranslationOptions();
		
		// Create text areas
		for (String sourceSentence : sourceText) {
			sourceTextArea.add(new JTextArea(sourceSentence));
			targetTextArea.add(new TextCompletionArea(completionModel));
			
		}
		
		Runnable spanLimitAction = new Runnable() {
			
			@Override
			public void run() {
				String value = preferencesModel.getValue(SPAN_LIMIT);
				int spanLimit;
				try {
					spanLimit = Integer.valueOf(value);
				} catch (NumberFormatException e) {
					spanLimit = Integer.valueOf(SPAN_LIMIT.getDefaultValue());
				}
				
				for (ChildScrollPane child : childScrollPanes) {
					Container parent = child.getParent();
					if (parent != null) {
						parent.remove(child);
					}
				}
				childScrollPanes.clear();
				for (String sourceSentence : sourceText) {
					SentencePanelModel sentencePanelModel = new SentencePanelModel(sourceSentence,spanLimit,translationsList);
					childScrollPanes.add(new ChildScrollPane(sentencePanelModel));
				}
			}
			
		};
		
		preferencesModel.register(SPAN_LIMIT, spanLimitAction);
		
		spanLimitAction.run();
		
		// Configure and lay out text areas
		Iterator<JTextArea> sourceTextAreas = sourceTextArea.iterator();
		Iterator<TextCompletionArea> targetTextAreas = targetTextArea.iterator();
		for (int i=0; sourceTextAreas.hasNext() && targetTextAreas.hasNext(); i++) {
			final int index = i;
			
			final JPanel buttonPanel = new JPanel();
			contentPane.add(buttonPanel);
			
				final JTextArea sourceSentenceArea = sourceTextAreas.next();
				sourceSentenceArea.setFocusable(false);
				sourceSentenceArea.setLineWrap(true);
				sourceSentenceArea.setWrapStyleWord(true);
				sourceSentenceArea.setEditable(false);
				contentPane.add(sourceSentenceArea);
			

				final TextCompletionArea targetSentenceArea = targetTextAreas.next();
				targetSentenceArea.setFocusable(true);
				targetSentenceArea.setLineWrap(true);
				contentPane.add(targetSentenceArea);
				
				final JPanel sentencePanel = new JPanel();
				sentencePanel.setLayout(new BoxLayout(sentencePanel, BoxLayout.PAGE_AXIS));
				contentPane.add(sentencePanel);
				
				final JCheckBox enabled = new JCheckBox("Sentence " + index);
				enabled.setFocusable(false);
				buttonPanel.add(enabled);
				
				final Runnable expandSentencePanel = new Runnable() {
					@Override
					public void run() {
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
						
						if (! enabled.isSelected()) {
							enabled.setSelected(true);
						}
						
						PrimaryPanel.this.validate();
						PrimaryPanel.this.repaint();
						
						contentPane.scrollRectToVisible(
								new Rectangle(
										buttonPanel.getX(),
										buttonPanel.getY(),
										sourceSentenceArea.getWidth(),
										sourceSentenceArea.getHeight() 
										+ targetSentenceArea.getHeight() 
										+ sentencePanel.getHeight()
										+ buttonPanel.getHeight()
						));

						
					}
					
				};
				
				final Runnable contractSentencePanel = new Runnable() {
					@Override
					public void run() {
						sentencePanel.remove(childScrollPanes.get(index));
						sentencePanel.setPreferredSize(new Dimension(0,0));
						
						int viewportWidth = (int) PrimaryPanel.this.getViewportBorderBounds().getWidth();
						int height = 0;
						for (Component c : contentPane.getComponents()) {
							height += c.getPreferredSize().getHeight();
						}
						contentPane.setPreferredSize(new Dimension(viewportWidth,height));

						
						if (enabled.isSelected()) {
							enabled.setSelected(false);
						}
						
						PrimaryPanel.this.validate();
						PrimaryPanel.this.repaint();
					}
					
				};
				
				enabled.addItemListener(new ItemListener(){

					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange()==ItemEvent.SELECTED) {
							expandSentencePanel.run();
						} else {
							contractSentencePanel.run();
						}
					}

					
				});
				
				targetSentenceArea.addFocusListener(new FocusListener(){

					@Override
					public void focusGained(FocusEvent e) {
						expandSentencePanel.run();
					}

					@Override
					public void focusLost(FocusEvent e) {
						contractSentencePanel.run();
					}
					
				});
				
				
							
		}
		
		Runnable focusTraversalAction = new Runnable() {
			@Override
			public void run() {
				Set<? extends AWTKeyStroke> forwardFocusKeys = 
					Collections.singleton(
							KeyStroke.getKeyStroke(
									preferencesModel.getValue(FORWARD_FOCUS_TRAVERSAL)));
				Set<? extends AWTKeyStroke> backwardFocusKeys = 
					Collections.singleton(
							KeyStroke.getKeyStroke(
									preferencesModel.getValue(BACKWARD_FOCUS_TRAVERSAL)));

				
				for (TextCompletionArea targetSentenceArea : targetTextArea) {
					targetSentenceArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardFocusKeys);
					targetSentenceArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardFocusKeys);
				}
			}
			
		};
		
		preferencesModel.register(FORWARD_FOCUS_TRAVERSAL,focusTraversalAction); 
		preferencesModel.register(BACKWARD_FOCUS_TRAVERSAL,focusTraversalAction);
		
		focusTraversalAction.run();
		
		int displayWidth = Displays.getMaxDisplayWidth();
		Dimension size = new Dimension((int) displayWidth, (int) contentPane.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);
		
		PrimaryPanel.this.validate();
		PrimaryPanel.this.repaint();
		
	}

}
