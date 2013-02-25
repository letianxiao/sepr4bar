package dab.gui;

import java.util.HashMap;

/**
 * Creates DynamicImage objects registered to the factory.
 * @author Team Haddock
 *
 */
public class DynamicImageFactory {
	private static DynamicImageFactory instance;
	private HashMap<String, DynamicImage> registeredImages = new HashMap<String, DynamicImage>();

	/**
	 * 
	 * @return The singleton instance of this class
	 */
	public static DynamicImageFactory instance(){
		if(instance==null)
			instance=new DynamicImageFactory();
		return instance;
	}
	
	/**
	 * Registers a new image to the factory
	 * @param ID The string containing the registered ID of a image type
	 * @param image an instance of the image type to be registered, implementing DynamicImage
	 */
	public void registerImage(String ID, DynamicImage image){
		registeredImages.put(ID, image);
	}
	
	/**
	 * Produces a new image of the type associated with the imageID string.
	 * @param imageID String containing the name of the desired button
	 * @param coordX The X location of the Component
	 * @param coordY The Y bound of the Component
	 * @param boundX The X location of the Component
	 * @param boundY The Y bound of the Component
	 * @param imagePath The image to use for this instance
	 * @return a new button object which implements IButton.
	 */
	public DynamicImage createButton(String imageID, int coordX, int coordY, int boundX, int boundY, String imagePath){
		return (DynamicImage)registeredImages.get(imageID).createImage(coordX, coordY, boundX, boundY, imagePath);
	}
}
