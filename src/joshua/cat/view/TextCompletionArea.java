/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.view;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;

import joshua.cat.model.TextCompletionModel;


// Thanks to Sun for providing the excellent TextAreaDemo
//	(http://java.sun.com/docs/books/tutorial/uiswing/examples/components/TextAreaDemoProject/src/components/TextAreaDemo.java)
//	which demonstrates the use of text completion in a JTextArea.
//
// This class is partially based on the techniques shown in that demo.
@SuppressWarnings("serial")
public class TextCompletionArea extends JTextArea implements DocumentListener {

	private static enum Actions { COMPLETE, COMPLETE_WITH_SPACE, NEW_LINE };

	private static enum InputMode { TYPING, COMPLETING }

	private boolean completionInProgress = false;
//	private boolean pasting = false;
	
	private InputMode mode;


	private final TextCompletionModel completionModel;


	public TextCompletionArea(TextCompletionModel completionModel) {
		getDocument().addDocumentListener(this);

		this.completionModel = completionModel;


		this.mode = InputMode.TYPING;
		
		int controlMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		InputMap inputMap = getInputMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,controlMask), Actions.NEW_LINE);
		inputMap.put(KeyStroke.getKeyStroke("ENTER"), Actions.COMPLETE_WITH_SPACE);
	        
		ActionMap actionMap = getActionMap();
		actionMap.put(Actions.NEW_LINE, new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				int caretPosition = getCaretPosition();
				int lineNumber = getDocument().getDefaultRootElement().getElementIndex(caretPosition);
				try {
					int endOfLine = getLineEndOffset(lineNumber);
					TextCompletionArea.this.insert("\n", endOfLine);
				} catch (BadLocationException e) {
				}
				mode = InputMode.TYPING;
			}
		});
		actionMap.put(Actions.COMPLETE, new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (mode == InputMode.COMPLETING) {
					int textPosition = getSelectionEnd();
					setCaretPosition(textPosition);
					mode = InputMode.TYPING;
				} 
			}
		});
		actionMap.put(Actions.COMPLETE_WITH_SPACE, new AbstractAction() {
			public void actionPerformed(ActionEvent ev) {
				if (mode == InputMode.COMPLETING) {
					int textPosition = getSelectionEnd();					
					insert(" ", textPosition);
					setCaretPosition(textPosition+1);
					mode = InputMode.TYPING;
				} 
			}
		});
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {}

	@Override
	public void insertUpdate(DocumentEvent e) {
		
		if (! completionInProgress) {
			try {

				int endOfEdit =  e.getOffset() + e.getLength();

				String documentText = getText(0, endOfEdit);

				int lastSpaceIndex = documentText.lastIndexOf(' ');

				String prefix = documentText.substring(lastSpaceIndex+1);
				String completion = completionModel.complete(prefix);

				if (completion != null) {
					// Implementations of DocumentListener
					//    may not directly update their associated Document.
					//
					// So, ask the Swing event thread 
					//    to perform the actual Document update,
					//    as defined in the CompletionTask
					SwingUtilities.invokeLater(
							new CompletionTask(completion, endOfEdit));
				} else {
					// Since there is no valid completion to the current prefix,
					//    cancel the completion action by resuming typing mode
					mode = InputMode.TYPING;
				}


			} catch (BadLocationException exception) {
				// The editing location was somehow invalid
				//
				// Ensure that typing mode is resumed
				mode = InputMode.TYPING;
			} 
		}
		
	}

	private class CompletionTask implements Runnable {
		String completion;
		int position;

		CompletionTask(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}

		public void run() {
			completionInProgress = true;
			insert(completion, position);

			// Select the text
			setCaretPosition(position + completion.length());
			moveCaretPosition(position);

			mode = InputMode.COMPLETING;
			completionInProgress = false;
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {}

}
