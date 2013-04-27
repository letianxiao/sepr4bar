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
public class Bullets {
    private Point location;
    private final int timeToBeSeen = 100;
    private int toBeVisible;
    
    public Bullets(Point point){
        location = point;
        toBeVisible = timeToBeSeen;
        System.out.println("bullet point" + location);
    }
    
    public void step(){
        if(toBeVisible > 0){
            toBeVisible --;
        }
    }
    
    public Point getLocation() { return location;}
 
    public boolean getIsVisible() { return (toBeVisible>0);}
}
