package joshua.cat.view;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

	private final Map<TextCompletionArea,KeyListener> textCompletionAreaKeyListeners;

	private final JPanel contentPane;
	private double scrollFactor = 1.0;
	
	public PrimaryPanel(final SourceText sourceText, TranslationOptionsCompletionModel completionModel) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		///////////////////////////////////////////////
		// Construct the content pane and            //
		//   child components of this window:        //
		///////////////////////////////////////////////

		contentPane = new ScrollablePanel(this,true,false,4);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		this.setViewportView(contentPane);

		this.sourceTextArea = new ArrayList<JTextArea>();
		this.targetTextArea = new ArrayList<TextCompletionArea>();
		this.childScrollPanes = new ArrayList<ChildScrollPane>();
		this.translationsList = completionModel.getTranslationOptions();
		this.textCompletionAreaKeyListeners = new HashMap<TextCompletionArea,KeyListener>();

		// Create text areas
		for (String sourceSentence : sourceText) {
			sourceTextArea.add(new JTextArea(sourceSentence));
			targetTextArea.add(new TextCompletionArea(completionModel));

		}


		///////////////////////////////////////////////
		// Now define actions and listeners          //
		//   on these newly created components:      //
		///////////////////////////////////////////////

		final PreferencesModel preferencesModel = PreferencesModel.get(TranslateWindow.class.getClass());

		// Define the action to be run
		//   when the span limit preference is changed
		{
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

					int i=0;
					for (String sourceSentence : sourceText) {
						if (childScrollPanes.size() <= i) {
							SentencePanelModel sentencePanelModel = new SentencePanelModel(sourceSentence,spanLimit,translationsList);
							childScrollPanes.add(new ChildScrollPane(sentencePanelModel));
						} else {
							childScrollPanes.get(i).reset(spanLimit,translationsList);
						}
						i+=1;
					}

				}

			};

			// Register the action with the preference model,
			// then run the action now
			preferencesModel.register(SPAN_LIMIT, spanLimitAction);
			spanLimitAction.run();
		}

		// Define the action to be run
		//   when either keyboard shortcut preference 
		//   for focus traversal is changed
		{
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

			// Register the action with the preference model,
			// then run the action now
			preferencesModel.register(FORWARD_FOCUS_TRAVERSAL,focusTraversalAction); 
			preferencesModel.register(BACKWARD_FOCUS_TRAVERSAL,focusTraversalAction);
			focusTraversalAction.run();
		}


		// Iterate over each sentence in the source text
		Iterator<JTextArea> sourceTextAreas = sourceTextArea.iterator();
		Iterator<TextCompletionArea> targetTextAreas = targetTextArea.iterator();
		for (int i=0; sourceTextAreas.hasNext() && targetTextAreas.hasNext(); i++) {
			final int index = i;

			///////////////////////////////////////////////
			// For each sentence in the source text,     //
			//   we will construct four main components: //
			///////////////////////////////////////////////

			// Construct a check box 
			//   to govern when the sentence panel is shown
			final JCheckBox enabled = new JCheckBox("Sentence " + index);
			enabled.setFocusable(false);
			final JPanel buttonPanel = new JPanel();
			buttonPanel.add(enabled);
			contentPane.add(buttonPanel);

			// Construct a text area
			//   to display the source sentence
			final JTextArea sourceSentenceArea = sourceTextAreas.next();
			sourceSentenceArea.setFocusable(false);
			sourceSentenceArea.setLineWrap(true);
			sourceSentenceArea.setWrapStyleWord(true);
			sourceSentenceArea.setEditable(false);
			contentPane.add(sourceSentenceArea);

			// Construct a text completion area 
			//   where the user can type the target sentence translation
			final TextCompletionArea targetSentenceArea = targetTextAreas.next();
			targetSentenceArea.setFocusable(true);
			targetSentenceArea.setLineWrap(true);
			contentPane.add(targetSentenceArea);

			// Construct a sentence panel
			//   where the translation options for the sentence can be displayed
			final JPanel sentencePanel = new JPanel();
			sentencePanel.setLayout(new BoxLayout(sentencePanel, BoxLayout.PAGE_AXIS));
			contentPane.add(sentencePanel);



			///////////////////////////////////////////////
			// Now define actions and listeners          //
			//   on these newly created components:      //
			///////////////////////////////////////////////

			// Define the action to be run
			//   when this sentence panel should be expanded
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

			// Define the action to be run
			//   when this sentence panel should be contracted
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

			// Define a listener on the check box
			//   to expand and contract the sentence panel
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

			// Define a focus listener on the target sentence area
			//   to expand and contract the sentence panel
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

			
			{
				// Define the action to be run
				//   to allow keyboard scrolling of sentence panel
				Runnable textCompletionAreaKeyAction = new Runnable() {
					@Override
					public void run() {
						final int sentencePanelScrollLeft = 
							KeyStroke.getKeyStroke(
									preferencesModel.getValue(SCROLL_SENTENCE_PANEL_LEFT)).getKeyCode();
						
						final int sentencePanelScrollRight = 
							KeyStroke.getKeyStroke(
									preferencesModel.getValue(SCROLL_SENTENCE_PANEL_RIGHT)).getKeyCode();

						final int pageScrollDown = 
							KeyStroke.getKeyStroke(
									preferencesModel.getValue(SCROLL_PAGE_DOWN)).getKeyCode();
						
						final int pageScrollUp = 
							KeyStroke.getKeyStroke(
									preferencesModel.getValue(SCROLL_PAGE_UP)).getKeyCode();
						
						targetSentenceArea.removeKeyListener(textCompletionAreaKeyListeners.get(targetSentenceArea));
						
						KeyListener keyListener = new KeyAdapter() {
							@Override
							public void keyPressed(KeyEvent e) {
								int keyCode = e.getKeyCode();
								
								if (keyCode==sentencePanelScrollLeft) {
									childScrollPanes.get(index).scrollLeft();
								} else if (keyCode==sentencePanelScrollRight) {
									childScrollPanes.get(index).scrollRight();
								} 
								else if (keyCode==pageScrollDown) {
									scrollDown();
								} else if (keyCode==pageScrollUp) {
									scrollUp();
								}
							}
						};
						targetSentenceArea.addKeyListener(keyListener);
						textCompletionAreaKeyListeners.put(targetSentenceArea, keyListener);
					}

				};
				
				// Register the action with the preference model,
				// then run the action now
				preferencesModel.register(SCROLL_PAGE_DOWN,textCompletionAreaKeyAction);
				preferencesModel.register(SCROLL_PAGE_UP,textCompletionAreaKeyAction);
				preferencesModel.register(SCROLL_SENTENCE_PANEL_LEFT,textCompletionAreaKeyAction); 
				preferencesModel.register(SCROLL_SENTENCE_PANEL_RIGHT,textCompletionAreaKeyAction);
				textCompletionAreaKeyAction.run();
			}
			
			
			{
				// Define the action to be run
				//   to set the horizontal scroll factor
				Runnable scrollFactorAction = new Runnable() {
					@Override
					public void run() {
						final double scrollFactor = 
							Double.parseDouble(
									preferencesModel.getValue(SENTENCE_PANEL_HORIZONTAL_SCROLL_FACTOR));
						
						childScrollPanes.get(index).setScrollFactor(scrollFactor);
					}
				};
				
				// Register the action with the preference model,
				// then run the action now
				preferencesModel.register(SENTENCE_PANEL_HORIZONTAL_SCROLL_FACTOR,scrollFactorAction); 
				scrollFactorAction.run();
			}
		}


		// Set the size of the window
		int displayWidth = Displays.getMaxDisplayWidth();
		Dimension size = new Dimension((int) displayWidth, (int) contentPane.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);

		// Ensure that this window 
		//   and all of its children
		//   are properly displayed
		PrimaryPanel.this.validate();
		PrimaryPanel.this.repaint();

	}

	public void scrollUp() {
		Rectangle visible = this.getViewport().getViewRect();
		
		int newY = (int) (visible.y - visible.height*scrollFactor);
		Rectangle scrollTo = new Rectangle(
				visible.x,
				newY,
				visible.width,visible.height);

		
		for (TextCompletionArea targetText : targetTextArea) {
			if (targetText.getY() >= newY) {
				targetText.requestFocus();
			}
			break;
		}
		
		contentPane.scrollRectToVisible(scrollTo);
	}
	
	public void scrollDown() {
		Rectangle visible = this.getViewport().getViewRect();
		
		int newY = (int) (visible.y + visible.height*scrollFactor);
		Rectangle scrollTo = new Rectangle(
				visible.x,
				newY,
				visible.width,visible.height);
		
		int newBottom = newY + visible.height;
		
		TextCompletionArea scrollToText = targetTextArea.get(0);
		for (TextCompletionArea targetText : targetTextArea) {
			if (targetText.getY() < newBottom) {
				scrollToText = targetText;
			} else {
				break;
			}
		}
		scrollToText.requestFocus();
		
		contentPane.scrollRectToVisible(scrollTo);
	}
}
