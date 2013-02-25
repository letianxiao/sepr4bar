package dab.gui;

import javax.swing.ImageIcon;

/**
 * Contains a image object, used with setVisible() to toggle the apperance
 * of components in the reactor
 * @author mb941
 *
 */
public class Component extends DynamicImage{
	
	//Register the object with the DynamicImageFactory, called when object is first loaded.
	static{
		DynamicImageFactory.instance().registerImage("Component", new Component(0, 0, 0, 0, ""));
	}

	/**
	 * Creates a new instance with the specified variables.
	 * Note: image is set to invisible by default.
	 * @param coordX The X location of the Component
	 * @param coordY The Y bound of the Component
	 * @param boundX The X location of the Component
	 * @param boundY The Y bound of the Component
	 * @param imagePath The image to use for this instance
	 */
	private Component(int coordX, int coordY, int boundX, int boundY, String imagePath){
		setBounds(coordX, coordY, boundX, boundY);
        setIcon(new ImageIcon(imagePath));
        setIconTextGap(0);
        setVisible(false);
	}
	
	/**
	 * Create a new Component and returns it.
	 */
	public Component createImage(int coordX, int coordY, int boundX, int boundY, String imagePath){
		return new Component(coordX, coordY, boundX, boundY, imagePath);
	}
}
