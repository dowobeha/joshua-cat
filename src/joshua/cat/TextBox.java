package joshua.cat;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class TextBox {

	private String text;
	private Rectangle2D bounds;
	
	public TextBox(String text, Rectangle2D bounds) {
		this.text = text;
		this.bounds = bounds;
	}

	public void paint(Graphics2D g) {
		g.drawString(text, (float) bounds.getX(), (float) (bounds.getY()+bounds.getHeight()));
//		g.draw(bounds);
	}
	
	public double getX() {
		return bounds.getX();
	}
	
	public double getWidth() {
		return bounds.getWidth();
	}
	
	public double getHeight() {
		return bounds.getHeight();
	}
}
