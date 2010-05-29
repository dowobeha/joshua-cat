/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.options;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import joshua.cat.options.ui.PrimaryPanel;
import joshua.ui.StartupWindow;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		StartupWindow splashScreen = new StartupWindow("Translate","Lane Schwartz","2010",Color.BLACK,5);
		
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;

		JFrame window = new JFrame();
		window.getContentPane().add(new PrimaryPanel(sourceText, new TranslationOptionsCompletionModel(translations),spanLimit));
		window.pack();
		window.setVisible(true);
		
		
		splashScreen.setVisible(false);
	}
	
}
