package dab.bigBunny;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.border.TitledBorder;

public class BunnyInterface extends JFrame implements KeyListener {

    private ShowCanvas canvas;
    private BunnyController controller;

    public BunnyInterface() {

        controller = new BunnyController();
        Container container = getContentPane();
        canvas = new ShowCanvas(controller);
        container.add(canvas);
        setSize(800, 600);
        container.setFocusable(true);
        setVisible(true);
        container.addKeyListener(this);

        int delay = 1000 / 30; // milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // ...Perform a task...
                controller.step();
                canvas.repaint();
            }
        };
        new Timer(delay, taskPerformer).start();

    }

    public static void main(String arg[]) {
        new BunnyInterface();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                controller.startForward();
                break;
            case KeyEvent.VK_LEFT:
                controller.startRotateLeft();
                break;
            case KeyEvent.VK_RIGHT:
                controller.startRotateRight();
                break;
            case KeyEvent.VK_DOWN:
                controller.startBrake();
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                controller.stopForward();
                break;
            case KeyEvent.VK_LEFT:
                controller.stopRotateLeft();
                break;
            case KeyEvent.VK_RIGHT:
                controller.stopRotateRight();
                break;
            case KeyEvent.VK_DOWN:
                controller.stopBrake();
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}

class ShowCanvas extends JPanel {

    BufferedImage image;
    BunnyController controller;

    ShowCanvas(BunnyController controller) {
        this.controller = controller;
        setBackground(Color.white);
        setSize(450, 400);

        try {
            image = ImageIO.read(new File("resources/bunny.jpg"));
        } catch (Exception e) {
            System.err.println("Image not found");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform af = new AffineTransform();

        af.translate(controller.getX(), controller.getY());
        af.rotate((90 + controller.getRotation()) * Math.PI / 180);
        af.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        g2D.drawImage(image, af, this);

    }
}
