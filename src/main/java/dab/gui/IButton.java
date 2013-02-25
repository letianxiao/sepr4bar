package dab.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Abstract class defining the buttons used in ButtonFactory
 * @author mb941
 *
 */
public abstract class IButton extends JButton implements Observable {
	protected ButtonState state;
	protected int id;
	
	public abstract void updateState(ButtonState state);
	
	public abstract IButton createButton(int id);
	
	/**
	 * Gets the state of the button
	 * @return ButtonState the button currently holds
	 */
	public ButtonState getState(){
		return state;
	}
	
	/**
	 * Sets the button image to green (on)
	 */
	protected void setToGreen(){
        setIcon(new ImageIcon("resources/controlPanel/greenButton.png"));
    }
	
	/**
	 * Sets the button image to red (off)
	 */
	protected void setToRed(){
        setIcon(new ImageIcon("resources/controlPanel/redButton.png"));
    }
	
	/**
	 * Updates the icon accoding to current state.
	 */
	protected void updateIcon(){
		if(state==ButtonState.ON)
			setToGreen();
		else
			setToRed();
	}

}
