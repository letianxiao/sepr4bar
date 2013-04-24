/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.util.ArrayList;

/**
 *
 * @author Aiste
 */
public class HitBoundsController {
    private ArrayList<HitBounds> hittableComponents;
    
    public HitBoundsController() {
        hittableComponents = new ArrayList<HitBounds>();
    }
    
    public void addHitBounds(TheRectangle rectangle){
        hittableComponents.add(rectangle);
    }
    
    public void addHitBounds(Circle circle){
        hittableComponents.add(circle);
    }
    
   public ArrayList<HitBounds> getHittableComponents(){
       return hittableComponents;
   }
}
