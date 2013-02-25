package dab.gui;

import dab.engine.simulator.UserCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

/**
 * Class extending IButton used to control Pumps in the game.
 * @author Team Haddock
 *
 */
public class PumpButton extends IButton implements Observable {
	protected ArrayList<Observer> observers;
	
	//register this button with the factory.
	static{
		ButtonFactory.instance().registerButton("Pump", new PumpButton(0));
	}
	
	/**
	 * Create a new PumpButton, setting its ID as specified
	 * @param id ID to set.
	 */
	private PumpButton(final int id){
		observers = new ArrayList<Observer>();
        setBorderPainted(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        updateState(ButtonState.ON);
        this.id = id;
        setContentAreaFilled(false);
        addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(state!=ButtonState.BROKEN){
        			if(state==ButtonState.ON){
        				updateState(ButtonState.OFF);
        				notifyObservers(UserCommands.TURNOFF, id);
        			}else{
        				updateState(ButtonState.ON);
        				notifyObservers(UserCommands.TURNON, id);
        			}}       		
        	}
        });
	}
	
	/**
	 * Sets the image to grey(component broken)
	 */
	private void setToGrey(){
        setIcon(new ImageIcon("resources/controlPanel/greyButton.png"));
    }
	
	protected void notifyObservers(UserCommands command, int id){
		for(Observer o : observers)
			o.update(command, id);
	}
	
	public void attachObserver(Observer observer) {
		observers.add(observer);
	}
	
	public void updateState(ButtonState state){
		this.state = state;
		updateIcon();
	}
	
	/**
	 * Update the icon according to internal state
	 */
	protected void updateIcon(){
		if(state==ButtonState.BROKEN){
			setToGrey();
		}else
		if(state==ButtonState.ON)
			setToGreen();
		else
			setToRed();
	}
	
	/**
	 * Creates a new PumpButton with the specified id
	 */
	public PumpButton createButton(int id){
		return new PumpButton(id);
	}
	
}
