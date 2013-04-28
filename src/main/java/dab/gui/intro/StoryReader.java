/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.gui.intro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;

/**
 *
 * @author eduard
 */
// package only
class StoryReader {

    private BufferedImage storyImage;
    private double[][] pixels3D;
    private int numPixels = 0;
    private int height;
    private int width;
    static int X_AXIS = 0, Y_AXIS = 1, Z_AXIS = 2;

    StoryReader(String filename) {
        InputStreamReader storyFileStream;

        storyFileStream = new InputStreamReader(
                DaIntro.class.getResourceAsStream("WelcomeStory.txt"),
                Charset.forName("US-ASCII")); // charset of welcome story is (better be) ASCII

        try {
            storyImage = makeStoryImage(storyFileStream);
            height = storyImage.getHeight();
            width  = storyImage.getWidth();
            
            pixels3D   = compute3DPixels(storyImage);   
        } catch (IOException e) {
            System.err.println(e);
            // non-fatal error; make image blank
            storyImage = new BufferedImage(100, 200, Image.SCALE_DEFAULT);
        }
        
        
    }
    
    BufferedImage getImage() {
        return storyImage;
    }
    
    int getWidth() {
        return width;
    }
    
    int getHeight() {
        return height;
    }
    
    int getNumPixels() {
        return numPixels;
    }
    
    double [][] get3DPixels() {
        return pixels3D;
    }
    
    private BufferedImage makeStoryImage(InputStreamReader storyStream) throws IOException {
        // read story
        // create 2d image (enlarged text)
        LineNumberReader story = new LineNumberReader(storyStream);

        // image needs to be *at least* large enough to hold our text
        // later we'll 'clip off' unnecesarry bits 
        BufferedImage image = new BufferedImage(900, 1300, Image.SCALE_DEFAULT);
        int imageMidPoint = image.getWidth() / 2; // used for centering text

        Graphics gTemp = image.getGraphics();
        gTemp.setFont(new Font("Bookman Old Style", Font.BOLD, 40));

        String line;
        line = story.readLine();
        while (line != null) {
            // we want each line to be centered
            int halfLineWidth = gTemp.getFontMetrics().stringWidth(line) / 2;
            int lineHeight = gTemp.getFontMetrics().getHeight();
            
            // 10 pixels between lines
            gTemp.drawString(line, imageMidPoint - halfLineWidth, (lineHeight + 10) * story.getLineNumber());

            line = story.readLine();
        }
        gTemp.dispose();
        
        // crop it before we return
        return cropImage(image);
    }
    
    private BufferedImage cropImage(BufferedImage img) {
        
        int x, y;
        int minX = 0, minY = 0, maxX = img.getWidth(), maxY = img.getHeight();
        int bgColor = Color.BLACK.getRGB(); // background is black
        
        // determine min y
        for (y = 0; y < img.getHeight(); ++y)
            for (x = 0; x < img.getWidth(); ++x)
                if (img.getRGB(x, y) != bgColor) { // 0 is black
                    minY = y;
                    y = img.getHeight();
                    break; // use gotos
                }
        
        detMinY:
        // determine max y
        for (y = img.getHeight() - 1; y >= 0; --y)
            for (x = 0; x < img.getWidth(); ++x)
                if (img.getRGB(x, y) != bgColor) {
                    maxY = y;
                    y = -1;
                    break;
                }
        
        // determine min x
        for (x = 0; x < img.getWidth(); ++x)
            for (y = minY; y <= maxY; ++y)
                if (img.getRGB(x, y) != bgColor) {
                    minX = x;
                    x = img.getWidth();
                    break;
                }
        
        // determine max x
        for (x = img.getWidth() - 1; x >= 0; --x)
            for (y = minY; y <= maxY; ++y)
                if (img.getRGB(x, y) != bgColor) {
                    maxX = x;
                    x = -1;
                    break;
                }
        
        
        return img.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
                
    }
    
    private double[][] compute3DPixels(BufferedImage img) {
        int midX = img.getWidth() / 2;
        //int midY = img.getHeight() / 2;
        int x, y;
        int bgColor = Color.BLACK.getRGB();
        double [][] pixels;
        
        // first we calculate the number of pixels we require
        for (y = 0; y < img.getHeight(); ++y) {
            for (x = 0; x < img.getWidth(); ++x) {
                if (img.getRGB(x, y) != bgColor) {
                    ++numPixels;
                }
            }
        }
        
        // create the array (that is a numPixels x 3 vertical array)
        pixels = new double[numPixels + 1][3]; 
        
        numPixels = 0;
        for (y = 0; y < img.getHeight(); ++y) {
            for (x = 0; x < img.getWidth(); ++x) {
                if (img.getRGB(x, y) != bgColor) {
                    pixels[numPixels][0] = x - midX;
                    pixels[numPixels][1] = y - getHeight();
                    pixels[numPixels][2] = 0;
                    ++numPixels;
                }
            }
        }
        
        return pixels;
    }
    
    /*
     * x' = [cos -sin][x]
     * y' = [sin  cos][y]
     * --------------------
     * We want a rotation on the X axis
     * z' = cosA z - sinA y
     * y' = sinA z + cosA y
     * 
     * WARNING: be very careful on the positive and negative orientations on the axes
     */
    
    public void rotateX(double radians) {
        int i;
        for (i = 0; i < numPixels; ++i) {
            pixels3D[i][Z_AXIS] = Math.cos(radians) * pixels3D[i][Z_AXIS] - Math.sin(radians) * pixels3D[i][Y_AXIS];
            pixels3D[i][Y_AXIS] = Math.sin(radians) * pixels3D[i][Z_AXIS] + Math.cos(radians) * pixels3D[i][Y_AXIS];
        }
    }
    
    public void move(double x, double y, double z) {
        int i;
        for (i = 0; i < numPixels; ++i) {
            pixels3D[i][X_AXIS] += x;
            pixels3D[i][Y_AXIS] += y;
            pixels3D[i][Z_AXIS] += z;
        }
    }
    
}
