package dab.engine.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import dab.engine.utilities.Percentage;

/**
 * Base Class for all components which can fail.
 * 
 * FailableComponents maintain their state of repair and their wear.
 * 
 * Wear is accumulated by the template method stepWear; particular
 * FailableComponents should implement an appropriate calculateWearDelta
 * function to provide the wear characteristic of that component.
 * 
 * Components which can fail but which do not suffer from wear
 * (such as the reactor) can provide a calculateWearDelta function which
 * always returns a Percentage of zero.
 * 
 * @author Marius Dumetrescu
 */
public abstract class FailableComponent {

    @JsonProperty
    private boolean hasFailed;      //The state of the component
    @JsonProperty
    private Percentage wear;                //Current wear level - capped at 100%

    private int damage;
    private int maxDamage;  //this should be different for two player mode
    private final int INITIAL_DAMAGE = 5;   //this should be different for two player mode
    
    /**
     * Constructor for the FailableComponent. Sets default percentage to 0 and a normal FailureState
     */
    public FailableComponent() {
        //Initialize to a normal state
        hasFailed = false;
        wear = new Percentage(0);
        damage = 0;
        maxDamage = INITIAL_DAMAGE;  
    }

    public int getDamage(){
        return damage;
    }
    
    public void fixingDamage() throws CannotRepairException{
        damage --;
        if (damage <= 0 ){
            maxDamage += 2;         //this should be different for two player mode
            damage = 0;
            repair();
        }
    }
    
    /**
     *  @return hasFailed boolean
     */
    public boolean hasFailed() {
        return hasFailed;
    }

    /**
     * set hasFailed to true
     */
    public void fail() {
        fail(0);
    }

    public void fail(int i){
        hasFailed = true;
        stepWear();       
        damage += i + maxDamage;
        
        System.out.println("damage" + damage);
    }

    /**
     * set hasFailed to false
     */
    public void repair() throws CannotRepairException {
        hasFailed = false;
    }

    /**
     * CalculateWearDelta must be overridden by any child classes. It should be used to calculate a minute change in
     * wear level for the component to be added onto it. This is normally calculated within a method's step() routine.
     *
     * @return Percentage. The number of percentage points to add to the current wear level.
     */
    protected abstract Percentage calculateWearDelta();

    /**
     * Returns the component's current wear level as a percentage.
     *
     * @return The component's wear level.
     */
    public Percentage wear() {
        return wear;
    }

    /**
     * stepWear will increase the wear of the component from a delta value. The value will be capped at 100 and 0
     *
     * @param delta The number of percentage points to increase the FailableComponent's wear level by between 0 and 100
     */
    public void stepWear() {
        Percentage wearDelta = calculateWearDelta();
        if ((wear.points() + wearDelta.points()) < 100) {
            wear = wear.plus(wearDelta);
        } else {
            wear = new Percentage(100);     //Cap at 100%
        }
        if (wearDelta.points() == 0) {
            wear = new Percentage(0);        //Cap at 0%
        }
    }
}
