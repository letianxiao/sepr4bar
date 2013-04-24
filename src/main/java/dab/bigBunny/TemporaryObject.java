/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.awt.Point;

/**
 *
 * @author eduard
 */
public abstract class TemporaryObject {
    protected Point location;
    protected int remainingTimeToLive;
    
    public TemporaryObject(Point location, int ttl) {
        this.location = location;
        this.remainingTimeToLive = ttl;
    }
    
    public void step() {
        if (remainingTimeToLive > 0)
            remainingTimeToLive--;
    }
    
    public Point getLocation() { return location; }
    public boolean isAlive()   { return remainingTimeToLive > 0; }
    
}
