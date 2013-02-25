package dab.engine.simulator;

/**
 * Base class for exceptions thrown trying to control components
 * 
 * @author David
 */
public class ControlException extends Exception {

    public ControlException(String msg) {
        super(msg);
    }
}
