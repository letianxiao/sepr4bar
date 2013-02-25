package dab.gui;

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
    private int maximum;
    private int counter;
    private boolean fixed;

    /**
     * 
     * @param coordX X coordinate to initialise the button at
     * @param coordY Y coordinate to initialise the button at
     * @param initialCounter the starting amount of clicks required to fix a component.
     */
    public FixButton(int coordX, int coordY, int initialCounter)
    {
    	observers = new ArrayList<FixObserver>();
        this.maximum = initialCounter;
        this.counter = initialCounter;
        this.fixed = false;
        
        setBounds(coordX,coordY,87,30);
        setVisible(false);
        this.setIcon(new ImageIcon("resources/mainInterface/wrench.png"));
        this.setBackground(Color.WHITE);
        setText(""+initialCounter);
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
    	fixed = false;
    	setVisible(true);
    }
    
    /**
     * Decrements the press requirement when the button is pressed,
     * also checks if the button has hit the number of required clicks]
     * and updates accordingly
     */
    protected void pressed(){
    	counter--;
    	if (counter <= 0){
    		maximum += 2;
    		counter = maximum;
    		fixed = true;
    		setVisible(false);
    	}	
    	setText(""+counter);
    }
    
    /**
     * 
     * @return Whether the button has hit the click requirement
     */
    public boolean getFixed(){
    	return fixed;
    }

}
