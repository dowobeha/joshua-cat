package joshua.cat.view;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import net.dowobeha.prefs.PreferencesModel;
import static net.dowobeha.prefs.PreferencesModel.GROUP_KEYBOARD_SHORTCUTS;
import net.dowobeha.ui.ScrollablePanel;
import net.dowobeha.util.KeyStrokeFormatter;

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
	
	private final Collection<TranslationOptions> translationsList;
	
	private static final String FORWARD_FOCUS_TRAVERSAL = "Forward focus traversal";
	private static final String BACKWARD_FOCUS_TRAVERSAL = "Backward focus traversal";
	
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
		
		// Configure and lay out text areas
		Iterator<JTextArea> sourceTextAreas = sourceTextArea.iterator();
		Iterator<TextCompletionArea> targetTextAreas = targetTextArea.iterator();
		for (int i=0; sourceTextAreas.hasNext() && targetTextAreas.hasNext(); i++) {
			
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
				
				final int index = i;
				
//				buttonPanel.setLayout(new BorderLayout());
				final JButton more = new JButton("+");
				final JButton less = new JButton("-");
				//buttonPanel.add(new JToggleButton("visible"));
				final JCheckBox enabled = new JCheckBox("Sentence " + index);
				enabled.setFocusable(false);
				buttonPanel.add(enabled);
				
				more.putClientProperty("JComponent.sizeVariant", "mini");
				less.putClientProperty("JComponent.sizeVariant", "mini");
				
				more.setFocusable(false);
				less.setFocusable(false);
				
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
//						more.setEnabled(false);
//						less.setEnabled(true);
						
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
						{
							int viewportWidth = (int) PrimaryPanel.this.getViewportBorderBounds().getWidth();
							int height = 0;
							for (Component c : contentPane.getComponents()) {
								height += c.getPreferredSize().getHeight();
							}
							contentPane.setPreferredSize(new Dimension(viewportWidth,height));
						}
						
						if (enabled.isSelected()) {
						enabled.setSelected(false);
						}
//						more.setEnabled(true);
//						less.setEnabled(false);
						
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
				
				more.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						expandSentencePanel.run();
					}
				});
				
				less.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						contractSentencePanel.run();
					}		
				});
				
				more.setEnabled(true);
				less.setEnabled(false);
//				buttonPanel.add(more);
//				buttonPanel.add(less);
				
				targetSentenceArea.addFocusListener(new FocusListener(){

					@Override
					public void focusGained(FocusEvent e) {
						//enabled.doClick();
						expandSentencePanel.run();
					}

					@Override
					public void focusLost(FocusEvent e) {
						contractSentencePanel.run();
						//enabled.doClick();
					}
					
				});
				
				
							
		}
		
		final PreferencesModel preferencesModel = PreferencesModel.get(TranslateWindow.class.getClass());
		preferencesModel.setDefaultValue(FORWARD_FOCUS_TRAVERSAL, KeyStroke.getKeyStroke("TAB").toString());
		preferencesModel.setDefaultValue(BACKWARD_FOCUS_TRAVERSAL, KeyStroke.getKeyStroke("shift TAB").toString());

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
		
		KeyStrokeFormatter formatter = new KeyStrokeFormatter();
		
		preferencesModel.register(focusTraversalAction,formatter,GROUP_KEYBOARD_SHORTCUTS,FORWARD_FOCUS_TRAVERSAL); 
		preferencesModel.register(focusTraversalAction,formatter,GROUP_KEYBOARD_SHORTCUTS,BACKWARD_FOCUS_TRAVERSAL);
		
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
