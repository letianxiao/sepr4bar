package dab.gui;

import javax.swing.JLabel;

/**
 * Abstract class defining the DynamicImage for the DynamicImageFactory
 * @author Team Haddock
 *
 */
public abstract class DynamicImage extends JLabel {
	
	public abstract DynamicImage createImage(int coordX, int coordY, int boundX, int boundY, String imagePath);

}
