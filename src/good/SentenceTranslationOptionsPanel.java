package good;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SentenceTranslationOptionsPanel extends JPanel {

	private final int displayWidth;
	
	public SentenceTranslationOptionsPanel(String[] args, int spanLimit, TranslationOptions... translationsList) {
		this(args,spanLimit,Arrays.asList(translationsList));
	}
	
	public SentenceTranslationOptionsPanel(String[] args, int spanLimit, List<TranslationOptions> translationsList) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.displayWidth = getMaxDisplayWidth();
		
		Panel panel = new Panel(this,args,spanLimit,translationsList);
		int totalWidth = (int) panel.getPreferredSize().getWidth();
		int extra=displayWidth*2/3;
		List<Panel> panels = new ArrayList<Panel>();
		panels.add(panel);
		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
			panel = new Panel(this,args,spanLimit,translationsList);
			panels.add(panel);
		}

		Dimension size = new Dimension((int) displayWidth, (int) this.getPreferredSize().getHeight());
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setSize(size);

		for (int x=displayWidth; x+extra<totalWidth; x+=displayWidth) {
			panels.get(x/displayWidth).scrollPane.getViewport().setViewPosition(new Point(x,0));
		}
	}
	
	protected int getMaxDisplayWidth() {
		return (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getWidth();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		TranslationOptions translations = new TranslationOptions(corpus);
		
		int spanLimit = 5;
		

		Iterator<String> iterator = sourceText.iterator();
		iterator.next();
		String line2 = iterator.next();
		String[] sentence = line2.split(" ");
		
		JFrame window = new JFrame();
		SentenceTranslationOptionsPanel parent = new SentenceTranslationOptionsPanel(sentence,spanLimit,translations);
		window.setContentPane(parent);
		window.pack();
		window.setVisible(true);
		
		
	}
}
