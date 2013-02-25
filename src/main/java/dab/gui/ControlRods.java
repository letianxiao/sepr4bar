package dab.gui;

import javax.swing.ImageIcon;

/**
 * DynamicImage specific for the control rods in the reactor.
 * @author Team Haddock
 *
 */
public class ControlRods extends DynamicImage {
	
	//Registers the object with the DynamicImageFactory.
	static{
		DynamicImageFactory.instance().registerImage("ControlRods", new ControlRods(0, 0, 0, 0, ""));
	}

	/**
	 * 
	 * @param coordX The X location of the Component
	 * @param coordY The Y bound of the Component
	 * @param boundX The X location of the Component
	 * @param boundY The Y bound of the Component
	 * @param imagePath The image to use for this instance
	 */
	public ControlRods(int coordX, int coordY, int boundX,
			int boundY, String imagePath){
		setIcon(new ImageIcon(imagePath));
		//setIcon(new ImageIcon("resources/mainInterface/RODS_SCALED.png"));
        setVisible(true);
        setBounds(coordX, coordY, boundX, boundY);
        //setBounds(44, 250, 300, 300);
	}

	@Override
	
	/**
	 * Create an object with the specified parameters.
	 * @param coordX The X location of the Component
	 * @param coordY The Y bound of the Component
	 * @param boundX The X location of the Component
	 * @param boundY The Y bound of the Component
	 * @param imagePath The image to use for this instance
	 * @return a new ControlRods object, cast to a DynamicImage
	 */
	public DynamicImage createImage(int coordX, int coordY, int boundX,
			int boundY, String imagePath) {
		
		return new ControlRods(coordX, coordY, boundX, boundY, imagePath);
	}
}