package dab.gui.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * Class for handling the sounds in game
 */
public class Sounds extends Thread {
    private String filePath;
    private boolean loop;
    private Clip clip;

    /**
     *@param string filePath
     *@param boolean loop
     * 
     */
    public Sounds(String filePath, boolean loop){
        this.filePath = filePath;
        this.loop = loop;
    }

    /**
     *
     * Create a playClip
     */
    @Override
    public void run() {
        playClip(new File(filePath));
        //"resources/backgroundSound.wav"
    }

    /**
     *
     * Manages the playing of the clipFile
     */
    private void playClip(File clipFile){
        class AudioListener implements LineListener {
            private boolean done = false;

            @Override
            public synchronized void update(LineEvent event) {
                Type eventType = event.getType();
                if (eventType == Type.STOP || eventType == Type.CLOSE) {
                    done = true;
                    notifyAll();
                }

            }

            public synchronized void waitUntilDone() throws InterruptedException {
                while(!done) wait();
            }
        }
        try{
            do{
                AudioListener listener = new AudioListener();
                AudioInputStream audioInputStream;
                audioInputStream = AudioSystem.getAudioInputStream(clipFile);
                clip = AudioSystem.getClip();
                clip.addLineListener(listener);
                clip.open(audioInputStream);
                clip.start();
                try{
                    listener.waitUntilDone();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    clip.stop();
                    clip.flush();
                    clip.close();
                    throw new InterruptedException();
                }
            }while(loop);
        } catch (InterruptedException e) {
            interrupt();
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
        }
    }
    /**
     *
     * @return boolean (if music is playing or not)
     */
    public boolean isOn(){
        return clip.isRunning();
    }

    /**
     *
     * Stop music
     */
    public void stopIt() {
        clip.stop();
    }

    /**
     *
     * Start music
     */
    public void play() {
        clip.start();
    }

}
