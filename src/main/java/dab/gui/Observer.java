package dab.gui;

import dab.engine.simulator.UserCommands;

/**
 * Observers watch Observables and have update() called when
 * internal parameters in the observables are met.
 * @author Team Haddock
 *
 */
public interface Observer {
	
	/**
	 * update the internal state of this observer
	 * @param command UserCommands called to cause this update call
	 * @param parameter Paramter used to cause this update call
	 */
	public void update(UserCommands command, int parameter);

}
