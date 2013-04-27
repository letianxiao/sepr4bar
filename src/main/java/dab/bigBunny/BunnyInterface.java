package dab.bigBunny;

import dab.engine.simulator.Condenser;
import dab.engine.simulator.Pump;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BunnyInterface extends JFrame implements KeyListener {

    private ShowCanvas canvas;
    private BunnyController bunnyController;
    private Environment environment;
    private Dimension resolution;
    private JFrame frame;
    private HitBoundsController hitBoundsController;
    

    public BunnyInterface() {
        frame = new JFrame("MockGui");
        //for now temproray resolution. The one in haddocks game needs fixing as well
        resolution = new Dimension(800, 600);
        environment = new Environment(resolution.width, resolution.height);
        
        //this Has to be before the bunnyController initialisation
        hitBoundsController = new HitBoundsController();
        // new Pump just temprorary, and the numbers. Use the real ones later
        hitBoundsController.addHitableComponent(new Circle(new Pump(null, null) ,300, 300, 40,40));
        hitBoundsController.addHitableComponent(new Circle(new Pump(null, null) ,200, 200, 73, 73));
        hitBoundsController.addHitableComponent(new TheRectangle(new Condenser(), 500, 500, 40, 40));
        
        
        
        //Change radius according to image
        bunnyController = new BunnyController(environment, hitBoundsController, 20);        
        Container container = frame.getContentPane();
        canvas = new ShowCanvas(bunnyController, environment, resolution.width, resolution.height, hitBoundsController);
        container.setLayout(null);
        container.add(canvas);
        container.setMinimumSize(resolution);
        container.setPreferredSize(resolution);
        container.setMaximumSize(resolution);
        container.setFocusable(true);
        frame.setResizable(false);
        frame.setVisible(true);
        container.addKeyListener(this);
        frame.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        frame.pack();

        int delay = 1000 / 30; // milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // ...Perform a task...
                bunnyController.step();
                environment.step();
                canvas.repaint();
                System.out.println("" + bunnyController.getOrientation());
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
                bunnyController.startForward();
                break;
            case KeyEvent.VK_LEFT:
                bunnyController.startRotateLeft();
                break;
            case KeyEvent.VK_RIGHT:
                bunnyController.startRotateRight();
                break;
            case KeyEvent.VK_DOWN:
                bunnyController.startBrake();
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
                bunnyController.stopForward();
                break;
            case KeyEvent.VK_LEFT:
                bunnyController.stopRotateLeft();
                break;
            case KeyEvent.VK_RIGHT:
                bunnyController.stopRotateRight();
                break;
            case KeyEvent.VK_DOWN:
                bunnyController.stopBrake();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    } //Do nothing
}

class ShowCanvas extends JPanel implements MouseListener {

    BufferedImage bunny;
    BunnyController bunnyController;
    Environment environment;
    HitBoundsController hitBoundsController;
    JProgressBar bar;
    private Rectangle bounds;
    private JLabel box, pump;
    private ImageIcon boxToHit;
    private Ellipse2D.Double hitableCircle, pumpCircle;

    
    

    ShowCanvas(BunnyController controller, Environment environment, int dimX, int dimY, HitBoundsController hitBoundsController) {
        this.hitBoundsController = hitBoundsController;
        this.bunnyController = controller;
        this.environment = environment;
        this.setSize(dimX, dimY);
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
        box.setBounds(hitBoundsController.getHittableComponents().get(2).getDimensions());
        this.add(box);
        box.setVisible(true);
        //controller.setHitBounds(box.getBounds());
           
        pump = new JLabel();
        pump.setIcon(new ImageIcon("resources/mainInterface/MOVINGPUMP_MAIN_SCALED.gif"));  
        pump.setBounds(hitBoundsController.getHittableComponents().get(1).getDimensions());
        this.add(pump);
        Rectangle r = new Rectangle(hitBoundsController.getHittableComponents().get(1).getDimensions());
        pumpCircle = new Ellipse2D.Double(r.x, r.y, r.width, r.height);
       
        r = new Rectangle(hitBoundsController.getHittableComponents().get(0).getDimensions());
        hitableCircle = new Ellipse2D.Double(r.x, r.y, r.width, r.height);          

        //to call this on the reactorPannel, not on this thing
        bounds = this.getBounds();
        controller.setBounds(bounds);

        //This needs to be added to the ReactorPannel, so that only on reactor pannel we could shoot
        addMouseListener(this);

        try {
            bunny = ImageIO.read(new File("resources/bunny2.jpg"));
        } catch (Exception e) {
            System.err.println("Image not found");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform af = new AffineTransform();

        af.translate(bunnyController.getX(), bunnyController.getY());
        af.rotate((90 + bunnyController.getOrientation()) * Math.PI / 180);
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

        Ellipse2D.Double circle = new Ellipse2D.Double((double) bunnyController.getX() - 20, (double) bunnyController.getY() - 20, 40.0, 40.0);
        g2D.drawImage(bunny, af, this);
        g2D.setColor(Color.black);
        g2D.draw(circle);
        
        
        g2D.setColor(Color.GREEN);
        g2D.fill(hitableCircle);
        
        g2D.draw(pumpCircle);
        

        bar.setValue(bunnyController.getHealth());
    }

    public void mousePressed(MouseEvent e) {
        Point clicked = new Point(e.getX(), e.getY());


        //Also get the power generated, check if it is > then some amount,
        //if it is - subtrackt that amount and call this:
        double distance = clicked.distance(bunnyController.getCoordinates());
        if (distance <= bunnyController.getRadius()) {
            System.out.println("Bunny has been shot");
            bunnyController.hasBeenShot();
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
}
