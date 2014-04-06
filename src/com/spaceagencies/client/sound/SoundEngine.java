package com.spaceagencies.client.sound;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.spaceagencies.common.engine.Engine;
import com.spaceagencies.server.Time.Timestamp;

public class SoundEngine implements Engine {

    private Audio explosionEffect;

    public SoundEngine() {
     
    }

    @Override
    public void init() {
        try {
            oggEffect = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/gun1.ogg"));
            explosionEffect = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/explosion1.ogg"));
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
       /* try{
            AL.create();
          } catch (LWJGLException le) {
            le.printStackTrace();
            return;
          }
        
        AL10.alGetError();

        // Load the wav data.
        if(loadALData() == AL10.AL_FALSE) {
          System.out.println("Error loading data.");
          return;
        }
*/
        //setListenerValues();
        
       // AL10.alSourcePlay(source.get(0));
    }
    
    @Override
    public void start() {
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroy() {
        //killALData();
        AL.destroy();
    }

    @Override
    public void tick(Timestamp time) {
        // TODO Auto-generated method stub
        
    }

//        @Override
//        public void visit(BulletFiredEvent event) {
//            oggEffect.playAsSoundEffect(1f, 0.1f, false, 0, 0 ,0);
//        }
//        
//        @Override
//        public void visit(DamageEvent event) {
//            Component kernel = LoginManager.localPlayer.getShipList().get(0).getComponentByName("kernel");
//            Vec3 target = event.getTarget().getTransform().getTranslation();
//            Vec3 playerPosition = kernel.getFirstPart().getTransform().getTranslation();
//            
//            Vec3 worldDistance = target.minus(playerPosition);
//            Vec3 localDistance = worldDistance.rotate(kernel.getFirstPart().getTransform().inverse());
//            
//            Vec3 audioPosition = localDistance.multiply(0.03);
//            
////            Log.trace("(float) (event.getDamage().getEffectiveDamage()* 0.005f) "+(float) (event.getDamage().getEffectiveDamage()* 0.005f));
////            Log.trace("(float)  audioPosition.x"+(float)  audioPosition.x);
////            Log.trace("(float)  audioPosition.y"+(float)  audioPosition.y);
////            Log.trace("(float)  audioPosition.z"+(float)  audioPosition.z);
//            explosionEffect.playAsSoundEffect(3.0f,(float) Math.max(0,  (event.getDamage().getEffectiveDamage()* 0.005f)), false, (float)  audioPosition.x, (float)  audioPosition.y , (float) audioPosition.z);
//        }

    
    /** Buffers hold sound data. */
    IntBuffer buffer = BufferUtils.createIntBuffer(1);

    /** Sources are points emitting sound. */
    IntBuffer source = BufferUtils.createIntBuffer(1);

    /** Position of the source sound. */
    FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

    /** Velocity of the source sound. */
    FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

    /** Position of the listener. */
    FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

    /** Velocity of the listener. */
    FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

    /** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
    FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();

    private Audio oggEffect;

    /**
    * boolean LoadALData()
    *
    *  This function will load our sample data from the disk using the Alut
    *  utility and send the data into OpenAL as a buffer. A source is then
    *  also created to play that buffer.
    */
    int loadALData() {
      // Load wav data into a buffer.
      //AL10.alGenBuffers(buffer);

      //if(AL10.alGetError() != AL10.AL_NO_ERROR)
      //  return AL10.AL_FALSE;

      //Loads the wave file from your file system
      /*java.io.FileInputStream fin = null;
      try {
        fin = new java.io.FileInputStream("FancyPants.wav");
      } catch (java.io.FileNotFoundException ex) {
        ex.printStackTrace();
        return AL10.AL_FALSE;
      }
      WaveData waveFile = WaveData.create(fin);
      try {
        fin.close();
      } catch (java.io.IOException ex) {
      }*/

      //Loads the wave file from this class's package in your classpath
      //WaveData waveFile = WaveData.create("FancyPants.wav");
     
      
      
      //AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
      //waveFile.dispose();

      // Bind the buffer with the source.
      AL10.alGenSources(source);

      if (AL10.alGetError() != AL10.AL_NO_ERROR)
        return AL10.AL_FALSE;

      AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
      AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
      AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
      AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
      AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );

      // Do another error check and return.
      if (AL10.alGetError() == AL10.AL_NO_ERROR)
        return AL10.AL_TRUE;

      return AL10.AL_FALSE;
    }

    /**
     * void setListenerValues()
     *
     *  We already defined certain values for the Listener, but we need
     *  to tell OpenAL to use that data. This function does just that.
     */
    void setListenerValues() {
      AL10.alListener(AL10.AL_POSITION,    listenerPos);
      AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
      AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
    }

    /**
     * void killALData()
     *
     *  We have allocated memory for our buffers and sources which needs
     *  to be returned to the system. This function frees that memory.
     */
    void killALData() {
      AL10.alDeleteSources(source);
      AL10.alDeleteBuffers(buffer);
    }


    public void execute() {
      // Initialize OpenAL and clear the error bit.
      try{
        AL.create();
      } catch (LWJGLException le) {
        le.printStackTrace();
        return;
      }
      AL10.alGetError();

      // Load the wav data.
      if(loadALData() == AL10.AL_FALSE) {
        System.out.println("Error loading data.");
        return;
      }

      setListenerValues();

      // Loop.
      System.out.println("OpenAL Tutorial 1 - Single Static Source");
      System.out.println("[Menu]");
      System.out.println("p - Play the sample.");
      System.out.println("s - Stop the sample.");
      System.out.println("h - Pause the sample.");
      System.out.println("q - Quit the program.");
      char c = ' ';
      Scanner stdin = new Scanner(System.in);
      while(c != 'q') {
        try {
          System.out.print("Input: ");
          c = (char)stdin.nextLine().charAt(0);
        } catch (Exception ex) {
          c = 'q';
        }

        switch(c) {
          // Pressing 'p' will begin playing the sample.
          case 'p': AL10.alSourcePlay(source.get(0)); break;

          // Pressing 's' will stop the sample from playing.
          case's': AL10.alSourceStop(source.get(0)); break;

          // Pressing 'h' will pause the sample.
          case 'h': AL10.alSourcePause(source.get(0)); break;
        };
      }
      stdin.close();
      killALData();
      AL.destroy();
    }
}
