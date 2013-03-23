package dab.bigBunny;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class BunnyController {

    private int rotation;
    private double x, y, speed, boundX, boundY;
    private boolean forward, rotateLeft, rotateRight, braking;
    private final int rotationAmount = 5;
    private final double defAcceleration = 0.4;
    private final double breakingAcceleration = -1;
    private final double noramlStopping = -0.5;
    private Environment environment;
    private int radius;
    private int health;
    private Rectangle bounds, hitBounds;

    BunnyController(Environment environment, int radius) {
        x = 100;
        y = 100;
        rotation = 0;
        forward = false;
        rotateLeft = false;
        rotateRight = false;
        speed = 1;
        braking = false;
        this.environment = environment;
        this.radius = radius;
        health = 100;
    }

    public void step() {
        moveForward(forward);
        doRotateRight(rotateRight);
        doRotateLeft(rotateLeft);
    }

    public void startForward() { forward = true; }

    public void stopForward() { forward = false; }

    public void startRotateLeft() { rotateLeft = true; }

    public void stopRotateLeft() { rotateLeft = false; }

    public void startRotateRight() { rotateRight = true; }

    public void stopRotateRight() { rotateRight = false; }

    public void doRotateLeft(boolean left) {
        if (left) { 
            rotation = (rotation - rotationAmount)% 360;
            if (rotation < 0) {
                rotation += 360;
            }
        }
    }

    public void doRotateRight(boolean right) {
        if (right) {
            rotation = (rotation + rotationAmount) % 360;
        }
    }

    //Do not Go outside the game!!
    public void moveForward(boolean forw) {
        double acceleration, x0, y0;
        if (forward && !braking) {
            acceleration = defAcceleration;
        } else if (braking) {
            acceleration = breakingAcceleration;
        } else {
            acceleration = noramlStopping;
        }
        
        Slime intersected = environment.intersectWithSlime(new Point(getX(), getY()), radius);
        if (intersected != null) {
           // System.out.println("Intersected with " + intersected.toString());
            // BAD CODE!!!
            if (speed > 2) {
                acceleration = -1 * (intersected.getFreshness() + 0.5);
            } else {
                acceleration /= 2;
            }
        }        
       // System.out.println(String.format("speed: %f, acc: %f", speed, acceleration));     
        speed += acceleration;
        if (speed < 0) {
            speed = 0;
        }
        x0 = x;
        y0 = y;
        
        x += speed * Math.cos(Math.toRadians(rotation));
        y += speed * Math.sin(Math.toRadians(rotation));     
        
        //to check if intersectig with something - be that bounds or components
        checkInBounds(new Point2D.Double(x,y) ); 
        chekIntersects(x0, y0);
             
    }

    public void startBrake() { braking = true; }

    public void stopBrake() { braking = false; }

    public void hasBeenShot(){
        health --;
        System.out.println(health);
        if(health <= 0){
            
           //Nice animation of dying bunny, and gameover
       
        }
    }
    
    private void checkInBounds(Point2D.Double point){
        double a,b;
        a= point.getX();
        b= point.getY();
  
        if(!bounds.contains(a,b)){
           speed = defAcceleration;           
            
            if(bounds.getMinX() > a){
                x = bounds.getMinX();
            }
            else if (bounds.getMaxX() < a){
                x = bounds.getMaxX();
            }
            
            if(bounds.getMinY() > b){
                y = bounds.getMinY();
            }
            else if (bounds.getMaxY() < b){
                y = bounds.getMaxY();
            }
        }     
    }
    
    public void chekIntersects(double a, double b){
        double x0, y0, x1, y1, x2, y2, halfHeight, halfWidth;
        x0 = a;
        y0 = b;
        //do I need to feed x and y to this method, or is it OK to just use them?
        //latter make these for multiple thingies
        x2 = hitBounds.getCenterX();                    
        y2 = hitBounds.getCenterY();
        halfHeight = hitBounds.getHeight() / 2;
        halfWidth = hitBounds.getWidth() / 2;
        
           
        
        if(hit(y2, x2, halfHeight, halfWidth)){ 
            //Break the component
            //do stuff with speed
            //Maybe bounce??
            speed = defAcceleration; 
            //give headake
            
            if (rotation == 0) {
                 handleHitFromLeft(x2, halfWidth);}
            else if(rotation == 90){
                handleHitFromAbove(y2, halfHeight);
            }
            else if (rotation == 180){
                handleHitFromRight(x2, halfWidth);
            }
            else if (rotation == 270){
                handleHitFromBelow(y2, halfHeight);
            }          
            else if(rotation<90) {                    
                handleTopLeft(x0,x2,y0,y2,halfHeight,halfWidth);                                        
            }
            else if(rotation < 180){
                handleTopRight(x0,x2,y0,y2,halfHeight,halfWidth);
            }
            else if(rotation < 270){
                handleBottomRight(x0,x2,y0,y2,halfHeight,halfWidth);
            }
            else {
                handleBottomLeft(x0,x2,y0,y2,halfHeight,halfWidth);
            }
        }
         
    }
    
    private void handleHitFromAbove(double y2, double halfHeight){
        y = y2 - halfHeight - radius;
    }
    
    private void handleHitFromLeft(double x2, double halfWidth){
        x= x2 - halfWidth - radius;
    }
    
    private void handleHitFromRight(double x2, double halfWidth){
        x = x2 + halfWidth + radius;
    }
    
    private void handleHitFromBelow(double y2, double halfHeight){
        y = y2 + halfHeight + radius;
    }
    
    private void handleTopLeft(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){
        
        //hits from above
        if(hitFromAbove(x0, x2, y0, y2, halfHeight, halfWidth)){
            y = y2 - halfHeight - radius;
            x = x0 + ((y-y0)*tangent(90-rotation));       
        }
        else{
            //hits from left
            x = x2 - halfWidth - radius;
            y = y0 + ((x-x0)* tangent(rotation));           
        }
    }
    
    private void handleTopRight(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){        
        if(hitFromAbove(x0, x2, y0, y2, halfHeight, halfWidth)){
            y = y2 - halfHeight - radius;
            x = x0 - ((y-y0)*tangent(rotation-90));
        }
        else{
            x = x2 + halfWidth + radius;
            y = y0 + ((x0-x)*tangent(180-rotation));  // jei minusinis rotation, tai ta pati funkcija tik su -
        }    
    }
    
    private void handleBottomLeft(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){
        if(hitFromBelow(x0, x2, y0, y2, halfHeight, halfWidth)){
           y = y2 + halfHeight + radius;
           x = x0 + (y0-y)*tangent(rotation-90);
        }
        else{
           x = x2 - halfWidth - radius;
           y = y0 - (x-x0)*tangent(360 - rotation);
        }
        
    }
    
    private void handleBottomRight(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){
        if(hitFromBelow(x0, x2, y0, y2, halfHeight, halfWidth)){
            y = y2 + radius + halfHeight;
            x = x0 - (y0-y)*tangent(270 - rotation);
        }
        else{           
            x = x2 + radius + halfWidth;
            y = y0 - (x0-x)*tangent(rotation-180);
        }
    }
    
    private boolean hit(double y2, double x2, double halfHeight, double halfWidth){
        return((hitOnHeight(y, y2,halfHeight))&&hitOnWidth(x, x2, halfWidth));
    }
    
    private boolean hitOnHeight(double theY, double y2, double halfHeight){
        return((theY > y2 - halfHeight - radius)&&(theY < y2 + halfHeight + radius));
    }
    
    private boolean hitOnWidth(double theX,double x2, double halfWidth){
        return((theX > x2 - halfWidth - radius)&&(theX < x2 + halfWidth + radius));
    }
        
    private boolean hitFromAbove(double x0, double x2,double y0, double y2, double halfHeight, double halfWidth) {
        double deltaY = (y2 - halfHeight - radius) - y0;
        double newX;
        newX = x0 + (deltaY * tangent(90-rotation));
        return (hitOnWidth(newX, x2, halfWidth));
    }
   
    private boolean hitFromBelow(double x0, double x2,double y0, double y2, double halfHeight, double halfWidth){
       double deltaY = y0 - (y2 + halfHeight + radius);
       double newX;
       newX = x0 + (deltaY * tangent(rotation-90));        
       return (hitOnWidth(newX, x2, halfWidth));
    }
    

    
     public void setHitBounds(Rectangle rectangle) {
        hitBounds = rectangle;
        System.out.println("centerX" +hitBounds.getCenterX());
    }
    
    public int getX() { return (int) x; }

    public int getY() { return (int) y; }
    
    public Point getCoordinates(){ return (new Point(getX(),getY())); }

    public int getRotation() { return rotation; }
    
    public int getHealth(){ return health; }
    
    public int getRadius(){ return radius; }
    
    public void setBounds(Rectangle rectangle) {
        bounds = rectangle;
    }    
    
    private double tangent(int alfa){
        return Math.tan(Math.toRadians(alfa));
    }
    
}
