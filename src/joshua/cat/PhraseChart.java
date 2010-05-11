package joshua.cat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import joshua.ui.GUI;

@SuppressWarnings("serial")
public class PhraseChart extends JComponent {
	
	private final List<String> source;
	private final List<TextBox> sourceBoxes; 
	
	private final List<Rectangle2D> row;
	
	private double topMargin = 20;
	private double leftMargin = 20;
	
	private double internalMargin = 20;
	
	private FontMetrics fontInfo;
	private Font font;
	
	private final double spaceWidth;
	
	public PhraseChart(final String[] sourceText) {
		
		// Store list of source words
		source = new AbstractList<String>() {

			@Override
			public String get(int index) {
				return sourceText[index];
			}

			@Override
			public int size() {
				return sourceText.length;
			}
			
		};
		
		this.sourceBoxes = new ArrayList<TextBox>(source.size());
		
		this.setOpaque(true);
		
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		fontInfo = this.getFontMetrics(font);
		Graphics g = this.getGraphics();
		
		double maxY = topMargin;
		double maxX = leftMargin;
		
		// Calculate text boxes
		{
			double x = leftMargin;
			double y = topMargin;
			
			
			Rectangle2D spaceBounds = fontInfo.getStringBounds(" ", g);
			spaceWidth = spaceBounds.getWidth();
			
			for (String word : source) {
				Rectangle2D bounds = fontInfo.getStringBounds(word, g);
				
				double width = bounds.getWidth();
				double height = bounds.getHeight();
				
				maxY = Math.max(maxY, y+height);
				
				Rectangle2D box = new Rectangle2D.Double(x, y, width, height);
				sourceBoxes.add(new TextBox(word,box));
				
				x += bounds.getWidth() + spaceWidth;
				maxX = x;
			}
			
			Dimension d = new Dimension((int) Math.ceil(maxX), (int) Math.ceil(maxY));
			
			this.setMinimumSize(d);
			this.setSize(d);
			this.setVisible(true);
			
		}
		
		// Calculate pyramid
		this.row = new ArrayList<Rectangle2D>();
		{
			double y = maxY + internalMargin/2;
			
			for (TextBox sourceBox : sourceBoxes) {
				
				double width = sourceBox.getWidth() + spaceWidth;
				double height = sourceBox.getHeight();
				double x = sourceBox.getX() - spaceWidth/2;
				
				Rectangle2D box = new Rectangle2D.Double(x, y, width, height);
				row.add(box);
				
			}
		}
		
	}
	
	protected void paintComponent(Graphics graphics) {
		
		Graphics2D g = (Graphics2D) graphics;
		g.setFont(font);
		
		// Draw background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Draw words
		g.setColor(Color.BLACK);
		for (TextBox word : sourceBoxes) {			
			word.paint(g);	
		}
		
		// Draw pyramid
		g.setColor(Color.BLACK);
		for (Rectangle2D box : row) {
			g.draw(box);
		}
//		{
//			double[] xCoords = new double[sourceBoxes.size()];
//			int index = 0;
//			double boxWidth = 0;
//			double boxX = 0;
//			for (TextBox word : sourceBoxes) {	
//				boxWidth = word.getWidth();
//				boxX = word.getX();
//				xCoords[index] = boxX;
//				index += 1;
//			}
//			xCoords[index] = boxX + boxWidth;
//		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GUI.wrapAndShow("Phrase Chart", new PhraseChart(args));
	}

}
