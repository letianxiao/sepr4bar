package dab.gui;

import dab.engine.simulator.UserCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class ValveButton extends IButton implements Observable{
	protected ArrayList<Observer> observers;
	
	static{
		ButtonFactory.instance().registerButton("Valve", new ValveButton(0));
	}
	
	public ValveButton(final int id){
		observers = new ArrayList<Observer>();
        setBorderPainted(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        updateState(ButtonState.ON);
        this.id = id;
        setContentAreaFilled(false);
        addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(state==ButtonState.ON){
        			updateState(ButtonState.OFF);
        			notifyObservers(UserCommands.CLOSE, id);
        		}else{
        			updateState(ButtonState.ON);
        			notifyObservers(UserCommands.OPEN, id);
        		}     		
        	}
        });
	}
		
	public void updateState(ButtonState state){
		if(state!=ButtonState.BROKEN){
			this.state = state;
			updateIcon();
		}
	}

	public ValveButton createButton(int id){
		return new ValveButton(id);
	}
	
	protected void notifyObservers(UserCommands command, int id){
		for(Observer o : observers)
			o.update(command, id);
	}

	
	public void attachObserver(Observer observer) {
		observers.add(observer);
	}
	
}
