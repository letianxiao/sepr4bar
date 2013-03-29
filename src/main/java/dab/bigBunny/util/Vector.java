package dab.bigBunny.util;

import java.awt.geom.Point2D;

/**
 * Represent a vector from A to B
 * @author eduard
 */
public class Vector {
    Point2D.Double pA, pB;
    double orientation, length;
    
    public Vector(Point2D.Double a, Point2D.Double b) {
        double dX = (b.getX() - a.getX());
        double dY = (b.getY() - a.getY());
        this.pA = a;
        this.pB = b;
        // remember that here we're dealing w/ geometry coordinates
        // meaning that pi/2 is straight DOWN
        this.orientation = Math.atan(dY / dX);
        this.length      = Math.sqrt(dX * dX + dY * dY);
    }
    
    public Vector(Point2D.Double a, double orientation, double length) {
        this.pA = a;
        this.orientation = orientation;
        this.length = length;
        this.pB = new Point2D.Double(
                pA.getX() + length * Math.cos(orientation), 
                pA.getY() + length * Math.sin(orientation));
    }
    
    public double getLength()      { return length; }
    public double getOrientation() { return orientation; }
    public Point2D.Double getA()   { return pA; }
    public Point2D.Double getB()   { return pB; }
    
}
