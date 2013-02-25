package dab.gui;

import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * A controller class which holds several DynamicImages and displays one at a time.
 * @author TeamHaddock
 *
 */
public class ComponentController {
	private ArrayList<DynamicImage> images;
	
	public ComponentController(){
		images = new ArrayList<DynamicImage>();
	}
	
	/**
	 * Add a new DynamicImage to the controller with the specified parameters.
	 * @param coordX The X location of the Component
	 * @param coordY The Y bound of the Component
	 * @param boundX The X location of the Component
	 * @param boundY The Y bound of the Component
	 * @param imagePath The image to use for this instance
	 * @param comp The JComponent to add the image to.
	 */
	public void addImage(int coordX, int coordY, int boundX, int boundY, String imagePath, JComponent comp){
		DynamicImage temp = DynamicImageFactory.instance().createButton("Component", coordX, coordY, boundX, boundY, imagePath);
		images.add(temp);
		comp.add(temp);
	}
	
	/**
	 * Display the image with the specified ID.
	 * @param imageID The image to display, IDs are assigned as objects are added, starting from 0.
	 */
	public void showImage(int imageID){
		for(DynamicImage i : images)
			i.setVisible(false);
		if(images.size()>imageID)
			images.get(imageID).setVisible(true);
	}

}
