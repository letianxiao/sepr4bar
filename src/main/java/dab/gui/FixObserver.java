package dab.gui;

/**
 * An observer that waits for ObservableFix classes 
 * to call update();
 * @author mb941
 *
 */
public interface FixObserver {
	
	public void update();

}
