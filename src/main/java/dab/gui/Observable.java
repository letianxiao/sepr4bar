package dab.gui;

/**
 * Objects that are observable call Observer.update() on all
 * objects watching them when iternally specified paramters are met
 * @author Team Haddock
 *
 */
public interface Observable {

	/**
	 * Attach the specified Observer to the class.
	 * @param observer Class implementing Observer
	 */
	public void attachObserver(Observer observer);
	
	//Should implement (although cannot enforce private methods in interfaces):
	//private void notifyObservers();
}
