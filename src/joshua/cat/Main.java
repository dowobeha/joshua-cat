/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat;

import java.io.IOException;


import joshua.cat.model.TranslationOptionsCompletionModel;
import joshua.cat.view.PrimaryPanel;
import joshua.cat.view.TranslateWindow;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		
		
		
		TranslateWindow window = new TranslateWindow();
		
		
		
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;

		PrimaryPanel primaryPanel = new PrimaryPanel(sourceText, new TranslationOptionsCompletionModel(translations),spanLimit);
		
		window.setContent(primaryPanel);
		
		
	}
	
}
