package dab.bigBunny;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.border.TitledBorder;

public class BunnyInterface extends JFrame implements KeyListener {

    private ShowCanvas canvas;
    private BunnyController controller;
    private Environment environment;

    public BunnyInterface() {

        environment = new Environment(800, 600);
        
        controller = new BunnyController(environment, 10);
        Container container = getContentPane();
        canvas = new ShowCanvas(controller, environment);
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
                environment.step();
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
    Environment environment;

    ShowCanvas(BunnyController controller, Environment environment) {
        this.controller = controller;
        this.environment = environment;
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
        
        for (Slime s : environment.getSlimes()) {
            Ellipse2D.Double circle = new Ellipse2D.Double(
                    s.getLocation().getX() - s.getRadius(), 
                    s.getLocation().getY() - s.getRadius(), s.getRadius() * 2, s.getRadius() * 2);
            double f = s.getFreshness();
            int rgb = (int)(255 * (1 - f)); // so that things get whiter
            Color c = new Color(127, rgb, rgb);
            g2D.setColor(c);
            g2D.fill(circle);
            
        }
        
        Ellipse2D.Double circle = new Ellipse2D.Double((double)controller.getX() - 10, (double)controller.getY() - 10, 20.0, 20.0);
        g2D.drawImage(image, af, this);
        g2D.setColor(Color.black);
        g2D.draw(circle);
        
        
    }
}
