/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.bigBunny.App;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        story.rotateX(Math.PI / 8);
        story.move(250, 500, -500);
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
        
        
        animator.setInitialDelay(300);
        animator.start();
        addKeyListener(this);
        
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
    
    
    private void stop() {
        animator.stop();
        removeKeyListener(this);
        mw.showMenu();
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
        story.move(0, -1, 1);
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
    }
    
    private int getBackgroundWidth() {
        return (int)(getHeight() * aspectRatio);
    }
    
    private void draw3DPixels(Graphics g) {
        double [][] pixels = story.get3DPixels();
        int numPixels = story.getNumPixels();
        int i;
        /*int viewPointX = (int)(getHeight() * aspectRatio / 2); // halfway of the background
        int viewPointY = (int)(getHeight() / 2);*/
        int viewPointX = getWidth()  / 2;
        int viewPointY = getHeight() / 2;
        
        g.setColor(Color.WHITE);
        for (i = 0; i < numPixels; ++i) {
            double x = pixels[i][0], y = pixels[i][1], z = pixels[i][2]; // negative z is towards the back
            double scaleFactor = z / getBackgroundWidth() + 1;
            
            // translate z by 1
            x /= scaleFactor;
            y /= scaleFactor;
            
            // manual translation, JUST FOR TESTING
            x += viewPointX;
            y += getHeight();
            
            if (scaleFactor < 1)
                g.fillRect((int)x, (int)y, 2, 2);
            else
                g.fillRect((int)x, (int)y, 1, 1);
            
            
        }
    }


            
}

