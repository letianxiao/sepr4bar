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
    private LinkedList<Bullets> bullets;
    
    public Environment(int width, int height) {
        slimes = new LinkedList<Slime>();
        bullets = new LinkedList<Bullets>();
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
        
        //CopyPasting from Slime. Bad. Sorry. 
        while(!bullets.isEmpty() && !bullets.getFirst().getIsVisible()){
            bullets.removeFirst();
        }
        
        for (Bullets b : bullets) { b.step(); }
        
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
    
    public void addBullet(Point location){
        bullets.add(new Bullets(location));
    }
    
    //CopyPasting from Slime. Bad. Sorry. 
    public LinkedList<Bullets> getBullets(){
        return bullets;
    }
    
}
