/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author eduard
 */
public class Environment {
    private LinkedList<Slime> slimes;
    private int width;
    private int height;
    
    
    public Environment(int width, int height) {
        slimes = new LinkedList<Slime>();
        this.width = width;
        this.height = height;
    }
    
    // idea: keep them sorted them by freshness
    public void step() {
        Random rnd = new Random();
        if (rnd.nextDouble() < 0.01 && slimes.size() < 10) { // 1% chance
            int x = rnd.nextInt(width);
            int y = rnd.nextInt(height);
            slimes.addFirst(new Slime(x, y, 3000));
            System.out.println("new slime " + x + " " + y);
        }
            
        
        // FIXME: slow as fuck
        while (!slimes.isEmpty() && slimes.getLast().getFreshness() == 0) {
            slimes.removeLast();
        }
        
        for (Slime s : slimes) {
            s.step();
        }
        
    }
    
    // return the newest one that intersects
    public Slime intersectWithSlime(Point p, int radius) {
        for (Slime s : slimes) {
            int sqdistance = (int)p.distanceSq(s.getLocation());
            int sqSumRadius = (radius + s.getRadius()) * (radius + s.getRadius());
            if (sqdistance < sqSumRadius) // they intersect
                return s;
        }
        return null;       
    }
    
    // NOTE: return arrayList;
    public LinkedList<Slime> getSlimes() {
        return slimes;
    }
}