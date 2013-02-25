package dab.gui;

/**
 * 
 * Objects that are observable call FixObserver.update() on all
 * objects watching them when iternally specified paramters are met
 * @author Team Haddock
 *
 */

public interface ObservableFix {
	
	/**
	 * Attach the specified FixObserver to the class.
	 * @param observer Class implementing FixObserver
	 */
	public void attachFixObserver(FixObserver observer);
	
	//Should implement (although cannot enforce private methods in interfaces):
	//public void notifyObservers();

}
