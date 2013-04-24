/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author eduard
 */
public class Environment {
    //private LinkedList<Slime> slimes;
    private int width;
    private int height;
    //private LinkedList<Bullets> bullets;
    
    private TemporaryObjectList<Slime> slimes;
    private TemporaryObjectList<BulletHole> bullets;
    
    private boolean softwareFailure;
    private final int DEFAULT_FAILURE_TIME = 100;
    private final int DEFAULT_CANT_FAIL = 300;
    private int sfTime, sfCantFailTime;  
    
    public Environment(int width, int height) {
        slimes = new TemporaryObjectList<Slime>();
        bullets = new TemporaryObjectList<BulletHole>();
        this.width = width;
        this.height = height;
        softwareFailure = false;
        sfTime = 0;
        sfCantFailTime = 0;
    }
    
    // idea: keep them sorted them by freshness
    public void step() {
        stepSoftwareFailure();
        Random rnd = new Random();
        if (rnd.nextDouble() < 0.01 && slimes.size() < 10) { // 1% chance
            int x = rnd.nextInt(width);
            int y = rnd.nextInt(height);
            slimes.add(new Slime(x, y, 3000));
            System.out.println("new slime " + x + " " + y);
        }
        slimes.step();
        bullets.step();
        
    }
    
    // return the newest one that intersects
    public Slime intersectWithSlime(Point p, int radius) {
        for (Slime s : slimes.getRawList()) {
            int sqdistance = (int)p.distanceSq(s.getLocation());
            int sqSumRadius = (radius + s.getRadius()) * (radius + s.getRadius());
            if (sqdistance < sqSumRadius) // they intersect
                return s;
        }
        return null;       
    }
    
    // NOTE: return arrayList;
    public LinkedList<Slime> getSlimes() {
        return slimes.getRawList();
    }
    
    public void addBullet(Point location){
        bullets.add(new BulletHole(location));
    }
    
    //CopyPasting from Slime. Bad. Sorry. 
    public LinkedList<BulletHole> getBullets(){
        return bullets.getRawList();
    }
    
    public void startSoftwareFailure() {
        if(sfCantFailTime<=0){
            softwareFailure = true;
            sfTime = DEFAULT_FAILURE_TIME;
            sfCantFailTime = DEFAULT_CANT_FAIL;
            //call software failure in the powerplant
        }
    } 
    
    private void stepSoftwareFailure(){       
        if(softwareFailure) {
            sfTime --;
            if(sfTime <=0){
               softwareFailure = false; 
            }
        }
        if(sfCantFailTime>0) {sfCantFailTime --;}
    }
    
    public boolean getSoftwareFailure(){
        return softwareFailure;
    }
}
