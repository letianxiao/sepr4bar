package dab.bigBunny;

import java.awt.Point;
import java.awt.Rectangle;

public class BunnyController {
    // number of degrees to rotate per step
    private final int ROTATION_AMOUNT = 5;
    
    // default ammount to accelerate
    private final double DEF_ACCELERATION = 0.4;
    
    private final double BRAKING_ACCELERATION = -1;
    
    // ammount we slow down when nothing is happenning (it's basically usual drag)
    private final double NORMAL_STOPPING = -0.5;
    
    //if the bunny is sliding off the surface(not next to it, but in the direction of it),
    //its speed is decreased by this amount
    private final double FRICTION_SLOWDOWN = 3;
    
    //the angle at which the bunny slids, not bounces
    private final int SLIDE_ANGLE = 30;
    
    //if the bunny has lower speed then the counce speed, it starts sliding (rotating) by this
    private final int SLIDE_AMOUNT = 2;
    
    //the speed from which the bunny bouces of things
    private final int BOUNCE_SPEED = 2;
    
    //the amount of bouncing 
    private final double BOUNCE_AMOUNT = -0.5;
      
    private int orientation, tempOrientation, direction;
    private String spinDirection;
    private double x, y, speed,tempSpeed;
    private boolean movingForward, rotatingLeft, rotatingRight, braking;
    private boolean softwareFailure;

    private Environment environment;
    private int radius;
    private int health;
    private Rectangle bounds, hitBounds;
    
    

    /* 
     * BunnyController: Environment, position, size
    */    
    public BunnyController(Environment e, Point p, int size) {
        this.environment = e;
        this.radius = size;
        
        this.x = p.x;
        this.y = p.y;
        
        movingForward = false;
        braking = false;
        rotatingLeft = false;
        rotatingRight = false;
        
        orientation = 0;
        speed = 0;
        
        health = 100;
    }
    
    public BunnyController(Environment environment, int radius) {
        this(environment, new Point(100, 100), radius);
    }

    public void step() {
        softwareFailure = environment.getSoftwareFailure();
        //updateMovement();
        moveForward();
        doRotateLeft();
        doRotateRight();     
    }
    
    /*
     * calculate acceleration depending on the state
     * (running, braking, intersecting slime)
     * <<<<<<look at the acceleration computation below. That one is not finished either, but it has more stuff
     * than this>>>>>
     */
    
    private double computeAcceleration() {
        double acceleration;
        if (movingForward && !braking) {
            acceleration = DEF_ACCELERATION;
        } else if (braking) {
            acceleration = BRAKING_ACCELERATION;
        } else {
            acceleration = NORMAL_STOPPING;
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
    
    //<<<<<this is awesome, except for newX, as it would require a lot of refactoring of later things
    //but if you insist that x can't be readjusted a few times.. Go ahead :P Refactor :P I tried and broke stuff :D >>>>
    protected void updateMovement() {
        double acceleration = computeAcceleration();
        double newX, newY;
        
        // no not allow backwards movement
        //<<<<<Yes, allow backwards movements for awesome bouncing :P>>>>>>>>>
        speed += acceleration;
        if (speed < 0) {
            speed = 0;
        }

        // calculate predicted coordonates        
        newX = x + speed * Math.cos(Math.toRadians(orientation));
        newY = y + speed * Math.sin(Math.toRadians(orientation));  
        
        // check for solid collisions (including bounding box)
        
        // update orientation (orientation)
        // TODO: should we update the orientation first and then the coordonates?
        
        //<<<<<<<<<<<<<<This is BAD, orientation shouldnt go from -360 to 360. 
        //Why dont you want to call the methods I wrote, with SF from here? 
        //They're not pretty, could use improvements, but at least they're not
        //calculating it wrongly as those are..>>>>>>>>>>>>>>
        if (rotatingLeft)
            orientation = (orientation - ROTATION_AMOUNT) % 360;
        if (rotatingRight) 
            orientation = (orientation + ROTATION_AMOUNT) % 360;
        
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
    public void doRotateLeft() {
        if (rotatingLeft) { 
            if(!softwareFailure){ rotateLeft(); }
            else { rotateRight(); }
        }
    }

    /*
     * see {@link moveForward()} for reasons
     */
    @Deprecated
    public void doRotateRight() {
        if (rotatingRight) {
            if(!softwareFailure){ rotateRight(); }
            else { rotateLeft(); }
        }       
    }
    
    public void rotate(){
        if (rotatingLeft) { 
            if(!softwareFailure){ rotateLeft(); }
            else { rotateRight(); }
        }
        if (rotatingRight) {
            if(!softwareFailure){ rotateRight(); }
            else { rotateLeft(); }
        } 
    }
    
    private void rotateRight(){
        orientation = (orientation + ROTATION_AMOUNT) % 360;
    }
    
    private void rotateLeft(){
        orientation = (orientation - ROTATION_AMOUNT)% 360;
                if (orientation < 0) {
                    orientation += 360;
                }
    }

    /*
     * move bunny forward according to forward/braking state + slime slow down
     * reason for deprecating: 
     *   doesn't scale very well (need to call other functions for bound checking)
     *   it doesn't allow for easy changes in orientation 
     *     (think what happens if we colide with a component at a very small angle)
     * 
     * <<<<<<<Fixed the sliding, have no idea what the rest of the comment mean :(  
     * Do what you want, but please at least look at what I did first. Would appreciate if refactoring wouldnt
     * mean you rewriting stuff from scratch, I can fix my mistakes or you can fix them, but incomplete things
     * shouldnt be dissmissed only when they're incomplete :PPPPPP >>>>>>>>>>>
     * 
     * @deprecated use {@link updateMovement()} instead
     */
    //Do not Go outside the game!!
    @Deprecated
    public void moveForward() {
        double acceleration, x0, y0;
        if (movingForward && !braking) {
            acceleration = DEF_ACCELERATION;
        } else if (braking && (speed >0)) {
            acceleration = BRAKING_ACCELERATION;
        } else if(speed >0){
            acceleration = NORMAL_STOPPING;
        } else if (speed < 0){
            acceleration = -BRAKING_ACCELERATION;
        } else {acceleration = 0;}
        
       // System.out.println("speed "+ speed);
        
        Slime intersected = environment.intersectWithSlime(new Point(getX(), getY()), radius);
        if (intersected != null) {
           // System.out.println("Intersected with " + intersected.toString());
            // BAD CODE!!!
            if (speed > 2) {
                acceleration = -1 * (intersected.getFreshness() + 0.5);
            } else if (speed < -2){
                acceleration = intersected.getFreshness() + 0.5;
            } else  {
                acceleration /= 2;
            }
        }      
        
        x0 = x;
        y0 = y;
       // System.out.println(String.format("speed: %f, acc: %f", speed, acceleration));     
        speed += acceleration;
        if((speed>-1)&& speed<0){speed =0;}
        
        if(speed>=0) {
            tempOrientation = orientation;
            updateXY(tempOrientation);   
        } else {
            spin();
             updateXY(direction); 
        }
      
        //to check if intersectig with something - be that bounds or components
        checkInBounds(); 
        chekIntersects(x0, y0);
    }

      private void updateXY(int thisDirection){
        x += speed * Math.cos(Math.toRadians(thisDirection));
        y += speed * Math.sin(Math.toRadians(thisDirection));
    }
    
    
    
  
    
    //<<<<<geting weird stuff when trying to use strings in cases. Will fix it later, as lazy to google now 8)>>>>>
    private void checkInBounds(){

        if(!bounds.contains(x,y)){    
            if(bounds.getMinX() > x){
                x = bounds.getMinX();
                //adjustOrientation("right");
                adjustOrientation(2);
            } else if (bounds.getMaxX() < x){
                x = bounds.getMaxX();
               // adjustOrientation("left");
                adjustOrientation(0);
            }
            
            if(bounds.getMinY() > y){
                y = bounds.getMinY();
                //adjustOrientation("up");
                adjustOrientation(3);
            } else if (bounds.getMaxY() < y){
                y = bounds.getMaxY();
                //adjustOrientation("down");
                adjustOrientation(1);
            }        
        orientation=tempOrientation;
        }     
    }
    
    public void chekIntersects(double x0, double y0){
        double x2, y2, halfHeight, halfWidth;
        int thisDirection;
        
        //latter make these for multiple thingies
        x2 = hitBounds.getCenterX();                    
        y2 = hitBounds.getCenterY();
        halfHeight = hitBounds.getHeight() / 2;
        halfWidth = hitBounds.getWidth() / 2;
        
        /* 
         * first proposition checks if new coordinates of the bunny are inside the hitable object,
         * second one checks if the bunny jumps over the hitable object, i.e. the line between its
         * previous coordinates and the new ones intersects the object. 
         */
        if((hit(y2, x2, halfHeight, halfWidth))){
      
        //ToDo: use that with actual bounds, not the expanded ones. So that the bunny wouldn't jump over the thing
        //(hitBounds.intersectsLine(x0, y0, x, y))
            
            
            //Break the component         
            //give headake
           
           thisDirection = adjustThisDirection();
         
            
            if (thisDirection == 0) {
                handleHitFromLeft(x2, halfWidth);
            } else if(thisDirection == 90){
                handleHitFromAbove(y2, halfHeight);
            } else if (thisDirection == 180){
                handleHitFromRight(x2, halfWidth);
            } else if (thisDirection == 270){
                handleHitFromBelow(y2, halfHeight);
            } else if(thisDirection<90) {                    
                handleTopLeft(x0,x2,y0,y2,halfHeight,halfWidth);                                        
            } else if(thisDirection < 180){
                handleTopRight(x0,x2,y0,y2,halfHeight,halfWidth);
            } else if(thisDirection < 270){
                handleBottomRight(x0,x2,y0,y2,halfHeight,halfWidth);
            } else {
                handleBottomLeft(x0,x2,y0,y2,halfHeight,halfWidth);
            }
         orientation = tempOrientation;
        }        
    }
   
    
    /*set y to be at the upper border of the hittable object */
    private void handleHitFromAbove(double y2, double halfHeight){
        y = y2 - halfHeight;
        //adjustOrientation("down");
        adjustOrientation(1);
    }
    
    /*set x to be at the left border of the hittable object */
    private void handleHitFromLeft(double x2, double halfWidth){
        x = x2 - halfWidth;
        //adjustOrientation("left");
        adjustOrientation(0);
    }
    
    /*set x to be at the right border of the hittable object */
    private void handleHitFromRight(double x2, double halfWidth){
        x = x2 + halfWidth;
        //adjustOrientation("right");
        adjustOrientation(2);
    }
    
    /*set x to be at the bottom border of the hittable object */
    private void handleHitFromBelow(double y2, double halfHeight){
        y = y2 + halfHeight;
        //adjustOrientation("up");
        adjustOrientation(3);
    }
    
    /*check if the object is hit from above or from the side*/
    private void handleTopLeft(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){       
        if(hitFromAbove(x0, x2, y0, y2, halfHeight, halfWidth)){
            handleHitFromAbove(y2, halfHeight);
            adjustX(x0, y0);      
        } else{           
            handleHitFromLeft(x2, halfWidth);
            adjustY(x0, y0, 1);           
        }
    }
    
    /*check if the object is hit from above or from the side */
    private void handleTopRight(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){        
        if(hitFromAbove(x0, x2, y0, y2, halfHeight, halfWidth)){
            handleHitFromAbove(y2, halfHeight);
            adjustX(x0, y0);  
        } else{
            handleHitFromRight(x2, halfWidth);
            adjustY(x0, y0, -1);
        }    
    }
    
    /*check if the object is hit from bellow or from the side */
    private void handleBottomLeft(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){        
        if(hitFromBelow(x0, x2, y0, y2, halfHeight, halfWidth)){
           handleHitFromBelow(y2, halfHeight);
           adjustX(x0, y0);  
        } else{
           handleHitFromLeft(x2, halfWidth);
           adjustY(x0, y0, 1);
        }     
    }
    
    /*check if the object is hit from bellow or from the side */
    private void handleBottomRight(double x0, double x2, double y0, double y2, double halfHeight, double halfWidth){  
        if(hitFromBelow(x0, x2, y0, y2, halfHeight, halfWidth)){
            handleHitFromBelow(y2, halfHeight);
            adjustX(x0, y0);  
        } else{           
            handleHitFromRight(x2, halfWidth);
            adjustY(x0, y0, -1);
        }
    }
    
    /*a check that the circle(bunny) has hit the the hittable object */
    private boolean hit(double y2, double x2, double halfHeight, double halfWidth){
        return((hitOnHeight(y, y2,halfHeight))&&hitOnWidth(x, x2, halfWidth));
    }
    
    /*a check if the circle is between the height coordinates of the hittable object */
    private boolean hitOnHeight(double theY, double y2, double halfHeight){
        return ((theY > y2 - halfHeight)&&(theY < y2 + halfHeight));
    }
    
    /*a check if the circle is between the width coordinates of the hittable object */
    private boolean hitOnWidth(double theX,double x2, double halfWidth){
        return((theX > x2 - halfWidth)&&(theX < x2 + halfWidth));
    }
        
    /* 
     * A check if the bunny hits the object from above. It gets the coordinates of 
     * y at which the circle would have hit the object. Then it gets the coordinates of x
     * at that point, those depend on orientation of the bunny and current position. then it checks 
     * if the x is in the bounds of the width of object. If it is not - the object is still not hit
     * and would get hit from the side.
     */
    private boolean hitFromAbove(double x0, double x2,double y0, double y2, double halfHeight, double halfWidth) {
        double deltaY = (y2 - halfHeight) - y0;
        double newX;
        newX = x0 + (deltaY * tangent(90-orientation));
        return (hitOnWidth(newX, x2, halfWidth));
    }
   
    /*same as hitFromAbove, just checks for the thing from above */
    private boolean hitFromBelow(double x0, double x2,double y0, double y2, double halfHeight, double halfWidth){
       double deltaY = y0 - (y2 + halfHeight);
       double newX;
       newX = x0 + (deltaY * tangent(orientation-90));        
       return (hitOnWidth(newX, x2, halfWidth));
    }
    
    /* 
     * when object is hit from above or below, x for the place where it was hit is counted. 
     * it is counted using dX = dY * tan(alfa), where dX and dY is the difference between bunny starting point 
     * and hit-point (i.e. the edges of triangle), and alfa is the angle of the corner oposite to x.  
     */
    private void adjustX(double x0, double y0){
        x = x0 + ((y-y0)*tangent(90-orientation));
    }
    
    /* 
     * when object is hit from left or right, y for the place where it was hit is counted. 
     * it is counted using dY = dX * tan(alfa), where dX and dY is the difference between bunny starting point 
     * and hit-point (i.e. the edges of triangle), and alfa is the angle of the corner oposite to y. i is either 1
     * or -1 used only for angle direction adjustment. 
     */
    private void adjustY(double x0, double y0, int i){
        y = y0 + ((x-x0)* tangent(orientation*i));    
    }
    
    private void adjustOrientation(int headingFrom){
        int thisDirection;
        boolean wouldBounce = (speed > BOUNCE_SPEED || speed< -BOUNCE_SPEED);
        //System.out.println("speed " + speed + " wouldBounce " + wouldBounce);
               
        thisDirection = adjustThisDirection();

        switch(headingFrom){
            case 0:
                if((90-thisDirection<SLIDE_ANGLE)&&(90-thisDirection >0)){
                    tempOrientation = 90;
                } else if ((thisDirection-270<SLIDE_ANGLE)&&(thisDirection-270>0)){
                    tempOrientation = 270;
                } else if(wouldBounce){
                    startSpin(0,thisDirection);
                } else if(thisDirection>270){
                    slide(-1);                 
                } else {
                    slide(1);
                }
                break;
            case 1: 
                if(thisDirection<SLIDE_ANGLE){
                    tempOrientation =0;
                } else if (thisDirection > 180-SLIDE_ANGLE){
                    tempOrientation = 180;
                } else if (wouldBounce) {
                   startSpin(1, thisDirection);
                } else if(thisDirection>90){
                    slide(1);
                } else {
                    slide(-1);
                }
                break;
            case 2:
                if(thisDirection>270-SLIDE_ANGLE){
                    tempOrientation = 270;
                } else if (thisDirection < 90 + SLIDE_ANGLE) {
                    tempOrientation = 90;
                } else if (wouldBounce) {
                    startSpin(2, thisDirection);
                } else if(thisDirection>180){
                    slide(1);              
                } else {
                    slide(-1); 
                }
                break;   
            case 3:
                if(thisDirection-180<SLIDE_ANGLE){
                    tempOrientation = 180;
                } else if(360 - thisDirection < SLIDE_ANGLE){
                    tempOrientation = 0;
                } else if (wouldBounce) {
                   startSpin(3,thisDirection);
                } else if(thisDirection>270){                   
                    slide(1);
                } else{
                    slide(-1);
                }
                break;          
        }
        
        if (speed > FRICTION_SLOWDOWN) {
            speed -= FRICTION_SLOWDOWN ;
        }
    }
 
    private void startSpin(int headingFrom, int thisDirection){
        switch(headingFrom) {
            case 0:
                direction = (360-thisDirection)%360;
                    if(thisDirection>270){                           
                        spinDirection = "right";
                    } else {
                        spinDirection = "left";
                    }  
            break;
            case 1:
                 direction = 90 + 90 - thisDirection;
                    if(thisDirection<90) {                        
                        spinDirection = "right";
                    } else {
                        spinDirection = "left";
                    }
            break;
            case 2:
                direction = 180 + 180 - thisDirection;
                    if(thisDirection<180){                                   
                        spinDirection = "right";
                    } else {
                        spinDirection = "left";
                    }
            break;
            case 3:
                 direction = 270 + (270 - thisDirection);
                    if(thisDirection<270){                            
                        spinDirection="right";
                    } else {
                        spinDirection = "left";
                    }
           break;
        }                 
 
        tempSpeed =speed;
        
        if (speed > 0) {
            speed = BOUNCE_AMOUNT * speed;
        } else {
            speed = BOUNCE_AMOUNT * speed * (-1);
        }
        
    }
    
    //<<<<Play around with numbers to make for the most fun bouncing.>>>>>   
    private void spin(){
        double spinCoef;
        int angle;
        double a,b,c,d;
        a=0.0001;        
        b=0.4;
        c= -1.4;
        d = 5;
       
        double speedCoef = b*tempSpeed*tempSpeed +c*tempSpeed + d;
     
        if(spinDirection.equalsIgnoreCase("left")){
            angle = (90- direction%90)%90;
            spinCoef = a*angle*angle ;
            orientation -= speedCoef*spinCoef;
       } else {
            angle = direction%90;
            spinCoef = a*angle*angle ;
           orientation += speedCoef*spinCoef;
       }       
       System.out.println("angle: " + angle + " spincoef: " + spinCoef + " speed: " + speed); 
    }
    
    private void slide(int i){
        tempOrientation = orientation + (i * SLIDE_AMOUNT);
    }
    
    private int adjustThisDirection(){
        if(speed <0) {
               return (180 + direction)%360;
           } else {
               return orientation;
           }
    }
    
    public void setHitBounds(Rectangle rectangle) {
        int newX = (int)(rectangle.getMinX() - radius);
        int newY = (int)(rectangle.getMinY() - radius);
        int newWidth = (int)(rectangle.getWidth() + radius + radius);
        int newHeight = (int) (rectangle.getHeight() + radius + radius);
        hitBounds = new Rectangle(newX, newY, newWidth, newHeight);        
    }
        
    public void hasBeenShot(){
        health --;
        System.out.println(health);
        if (health <= 0){
            
           //Nice animation of dying bunny, and gameover
       
        }
    }
    
    public int getX() { return (int) x; }

    public int getY() { return (int) y; }
    
    public Point getCoordinates() { return (new Point(getX(), getY())); }
    
    public int getOrientation()   { return orientation; }
    
    public int getHealth()        { return health; }
    
    /*
     * why would we need to get back the radius, it doesn't change does it?
     */
    @Deprecated
    public int getRadius()        { return radius; }

    public void setBounds(Rectangle rectangle) {
        bounds = rectangle;
    }    

    private double tangent(int alfa){
        return Math.tan(Math.toRadians(alfa));
    }
}
