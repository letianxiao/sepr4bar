/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.awt.Point;

/**
 *
 * @author Aiste
 */
public class BulletHole extends TemporaryObject{
    private static int timeToBeSeen = 100;
    
    public BulletHole(Point point) {
        super(point, timeToBeSeen);
    }
 
    /**
     * 
     * @return if bullet is visible
     * @deprecated use isAlive() instead
     */
    @Deprecated
    public boolean getIsVisible() { return isAlive();}
}
