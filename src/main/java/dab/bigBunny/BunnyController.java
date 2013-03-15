package dab.bigBunny;

public class BunnyController {

    int rotation;
    double x, y, speed;
    boolean forward, rotateLeft, rotateRight, braking;
    final int rotationAmount = 5;
    final double defAcceleration = 0.4;
    final double breakingAcceleration = -1;
    final double noramlStopping = -0.5;

    BunnyController() {
        x = 100;
        y = 100;
        rotation = 0;
        forward = false;
        rotateLeft = false;
        rotateRight = false;
        speed = 1;
        braking = false;

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
        double acceleration = 0;
        if (forward && !braking) {
            acceleration = defAcceleration;
        } else if (braking) {
            acceleration = breakingAcceleration;
        } else {
            acceleration = noramlStopping;
        }
        speed = speed + acceleration;
        if (speed < 0) {
            speed = 0;
        }
        x = x + speed * Math.cos(Math.toRadians(rotation));
        y = y + speed * Math.sin(Math.toRadians(rotation));
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
