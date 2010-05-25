package joshua.cat.options;

import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;
		

		Iterator<String> iterator = sourceText.iterator();
		iterator.next();
		String line2 = iterator.next();
		
		JFrame window = new JFrame();
		SentencePanelModel model = new SentencePanelModel(line2,spanLimit,translations);
		SentencePanel parent = new SentencePanel(model);
		window.setContentPane(parent);
		window.pack();
		window.setVisible(true);
//		window.addMouseListener(parent.mouseListener);
	}
	
}
