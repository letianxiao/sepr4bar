/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author eduard
 */
public class Slime {
    
    private static final int radius = 50;
    private Point location;
    private int remainingTimeToLive;
    private int initialTimeToLive;
    
    public Slime(int x, int y, int ttl) {
        this(new Point(x, y), ttl);
    }
    
    public Slime(Point p, int ttl) {
        this.location = p;
        this.remainingTimeToLive = ttl;
        this.initialTimeToLive = ttl;
    }
    
    public void step() {
        if (remainingTimeToLive > 0)
            remainingTimeToLive--;
    }
    
    public Point getLocation()      { return location; }
    public int   getRadius() { return radius; }
    
    /*
     * return 0 if we have faded away
     *        1 if we have just been created
     */
    public double getFreshness() {
        return (double)remainingTimeToLive / (double)initialTimeToLive;
    }
    
    public String toString() {
        return String.format("fresh: %f, X: %f, Y: %f", getFreshness(), location.getX(), location.getY());
    }
}
