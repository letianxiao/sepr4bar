package dab.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The splash displays at startup, and leads into the main menu.
 * This class is mostly hardcoded. You should not understand it.
 * It can be easily just removed.
 * It's just for the visual appearance.
 * 
 * The JFrame is JFrame that will be used throughout the whole game
 * @author Team Haddock + Ryan Castillo
 *
 */
public class NewSplash extends JFrame implements KeyListener{

    //the canvas where the animations will be drawn
    private CvStory canvas;
    //the panel of the splash screen
    JPanel mainPanel = new JPanel();
    private int counter = 0;
    private boolean running = false;

    public NewSplash() throws IOException {
        super("Team HADDOCK");
        setSize(1366,768);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0,0));
        canvas = new CvStory();
        canvas.addKeyListener(this);
        canvas.setFocusable(true);
        mainPanel.add(canvas);
        add(mainPanel, BorderLayout.CENTER);
        add(canvas);
        setVisible(true);
    }

    public void movePic ()
    {
        for (counter = 0; counter < 1700; counter++)
        {
            try
            { Thread.sleep(25);}
            catch (InterruptedException e){}
            canvas.repaint();
        }
        //show main menu after a certain ammount of time
        if (!running)
        {
            add(new Menu());
            setVisible(true);
            mainPanel.remove(canvas);
            canvas.setVisible(false);
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub
        int keyCode = arg0.getKeyCode();
        if(keyCode==KeyEvent.VK_SPACE){
            //show main menu when space is pressed
            add(new Menu());
            setVisible(true);
            canvas.setVisible(false);
            mainPanel.remove(canvas);
            running=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

}
class CvStory extends Canvas
{
    private String [] storyTxt;
    private double [][] pixels, pixels3D, beforeRotate;
    private int [][] stars;
    private int storySize, maxX, maxY;
    private int numPixels = 0;
    private boolean readData = false;
    Random generator = new Random ();

    CvStory () throws IOException
    {
        File f2 = new File("resources/WelcomeStory.txt");
        System.out.println(f2.getAbsolutePath());
        ReadFile f = new ReadFile ("resources/WelcomeStory.txt");
      

        storyTxt = f.getStory ();
        storySize = f.getSize ();

        pixels = new double [300000][3];
        beforeRotate = new double [300000][3];
        pixels3D = new double [300000][3];

        stars = new int [80][4];

        for (int a = 0; a < stars.length; a++)
        {
            stars[a][2] = (Math.abs (generator.nextInt ()) % 3) + 1;
            stars[a][3] = stars[a][2];

            stars[a][0] = (Math.abs (generator.nextInt ()) % 599) + 10;
            stars[a][1] = (Math.abs (generator.nextInt ()) % 499) + 10;
        }

        BufferedImage bufferImg = new BufferedImage (650, 900,
                Image.SCALE_DEFAULT);
        Graphics gBuffer = bufferImg.getGraphics ();

        Font g = new Font ("Bookman Old Style", Font.BOLD, 20);
        gBuffer.setFont(g);

        for (int i = 0; i < storySize; i++)
            gBuffer.drawString (storyTxt [i], 10, i * 20 + 30);

        for (int i = 0; i < 650 - 1; i++)
            for (int j = 0; j < 900 - 1; j++)
                if (bufferImg.getRGB (i, j) == -1)
                {
                    pixels[numPixels][0] = 0;
                    pixels[numPixels][1] = i - 135;
                    pixels[numPixels][2] = j + 410;      //value determines start

                    numPixels = numPixels + 1;

                    //System.out.println(numPixels);
                }

    }

    @Override
    public void paint (Graphics g)
    {
        Dimension d = getSize();

        maxX = d.width - 1;
        maxY = d.height - 1;

        double t = 0.0, Eye = 635.0;

        int originX = maxX / 2,
        originY = maxY / 2;

        BufferedImage bufferImg = new BufferedImage (getWidth(), getHeight(),
                Image.SCALE_DEFAULT);
        Graphics gBuffer = bufferImg.getGraphics ();

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("resources/spaceshipTiltedLeft.png"));
        } catch (IOException e) {
        }
        gBuffer.drawImage(img,0,0,null);
        gBuffer.setColor(Color.pink);
        gBuffer.setFont(new Font("Arial", Font.PLAIN, 25));
        gBuffer.drawString("Press space to continue...", 600, 700);
        rotateY(-59);

        gBuffer.setColor(Color.white);

        for (int i = 0; i < numPixels; i++)
            for (int j = 0; j < 3; j++)
            {

                if (j == 0)
                    t = 1.0 / (1.0 - (pixels[i][j] / Eye));
                else
                    pixels3D[i][j - 1] = t * pixels[i][j];
            }

        for (int i = 0; i < numPixels; i++)
        {
            gBuffer.drawLine ((int) Math.round(originX + pixels3D[i][0]),
                    (int) Math.round(pixels3D[i][1])+ 1,
                    (int) Math.round(originX + pixels3D[i][0]),
                    (int) Math.round(pixels3D[i][1])- 1);
            gBuffer.drawLine ((int) Math.round(originX + pixels3D[i][0])+ 1,
                    (int) Math.round(pixels3D[i][1])+ 1,
                    (int) Math.round(originX + pixels3D[i][0])+ 1,
                    (int) Math.round(pixels3D[i][1])- 1);
            gBuffer.drawLine ((int) Math.round(originX + pixels3D[i][0])- 1,
                    (int) Math.round(pixels3D[i][1])+ 1,
                    (int) Math.round(originX + pixels3D[i][0])- 1,
                    (int) Math.round(pixels3D[i][1])- 1);
        }

        try
        { Thread.sleep(10);}
        catch (InterruptedException e){}

        g.drawImage (bufferImg, 0, 0, this);
        rotateY(59);
        transPic (0, 0, -2);

    }

    public void rotateY (double ry)
    {
        double radAngle = (ry * Math.PI) / 180,
        newX = 0,
        newY = 0,
        newZ = 0;

        for (int i = 0; i < numPixels; i++)
        {
            newX = (pixels[i][0] * Math.cos (radAngle)) +
            (pixels[i][2] * (- Math.sin (radAngle)));
            newY = pixels[i][1];
            newZ = (pixels[i][0] * Math.sin (radAngle)) +
            (pixels[i][2] * Math.cos (radAngle));

            pixels[i][0] = newX;
            pixels[i][1] = newY;
            pixels[i][2] = newZ;
        }

    }

    public void transPic (double sx, double sy, double sz)
    {

        for (int i = 0; i < numPixels; i++)
        {
            pixels[i][0] = pixels[i][0] + sx;
            pixels[i][1] = pixels[i][1] + sy;
            pixels[i][2] = pixels[i][2] + sz;
        }

    }//end of transPic


    @Override
    public void update (Graphics g)
    {
        paint(g);
    }
}

//-------------------------------------------------------------------
//
//  Class for reading text files.
//
//-------------------------------------------------------------------

class ReadFile
{

    private BufferedReader inFile;
    private StringTokenizer tokenizer;
    private String line;
    private int numLine = 0;
    private String story [] = new String [40];

    ReadFile (String file) throws IOException
    {

        inFile = new BufferedReader (new FileReader (file));

        //***************************************************************

        //---------------------------------------------------------------
        //
        //  Code for reading the text file into the array.
        //
        //---------------------------------------------------------------

        line = inFile.readLine();

        while (line != null)
        {

            story [numLine] = line;
            ++numLine;
            line = inFile.readLine();
        }

        inFile.close();
    }//end of ReadFile Constructor

    public int getSize ()
    {
        return numLine;

    }

    public String [] getStory ()
    {
        return story;
    }


}