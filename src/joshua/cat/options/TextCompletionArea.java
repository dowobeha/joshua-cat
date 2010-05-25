package joshua.cat.options;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Thanks to Sun for providing the excellent TextAreaDemo
//	(http://java.sun.com/docs/books/tutorial/uiswing/examples/components/TextAreaDemoProject/src/components/TextAreaDemo.java)
//	which demonstrates how to do text completion in a JTextArea.
//
// This class is partially based on the techniques shown in that demo.
@SuppressWarnings("serial")
public class TextCompletionArea extends JTextArea implements DocumentListener {

	private static enum Actions { COMPLETE };
	
	private static enum InputMode { TYPING, COMPLETING }
	
	private InputMode mode;
	
	public TextCompletionArea() {
		this.mode = InputMode.TYPING;
		
		InputMap inputMap = getInputMap();
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), Actions.COMPLETE);
        inputMap.put(KeyStroke.getKeyStroke("TAB"), Actions.COMPLETE);
        
		ActionMap actionMap = getActionMap();
        actionMap.put(Actions.COMPLETE, new AbstractAction() {
            public void actionPerformed(ActionEvent ev) {
                if (mode == InputMode.COMPLETING) {
                    int textPosition = getSelectionEnd();
                    insert(" ", textPosition);
                    setCaretPosition(textPosition + 1);
                    mode = InputMode.TYPING;
                } 
            }
        });
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.getContentPane().add(new TextCompletionArea());
		window.setVisible(true);
	}

}
