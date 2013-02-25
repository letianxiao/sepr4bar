package dab.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Button Factory creates new objects of type IButton.
 * 
 * @author Team Haddock
 *
 */
public class ButtonFactory {
	private static ButtonFactory instance;
	private HashMap<String, IButton> registeredButtons = new HashMap<String, IButton>();

	/**
	 * Singleton fetch
	 * @return The singleton of this object
	 */
	public static ButtonFactory instance(){
		if(instance==null)
			instance=new ButtonFactory();
		return instance;
	}
	
	/**
	 * Registers a new button type with the factory for production
	 * @param ID The string containing the registered ID of a button type
	 * @param button an instance of the button type to be registered, implementing IButton
	 */
	public void registerButton(String ID, IButton button){
		registeredButtons.put(ID, button);
	}

	/**
	 * Produces a new button of the type associated with the buttonID string.
	 * @param buttonID String containing the name of the desired button
	 * @param id ID for the button to use.
	 * @return a new button object which implements IButton.
	 */
	public IButton createButton(String buttonID, int id){
			return (IButton)registeredButtons.get(buttonID).createButton(id);
	}

}
