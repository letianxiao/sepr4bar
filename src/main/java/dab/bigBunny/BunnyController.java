package dab.bigBunny;

import java.awt.Point;

public class BunnyController {

    int rotation;
    double x, y, speed;
    boolean forward, rotateLeft, rotateRight, braking;
    final int rotationAmount = 5;
    final double defAcceleration = 0.4;
    final double breakingAcceleration = -1;
    final double noramlStopping = -0.5;
    Environment environment;
    int radius;

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
    }

    public void step() {
        moveForward(forward);
        doRotateRight(rotateRight);
        doRotateLeft(rotateLeft);
    }

    public void startForward() {
        forward = true;
    }

    public void stopForward() {
        forward = false;
    }

    public void startRotateLeft() {
        rotateLeft = true;
    }

    public void stopRotateLeft() {
        rotateLeft = false;
    }

    public void startRotateRight() {
        rotateRight = true;
    }

    public void stopRotateRight() {
        rotateRight = false;
    }

    public void doRotateLeft(boolean left) {
        if (left) {
            rotation = (rotation - rotationAmount) % 360;

        }
    }

    public void doRotateRight(boolean right) {
        if (right) {
            rotation = (rotation + rotationAmount) % 360;

        }
    }

    //Do not Go outside the game!!
    public void moveForward(boolean forw) {
        double acceleration;
        if (forward && !braking) {
            acceleration = defAcceleration;
        } else if (braking) {
            acceleration = breakingAcceleration;
        } else {
            acceleration = noramlStopping;
        }
        
        Slime intersected = environment.intersectWithSlime(new Point(getX(), getY()), radius);
        if (intersected != null) {
            System.out.println("Intersected with " + intersected.toString());
            // BAD CODE!!!
            if (speed > 2) {
                acceleration = -1 * (intersected.getFreshness() + 0.5);
            } else {
                acceleration /= 2;
            }
        }
        
        System.out.println(String.format("speed: %f, acc: %f", speed, acceleration));
        
        speed += acceleration;
        if (speed < 0) {
            speed = 0;
        }
        x += speed * Math.cos(Math.toRadians(rotation));
        y += speed * Math.sin(Math.toRadians(rotation));
        
       
    }

    public void startBrake() {
        braking = true;
    }

    public void stopBrake() {
        braking = false;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getRotation() {
        return rotation;
    }
}
