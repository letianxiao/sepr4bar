/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author eduard
 */
public class TwoPlayerScreen extends JPanel implements MouseListener, KeyListener, ActionListener {

    BufferedImage bunny;
    BunnyController controller;
    Environment environment;
    JProgressBar bar;
    private Rectangle bounds;
    private JLabel box;
    private ImageIcon boxToHit;
    private Timer animator;

    public TwoPlayerScreen(BunnyController controller, Environment environment) {
        this.controller = controller;
        this.environment = environment;
        this.animator = new Timer(1000/30, this);
        
        setFocusable(true);
        //requestFocusInWindow();
        
        //this.setSize(dimX, dimY);
        this.setLayout(null);
        setBackground(Color.WHITE);
        bar = new JProgressBar(0, controller.getHealth());
        this.add(bar);
        bar.setBounds(10, 10, 100, 30);
        bar.setVisible(true);
        bar.setStringPainted(true);

        box = new JLabel("Box");
        boxToHit = new ImageIcon("resources/HitableBox.png");
        box.setIcon(boxToHit);
        box.setBounds(500, 500, 40, 40);
        this.add(box);
        box.setVisible(true);
     //   controller.setHitBounds(box.getBounds());

        //to call this on the reactorPannel, not on this thing
        //bounds = new Rectangle(getWidth(), getHeight());
        
        //controller.setBounds(bounds);

        //This needs to be added to the ReactorPannel, so that only on reactor pannel we could shoot
        addMouseListener(this);
        addKeyListener(this);

        try {
            bunny = ImageIO.read(new File("resources/bunny.jpg"));
        } catch (Exception e) {
            System.err.println("Image not found");
        }

        this.animator.setInitialDelay(150);
        this.animator.start();
        
        //setFocusable(true);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform af = new AffineTransform();

        af.translate(controller.getX(), controller.getY());
        af.rotate((90 + controller.getOrientation()) * Math.PI / 180);
        af.translate(-bunny.getWidth() / 2, -bunny.getHeight() / 2);

        for (Slime s : environment.getSlimes()) {
            Ellipse2D.Double circle = new Ellipse2D.Double(
                    s.getLocation().getX() - s.getRadius(),
                    s.getLocation().getY() - s.getRadius(), s.getRadius() * 2, s.getRadius() * 2);
            double f = s.getFreshness();
            int rgb = (int) (255 * (1 - f)); // so that things get whiter
            Color c = new Color(127, rgb, rgb);
            g2D.setColor(c);
            g2D.fill(circle);

        }

        for (BulletHole b : environment.getBullets()) {
            Ellipse2D.Double circle = new Ellipse2D.Double(b.getLocation().getX(), b.getLocation().getY(), 4.0, 4.0);
            g2D.setColor(Color.BLACK);
            g2D.fill(circle);
        }

        Ellipse2D.Double circle = new Ellipse2D.Double((double) controller.getX() - 10, (double) controller.getY() - 10, 20.0, 20.0);
        g2D.drawImage(bunny, af, this);
        g2D.setColor(Color.black);
        g2D.draw(circle);

        bar.setValue(controller.getHealth());
    }

    public void mousePressed(MouseEvent e) {
        Point clicked = new Point(e.getX(), e.getY());


        //Also get the power generated, check if it is > then some amount,
        //if it is - subtrackt that amount and call this:
        double distance = clicked.distance(controller.getCoordinates());
        if (distance <= controller.getRadius()) {
            //System.out.println("Bunny has been shot");
            controller.hasBeenShot();
            //Animation of shot bunny 
        } else {
            environment.addBullet(clicked); //bullet hole if missed
        }
    }

    public void mouseClicked(MouseEvent e) {
    } //Do nothing

    public void mouseReleased(MouseEvent e) {
    } //Do nothing

    public void mouseEntered(MouseEvent e) {
    } //Do nothing

    public void mouseExited(MouseEvent e) {
    } //Do nothing

    
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keypress");
        
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
            case KeyEvent.VK_SPACE:
                environment.startSoftwareFailure();
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
    } //Do nothing
    
    @Override
    public void actionPerformed(ActionEvent e) {
                controller.step();
                environment.step();
                repaint();
    }

}

