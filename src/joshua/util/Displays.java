package joshua.util;

import java.awt.GraphicsEnvironment;

public class Displays {
	
	public static int getMaxDisplayWidth() {
		return (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getWidth();
	}
}
