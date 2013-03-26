package dab.bigBunny;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class BunnyController {
    // number of degrees to rotate per step
    private final int rotationAmount = 5;
    
    // default ammount to accelerate
    private final double defAcceleration = 0.4;
    
    private final double breakingAcceleration = -1;
    
    // ammount we slow down when nothing is happenning (it's basically usual drag)
    private final double normalStopping = -0.5;
    
    private int rotation;
    private double x, y, speed, boundX, boundY;
    private boolean movingForward, rotatingLeft, rotatingRight, braking;

    private Environment environment;
    private int radius;
    private int health;
    private Rectangle bounds, hitBounds;

    /* 
     * BunnyController: Environment, position, size
    
    */
    
    BunnyController(Environment e, Point p, int size) {
        this.environment = e;
        this.radius = size;
        
        this.x = p.x;
        this.y = p.y;
        
        movingForward = false;
        braking = false;
        rotatingLeft = false;
        rotatingRight = false;
        
        rotation = 0;
        speed = 0;
        health = 100;
    }
    
    BunnyController(Environment environment, int radius) {
        this(environment, new Point(100, 100), radius);
    }

    public void step() {
        updateMovement();
        //moveForward(movingForward);
        //doRotateLeft(rotatingLeft);
        //doRotateRight(rotatingRight);
    }
    
    /*
     * calculate acceleration depending on the state
     * (running, braking, intersecting slime)
     */
    private double computeAcceleration() {
        double acceleration;
        if (movingForward && !braking) {
            acceleration = defAcceleration;
        } else if (braking) {
            acceleration = breakingAcceleration;
        } else {
            acceleration = normalStopping;
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
        
        return acceleration;
    }
    
    protected void updateMovement() {
        double acceleration = computeAcceleration();
        double newX, newY;
        
        // no not allow backwards movement
        speed += acceleration;
        if (speed < 0) {
            speed = 0;
        }

        // calculate predicted coordonates
        newX = x + speed * Math.cos(Math.toRadians(rotation));
        newY = y + speed * Math.sin(Math.toRadians(rotation));  
        
        // check for solid collisions (including bounding box)
        
        // update orientation (rotation)
        // TODO: should we update the rotation first and then the coordonates?
        if (rotatingLeft)
            rotation = (rotation - rotationAmount) % 360;
        if (rotatingRight) 
            rotation = (rotation + rotationAmount) % 360;
        
        // calculate new coordonates
        x = newX;
        y = newY;
    }

    public void startForward()     { movingForward = true;  }
    public void stopForward()      { movingForward = false; }
    
    public void startBrake()       { braking       = true;  }
    public void stopBrake()        { braking       = false; }

    public void startRotateLeft()  { rotatingLeft  = true;  }
    public void stopRotateLeft()   { rotatingLeft  = false; }

    public void startRotateRight() { rotatingRight = true;  }
    public void stopRotateRight()  { rotatingRight = false; }

    /*
     * see {@link moveForward()} for reasons
     */
    @Deprecated
    public void doRotateLeft(boolean left) {
        if (left) { 
            rotation = (rotation - rotationAmount) % 360;
        }
    }

    /*
     * see {@link moveForward()} for reasons
     */
    @Deprecated
    public void doRotateRight(boolean right) {
        if (right) {
            rotation = (rotation + rotationAmount) % 360;
        }
    }

    /*
     * move bunny forward according to forward/braking state + slime slow down
     * reason for deprecating: 
     *   doesn't scale very well (need to call other functions for bound checking)
     *   it doesn't allow for easy changes in rotation 
     *     (think what happens if we colide with a component at a very small angle)
     * 
     * @deprecated use {@link updateMovement()} instead
     */
    //Do not Go outside the game!!
    @Deprecated
    public void moveForward(boolean forw) {
        double acceleration;
        if (movingForward && !braking) {
            acceleration = defAcceleration;
        } else if (braking) {
            acceleration = breakingAcceleration;
        } else {
            acceleration = normalStopping;
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
        x += speed * Math.cos(Math.toRadians(rotation));
        y += speed * Math.sin(Math.toRadians(rotation));     
        
        //to check if intersectig with something - be that bounds or components
        checkInBounds(  new Point2D.Double(x,y) ); 
        chekIntersects( new Point2D.Double(x,y) );
             
    }



    public void hasBeenShot(){
        health --;
        System.out.println(health);
        if (health <= 0){
            
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
    
    public void chekIntersects(Point2D.Double point){
        double x1, y1, x2, y2;
        x1= point.getX();
        y1= point.getY();
        
        y2 = hitBounds.getCenterY();
        
        // ToDo Write a check for X and a check if doesn't jump through
        if((y1 - radius < y2 + (hitBounds.height/2)) && (y1 + radius > y2 - (hitBounds.height/2))){
            System.out.println("Hit wyyy");
            if(y1<y2){
                y = y2- (hitBounds.height/2);
            }
        }
        
      
    }
    
    
     public void setHitBounds(Rectangle rectangle) {
        hitBounds = rectangle;
        System.out.println("centerX" +hitBounds.getCenterX());
    }
    
    public int getX()             { return (int) x; }
    public int getY()             { return (int) y; }
    
    public Point getCoordinates() { return (new Point(getX(), getY())); }

    /*
     * A rotation is a circular *movement* of an object around a center (or point) of rotation 
     * source: wikipedia
     * 
     * @deprecated use {@link getOrientation()} instead
     */
    @Deprecated
    public int getRotation()      { return getOrientation(); }
    
    public int getOrientation()   { return rotation; }
    
    public int getHealth()        { return health; }
    
    /*
     * why would we need to get back the radius, it doesn't change does it?
     */
    @Deprecated
    public int getRadius()        { return radius; }
    
    public void setBounds(Rectangle rectangle) {
        bounds = rectangle;
    }    
}
