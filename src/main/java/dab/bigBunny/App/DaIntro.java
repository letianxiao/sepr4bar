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
    private BufferedImage storyImage;
    // each element stores the 3 coordinates of a pixel
    // [][0] = x axis - from left to right
    // [][1] = y axis - from top to bottom
    // [][2] = z axis - from viewer to back
    private double [][] story3DPixels;
            
    
    public DaIntro(MainWindow x) {
        this.mw = x;
        animator = new Timer(1000/FPS, this);
        setFocusable(true);
        
        System.out.println(DaIntro.class.getResource("WelcomeStory.txt").getFile());
        try {
            InputStreamReader storyFileStream =
                new InputStreamReader(
                    DaIntro.class.getResourceAsStream("WelcomeStory.txt"), Charset.forName("US-ASCII")); // charset of welcome story is (better be) ASCII
            
            
            BufferedImage tempImg = ImageIO.read(DaIntro.class.getResourceAsStream("background.png"));
            background = makeBackground(tempImg);
            storyImage = makeStoryImage(storyFileStream);
            
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
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
    
    private BufferedImage makeStoryImage(InputStreamReader storyStream) throws IOException{
        // read story
        // create 2d image (enlarged text)
        LineNumberReader story = new LineNumberReader(storyStream);
        BufferedImage    image = new BufferedImage(650, 900, Image.SCALE_DEFAULT);
        int imageMidPoint = image.getWidth() / 2; // used for centering text
        
        Graphics gTemp = image.getGraphics();
        gTemp.setFont(new Font ("Bookman Old Style", Font.BOLD, 20));
        
        String line;
        line = story.readLine();
        while(line != null) {
            // we want each line to be centered
            int halfLineWidth = gTemp.getFontMetrics().stringWidth(line) / 2;
            int lineHeight    = gTemp.getFontMetrics().getHeight();
            
            // 10 pixels between lines
            gTemp.drawString(line, imageMidPoint - halfLineWidth, (lineHeight + 10) * story.getLineNumber());
            
            line = story.readLine();
        }
        gTemp.dispose();
        
        return image;
        
        
        
        
    }
    
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
        int midX = (getWidth() / 2);
        // proper setup of background img
        g.drawImage(background, midX - background.getWidth() / 2, 0, background.getWidth(), getHeight(), null);
        //g.drawImage(storyImage, 0, 0, null);
    }


            
}

