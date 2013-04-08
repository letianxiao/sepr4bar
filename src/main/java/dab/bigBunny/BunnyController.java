package dab.bigBunny;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

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
    private final int BOUNCE_SPEED = 4;
    
    //the amount of bouncing 
    private final double BOUNCE_AMOUNT = -1;  //-0.5
      
    private int orientation, tempOrientation, direction,perpendicularAngle;
    private String spinDirection;
    private double x, y, speed;
    private boolean movingForward, rotatingLeft, rotatingRight, braking;
    private boolean softwareFailure;

    private Environment environment;
    private int radius;
    private int health;
    private Rectangle bounds, hitBounds;
    
    private Ellipse2D.Double hitableCircle;
    
    

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
        
        orientation = 0;
        speed = 0;
        
        health = 100;           
    }
    
    BunnyController(Environment environment, int radius) {
        this(environment, new Point(100, 100), radius);
    }

    public void step() {
        softwareFailure = environment.getSoftwareFailure();
        updateMovement();    
    }
    
    /*
     * calculate acceleration depending on the state
     * (running, braking, intersecting slime)
     */    
    private double computeAcceleration() {
        double acceleration;
       if (movingForward && !braking && speed >=0) {
            acceleration = DEF_ACCELERATION;
        } else if (braking) {
            acceleration = BRAKING_ACCELERATION * Math.signum(speed);   //math.signums - if moving backwards control
        } else if (movingForward && speed <0) {
            acceleration = -NORMAL_STOPPING + DEF_ACCELERATION;
        } else { 
            acceleration = NORMAL_STOPPING * Math.signum(speed);        
        } 
        
       //ToDo: after this is fixed, add for the backwards speed control
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
        Point2D.Double newLocation;
               
        speed += acceleration;
        if((speed>-1)&& speed<0){speed =0;}
        
        if(speed>=0) {
            tempOrientation = orientation;
            newLocation = updateXY(orientation);   
        } else {                    
            newLocation = updateXY(direction); 
            spin(); 
        }
     
      
        //to check if intersectig with something - be that bounds or components
        newLocation = checkInBounds(newLocation); 
        //checkIntersectsSquare(newLocation.getX(), newLocation.get);
        newLocation = checkIntersectsCircle(newLocation);
        
        rotate();
        
        // calculate new coordonates
        x = newLocation.getX();
        y = newLocation.getY();
    }

    public void startForward()     { movingForward = true;  }
    public void stopForward()      { movingForward = false; }
    
    public void startBrake()       { braking       = true;  }
    public void stopBrake()        { braking       = false; }

    public void startRotateLeft()  { rotatingLeft  = true;  }
    public void stopRotateLeft()   { rotatingLeft  = false; }

    public void startRotateRight() { rotatingRight = true;  }
    public void stopRotateRight()  { rotatingRight = false; }
    
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

    private Point2D.Double updateXY(int thisDirection){
        double newX,newY;
        newX = x + speed * Math.cos(Math.toRadians(thisDirection));
        newY = y + speed * Math.sin(Math.toRadians(thisDirection));
        return (new Point2D.Double(newX, newY) );
    }
     
    private Point2D.Double checkInBounds(double newX, double newY ){
            
        if(!bounds.contains(newX, newY)){    
            if(bounds.getMinX() > newX){
                newX = bounds.getMinX();
                adjustBunnyWhenHit(180);
            } else if (bounds.getMaxX() < newX){
                newX = bounds.getMaxX();
                adjustBunnyWhenHit(0);
            }
            
            if(bounds.getMinY() > newY){
                newY = bounds.getMinY();
                adjustBunnyWhenHit(270);
            } else if (bounds.getMaxY() < newY){
                newY = bounds.getMaxY();
                adjustBunnyWhenHit(90);
            }        
        orientation=tempOrientation;
        }  
       
        return (new Point2D.Double(newX, newY));       
    }
    
    private Point2D.Double checkInBounds(Point2D.Double point){
        return (checkInBounds(point.getX(), point.getY()));
    }
       
    public Point2D.Double checkIntersectsCircle(double newX, double newY){
      double dx,dy,dr,D, r, x1, x2, y1, y2, xa,  xb, ya, yb, discriminant;
          
      //ToDO: get hitable circles, ie pumps, and "for" them 
      
      //change coordinates so that the centre of the circle would be at (0;0)
      x1 = x - hitableCircle.getCenterX();                           // (x1;y1) is the point from where the bunny starts moving
      y1 = y - hitableCircle.getCenterY();              
      x2 = newX - hitableCircle.getCenterX();                        // (x2;y2) is the predicted coordinates (new coordinates) of the bunny     
      y2 = newY - hitableCircle.getCenterY();
      r = hitableCircle.getHeight()/2;
      
      dx = x2 - x1;
      dy = y2 - y1;
      dr = Math.sqrt(dx*dx + dy*dy);
      D = (x1*y2) - (x2*y1);
      discriminant = r*r*dr*dr - D*D;
      
      //distance between the new coordinates and the centre of the circle smaller than radius
      if(Point.distance(x2, y2, 0, 0)<r){
         //break component
          //give headacke
                   
         if(discriminant > 0) {
            int thisDirection;
            thisDirection = adjustThisDirection(); 
  
            xa = (D * dy + sgn(dy) * dx * Math.sqrt(discriminant))/(dr*dr);
            xb = (D * dy - sgn(dy) * dx * Math.sqrt(discriminant))/(dr*dr);
            ya = (-D * dx + modulus(dy) * Math.sqrt(discriminant))/(dr*dr);
            yb = (-D * dx - modulus(dy) * Math.sqrt(discriminant))/(dr*dr);
          
            if((thisDirection> 90)&& (thisDirection <270)){
                x2 = Math.max(xa, xb);
            } else {
                x2 = Math.min(xa, xb);
            }
            
            if (thisDirection >180) {
                y2 = Math.max(ya,yb);
            } else {
                y2 = Math.min(ya,yb);
            }
            
            //switch back to the proper coordinate system
            newX = x2 + hitableCircle.getCenterX(); 
            newY = y2 + hitableCircle.getCenterY();
               
              //this is because if the angle that is provided is the one that is <180, without consideration of direction
            // so if the circle is hit from above, it has to be reajusted           
            boolean angleWouldNeedToBeAjusted = (newY<hitableCircle.getCenterY());
            
            //get the perpendicular angle to the hitpoint
            perpendicularAngle = calculateAngle(newX, newY, hitableCircle.getCenterX(), hitableCircle.getCenterY(), 
                    hitableCircle.getCenterX()+1, hitableCircle.getCenterY(), angleWouldNeedToBeAjusted);         
            adjustBunnyWhenHit(perpendicularAngle);              
        } 
         
         orientation = tempOrientation; 
      }
        
      return (new Point2D.Double(newX, newY));    
    }  
    
    private Point2D.Double checkIntersectsCircle(Point2D.Double point){
        return (checkIntersectsCircle(point.getX(), point.getY()));
    }
    
    private int calculateAngle(double x0,  double y0, double x1, double y1, double x2, double y2, boolean angleWouldNeedToBeAjusted){
        double dotProduct, lengths;
        int angle;
        //adust coordintes so that the point at (x1,y1) would be shifted to (0,0) and everything else shifted too.
        x0 -= x1;       
        y0 -=y1;        
        x2 -= x1;        
        y2 -= y1;       
        dotProduct = x0*x2 + y0*y2;
        lengths = Point2D.distance(0, 0, x0, y0) * Point2D.distance(0, 0, x2, y2);
        
         //TODO: Think about this, just temp. I.e exception?
        if (lengths == 0) {angle = 1000;}                          
        else { angle =  (int) Math.toDegrees(Math.acos(dotProduct/lengths)); }
 
        if(angleWouldNeedToBeAjusted){ angle = 360 - angle; }                   
        angle = (angle + 180)%360;  
        
        return angle;       
    }
    
    public void checkIntersectsSquare2(double x0, double y0){
        double x2, y2, halfHeight, halfWidth;
        int thisDirection;
 
        
         /* 
         * Checks if the square thingy is hit. Incomplete and messy.
         * Those are for sqaues.We dont even have squares, its just for "playing around". 
         * latter make these for multiple thingies
         */
        
        x2 = hitBounds.getCenterX();                    
        y2 = hitBounds.getCenterY();
        halfHeight = hitBounds.getHeight() / 2;
        halfWidth = hitBounds.getWidth() / 2;
       
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
        adjustBunnyWhenHit(270);
    }
    
    /*set x to be at the left border of the hittable object */
    private void handleHitFromLeft(double x2, double halfWidth){
        x = x2 - halfWidth;
        adjustBunnyWhenHit(0);
    }
    
    /*set x to be at the right border of the hittable object */
    private void handleHitFromRight(double x2, double halfWidth){
        x = x2 + halfWidth;
        adjustBunnyWhenHit(180);
    }
    
    /*set x to be at the bottom border of the hittable object */
    private void handleHitFromBelow(double y2, double halfHeight){
        y = y2 + halfHeight;
        adjustBunnyWhenHit(90);
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
    
    private void adjustBunnyWhenHit(int thisAngle){
        int thisDirection;
        perpendicularAngle = thisAngle;
        boolean wouldBounce = (speed > BOUNCE_SPEED || speed< -BOUNCE_SPEED);
               
        thisDirection = adjustThisDirection();       
        ajustOrientation(perpendicularAngle, thisDirection);

        System.out.println("this direction " + thisDirection + " perpAngle " + perpendicularAngle);
        if (modulus(90 - modulus(thisDirection - perpendicularAngle)) < SLIDE_ANGLE) {
            
            if (spinDirection.equalsIgnoreCase("left")) {
                tempOrientation = (perpendicularAngle + 90) % 360;
                System.out.println("perpen " + perpendicularAngle + " temp Or " + tempOrientation);
            } else {
                tempOrientation = perpendicularAngle - 90;
            }
        } else if (wouldBounce) {
            bounce();
        } else {
            if (spinDirection.equalsIgnoreCase("left")) {
                slide(1);
            } else {
                slide(-1);
            }
        }

        if (speed > FRICTION_SLOWDOWN) {
            speed -= FRICTION_SLOWDOWN ;
        }
    }
 
    private void ajustOrientation(int perpendicularDirection, int thisDirection){
       int newDirection = (thisDirection - (thisDirection - perpendicularDirection)*2);   //current direction shifted to the opposite side of the perpendicular angle by the angle between them
       direction = (720 + newDirection)%360;                //720 just to prevent from ever getting negative

       if((newDirection<-270)||(thisDirection<perpendicularDirection)) {
               spinDirection = "right";
               
       } else {
               spinDirection = "left";
       }             
    }
    
    private void bounce(){     
         speed = BOUNCE_AMOUNT * speed * sgn(speed);      
    }
    
    //<<<<Play around with numbers to make for the most fun bouncing.>>>>>   
    private void spin(){
        double spinCoef;
        int angle;
        double a,b,c,d;
        a=0.0001;        
        b=1;
        c= 3;
        d = 5;
       
        double speedCoef = b*speed*speed +c*speed + d;
     
        if(spinDirection.equalsIgnoreCase("left")){
            angle = (360 + perpendicularAngle- direction)%360;
            spinCoef = a*angle*angle ;
            orientation -= speedCoef*spinCoef;
       } else {
            angle = (360 +direction - perpendicularAngle)%360;
            spinCoef = a*angle*angle ;
           orientation += speedCoef*spinCoef;
       }       
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
    
    //now this is set from bunnyinterface, its just to play around, but if we had hitable rectangles, it could be set using those
    public void setHitBounds(Rectangle rectangle) {
        int newX = (int)(rectangle.getMinX() - radius);
        int newY = (int)(rectangle.getMinY() - radius);
        int newWidth = (int)(rectangle.getWidth() + radius + radius);
        int newHeight = (int) (rectangle.getHeight() + radius + radius);
        hitBounds = new Rectangle(newX, newY, newWidth, newHeight);        
    }
    
    //now this is set from bunnyinterface, its just to play around,but it could be set using pumps in the future 
    public void setCircle(Ellipse2D.Double circle) {
       double newX = circle.getX() - radius;
       double newY = circle.getY() - radius;
       double newWidth = circle.getWidth()+radius + radius;
       double newHeight = circle.getHeight() + radius + radius;
       hitableCircle = new Ellipse2D.Double(newX, newY, newWidth, newHeight);       
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
    
    private double modulus (double i){
        if (i < 0) {return -i;}
        else { return i;}
    }
    
    private int modulus (int i){
        if (i < 0) {return -i;}
        else { return i;}
    }
    
     private int sgn(double i){
        if(i>0) { return 1; }
        else { return -1; }
    }
   
}
