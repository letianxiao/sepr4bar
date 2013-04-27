package dab.gui;

import dab.engine.simulator.CannotRepairException;
import dab.engine.simulator.FailableComponent;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Defines the FixButton abstractly, actionListeners are defined concretely elsewhere
 * @author Team Haddock
 *
 */
public abstract class FixButton extends JButton implements ObservableFix {

	private ArrayList<FixObserver> observers;
    private FailableComponent component;

    /**
     * 
     * @param coordX X coordinate to initialise the button at
     * @param coordY Y coordinate to initialise the button at
     * @param initialCounter the starting amount of clicks required to fix a component.
     */
    public FixButton(int coordX, int coordY,  FailableComponent component)
    {
    	observers = new ArrayList<FixObserver>();
        //this.maximum = initialCounter;
        //this.counter = initialCounter;
        //this.fixed = false;
        this.component = component;
       
        
        setBounds(coordX,coordY,87,30);
        setVisible(false);
        this.setIcon(new ImageIcon("resources/mainInterface/wrench.png"));
        this.setBackground(Color.WHITE);
        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);
    }
    
    /**
     * Attach a new observer to this class
     * @param observer the FixObserver to add.
     */
    public void attachFixObserver(FixObserver observer){
    	observers.add(observer);
    }
    
    /**
     * Notify all attached FixObservers with Observer.update()
     */
    protected void notifyFixObservers(){
    	for(FixObserver o : observers)
    		o.update();
    }
    
    /**
     * Sets the button to active
     */
    public void fail(){      
        setText(""+component.getDamage());
    	setVisible(true);
    }
    
    /**
     * Decrements the press requirement when the button is pressed,
     * also checks if the button has hit the number of required clicks]
     * and updates accordingly
     */
    protected void pressed() throws CannotRepairException{
    	component.fixingDamage();
        
        System.out.println("health" + component.getDamage());
        
    	if (!component.hasFailed()){
    		setVisible(false);
    	}	
    	setText(""+component.getDamage());
    }
    
    /**
     * 
     * @return Whether the button has hit the click requirement
     */
    public boolean getFixed(){
    	return component.getDamage()==0;
    }

}
