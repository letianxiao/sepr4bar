/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import dab.engine.utilities.Temperature;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author eduard
 * note: beats me why Da, but it's better than just naming the class Intro
 */
public class DaIntro extends JPanel implements ActionListener, KeyListener {
    private static int FPS = 30;
    private Timer animator;   
    private MainWindow mw;
    private BufferedImage background;
    // each element stores the 3 coordinates of a pixel
    // [][0] = x axis - from left to right
    // [][1] = y axis - from top to bottom
    // [][2] = z axis - from viewer to back
    private StoryReader story;
    private double aspectRatio;    
    
    public DaIntro(MainWindow x) {
        this.mw = x;
        animator = new Timer(1000/FPS, this);
        setFocusable(true);

        
        story = new StoryReader("WelcomeStory.txt");
        
        try {
            // load background raw img
            BufferedImage tempImg = ImageIO.read(DaIntro.class.getResourceAsStream("background.png"));
            
            // make actual background img
            background = makeBackground(tempImg);
            aspectRatio = (double)background.getWidth() / (double)background.getHeight();
        } catch (FileNotFoundException e) { // fatal errors, throw them as runtime exceptions
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
                
        // load story
        // load background
        // create story 2d image (enlarged text)
        // create 3d story (rotate)
        // position 3d story with center aligned with screen 
        // calculate 3d story movement vector (3d)
    }
    
    private BufferedImage makeBackground(BufferedImage rawImg) {
        BufferedImage temp = new BufferedImage(rawImg.getWidth(), rawImg.getHeight(), Image.SCALE_DEFAULT);
        Graphics gBackgnd = temp.getGraphics();
        gBackgnd.drawImage(rawImg, 0, 0, null); // raw image does not have scaling (I think TODO: investigate)
        gBackgnd.setFont(new Font("Arial", Font.PLAIN, 25));
        gBackgnd.setColor(Color.PINK);
        gBackgnd.drawString("Press space to continue...",
                rawImg.getWidth() / 2, /* mid point */
                rawImg.getHeight() - 50 - gBackgnd.getFontMetrics().getHeight()); /* 100 pixels from the bottom */
        gBackgnd.dispose();
        return temp;
    }
    
    /*
    private double[][] makeStoryPixels(BufferedImage storyImage) {
        // determine min x and y, and max x and y
        // crop the image so that it starts from (0,0) and goes to max x,y - min x,y
        int x, y;
        int h = storyImage.getHeight();
        int w = storyImage.getWidth();
        int blackColor = Color.BLACK.getRGB();
        return null;
        
        for (y = 0; y < h; ++y)
            for (x = 0; x < w; ++x)
                if (storyImage.getRGB(x, y) != Color.BLACK.getRGB())
            
        
        return null;
    }*/
    
    public void start() {
        animator.start();
        addKeyListener(this);
    }
    
    private void stop() {
        animator.stop();
        mw.setCurrentPanel("menu");
        
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            stop();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent ke) {}
    
    @Override
    public void keyTyped(KeyEvent ke) {}
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        // update 3d story position
        repaint();
    }
                        
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int bgApparentWidth = (int)(getHeight() * aspectRatio);
        int midX = (getWidth() / 2);
        
        // background is black
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // proper setup of background img (maintains aspect ratio)
        g.drawImage(background, midX - bgApparentWidth / 2, 0, bgApparentWidth, getHeight(), null);
        
        draw3DPixels(g);
        
        //g.drawImage(story.getImage(), 0, 0, null);
    }
    
    private void draw3DPixels(Graphics g) {
        double [][] pixels = story.get3DPixels();
        int numPixels = story.getNumPixels();
        int i;
        /*int viewPointX = (int)(getHeight() * aspectRatio / 2); // halfway of the background
        int viewPointY = (int)(getHeight() / 2);*/
        int viewPointX = getWidth() / 2;
        int viewPointY = getHeight() / 2;
        
        g.setColor(Color.WHITE);
        for (i = 0; i < numPixels; ++i) {
            double x = pixels[i][0], y = pixels[i][1], z = pixels[i][2];
            
            // translate z by 1
            x /= z / 1000 + 1;
            y /= z / 1000 + 1;
            // manual translation, JUST FOR TESTING
            x += viewPointX;
            y += getHeight();
            //System.out.println("x: " + x + "y: " + y);
            
            
            g.fillRect((int)x, (int)y, 1, 1);
            
        }
    }


            
}

