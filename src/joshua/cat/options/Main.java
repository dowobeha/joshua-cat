/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.options;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import joshua.ui.StartupWindow;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		StartupWindow splashScreen = new StartupWindow("Translate","Lane Schwartz","2010",Color.BLACK,5);
		
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;
//		
//
//		Iterator<String> iterator = sourceText.iterator();
//		iterator.next();
//		String line2 = iterator.next();
		
//		JFrame window = new JFrame();
//		SentencePanelModel model = new SentencePanelModel(line2,spanLimit,translations);
//		SentencePanel parent = new SentencePanel(model);
//		window.setContentPane(parent);
//		window.pack();
//		
////		window.addMouseListener(parent.mouseListener);
//		
//		JFrame window2 = new JFrame();
//		window2.getContentPane().add(new BitextPanel(sourceText, new TranslationOptionsCompletionModel(translations)));
//		window2.pack();
//		
//		
//		window.setVisible(true);
//		window2.setVisible(true);
		
		JFrame window3 = new JFrame();
		window3.getContentPane().add(new PrimaryPanel(sourceText, new TranslationOptionsCompletionModel(translations),spanLimit));
		window3.pack();
		window3.setVisible(true);
		
		
		splashScreen.setVisible(false);
	}
	
}
