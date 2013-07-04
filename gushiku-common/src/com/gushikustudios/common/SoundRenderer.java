package com.gushikustudios.common;

import com.badlogic.gdx.utils.Array;

public class SoundRenderer
{
   private Array<GameSound> mLoopedSounds; // maintains list of looped sounds so they can be paused and resumed
   private int mState;
   private float mSoundHackTime;
   
   private static final int BASE_DISTANCE = 1;
   
   public static final int NORMAL = 0;
   public static final int PAUSED = 1;
   private static final float SOUND_HACK_TIMEOUT = 1.5f;
   
   static SoundRenderer scmSoundRenderer = null;
   
   public SoundRenderer()
   {
      mLoopedSounds = new Array<GameSound>();
      mState = NORMAL;
      scmSoundRenderer = this;
   }
   
   public static SoundRenderer getSoundRenderer()
   {
      return scmSoundRenderer;
   }
   
   public static void setSoundRenderer(SoundRenderer render)
   {
      scmSoundRenderer = render;
   }
   
   public void play(GameSound sound)
   {
      // let tick flimbo through to allow for menu sounds in spite of being paused...
      if ((Prefs.mFXIsOn) && ((mState == NORMAL) || (sound == Assets.mSndTickFlimbo)))
      {
         sound.play();
      }
   }
   
   public void playAtVol(GameSound sound,float vol)
   {
      // let tick flimbo through to allow for menu sounds in spite of being paused...
      if ((Prefs.mFXIsOn) && ((mState == NORMAL) || (sound == Assets.mSndTickFlimbo)))
      {
         sound.play(vol);
      }
   }
   
   public void play(GameSound sound,float distanceFromPlayer)
   {
      if (Prefs.mFXIsOn)
      {
         // Derived this equation from sound pressure level and volume equations that were posted on the net:
         //
         // Volume = invlog(20 * log (1 / distance) / 33.2)
         //
         
         float volumeRatio;
         
         // use the distance from player to adjust the volume of the sound
         if (distanceFromPlayer <= BASE_DISTANCE * (1 << 0))
         {
            volumeRatio = 1;
         }
         else if (distanceFromPlayer < BASE_DISTANCE * (1 << 1))
         {
            volumeRatio = interp(distanceFromPlayer,BASE_DISTANCE * (1 << 0),BASE_DISTANCE * (1 << 1),1,0.75f);
         }
         else if (distanceFromPlayer < BASE_DISTANCE * (1 << 2))
         {
            volumeRatio = interp(distanceFromPlayer,BASE_DISTANCE * (1 << 1),BASE_DISTANCE * (1 << 2),0.75f,0.25f);
         }
         else if (distanceFromPlayer < BASE_DISTANCE * (1 << 3))
         {
            volumeRatio = interp(distanceFromPlayer,BASE_DISTANCE * (1 << 2),BASE_DISTANCE * (1 << 3),0.25f,0.05f);
         }
         else if (distanceFromPlayer < BASE_DISTANCE * (1 << 4))
         {
            volumeRatio = interp(distanceFromPlayer,BASE_DISTANCE * (1 << 3),BASE_DISTANCE * (1 << 4),0.05f,0);
         }
         else
         {
            volumeRatio = 0;
         }
         
         if (volumeRatio > 0)
         {
            sound.play(volumeRatio);
         }
//         System.out.println("volumeRatio: " + volumeRatio + " @ " + distanceFromPlayer);
      }
   }
   
   public void loop(GameSound sound)
   {
      if ((Prefs.mFXIsOn) && (mState == NORMAL))
      {
         sound.loop();
         mLoopedSounds.add(sound);
      }
   }
   
   public void loopSingle(GameSound sound)
   {
      loopSingle(sound,1);
   }
   
   public void loopSingle(GameSound sound, float vol)
   {
      if ((Prefs.mFXIsOn) && (mState == NORMAL))
      {
         if (sound.loopSingle(vol))
         {
            mLoopedSounds.add(sound);
         }
      }
   }
   
   public void loopSingleSetVol(GameSound sound, float vol)
   {
      if ((Prefs.mFXIsOn) && (mState == NORMAL))
      {
         sound.loopSingleSetVol(vol);
      }
   }
   
   public void loopSingleSetPitch(GameSound sound, float pitch)
   {
      if ((Prefs.mFXIsOn) && (mState == NORMAL))
      {
         sound.loopSingleSetPitch(pitch);
      }
   }
   
   public void loopSingleSetVolAndPitch(GameSound sound, float vol, float pitch)
   {
      if ((Prefs.mFXIsOn) && (mState == NORMAL))
      {
         sound.loopSingleSetVolAndPitch(vol,pitch);
      }
   }
   
   public void stop(GameSound sound)
   {
      sound.stop();
      mLoopedSounds.removeValue(sound, true);
   }
   
   public void stopAllLooped()
   {
      for (int i = 0; i < mLoopedSounds.size; i++)
      {
         mLoopedSounds.get(i).stop();
      }
      mLoopedSounds.clear();
   }
   
   private void pauseAllLooped()
   {
      for (int i = 0; i < mLoopedSounds.size; i++)
      {
         mLoopedSounds.get(i).pause();
      }
   }
   
   private void resumeAllLooped()
   {
      // if sounds are still enabled...
      if (Prefs.mFXIsOn)
      {
         for (int i = 0; i < mLoopedSounds.size; i++)
         {
            mLoopedSounds.get(i).resume();
         }
      }
      else
      {
         // can't resume because the sounds are not enabled.. force stop all sounds.
         stopAllLooped();
      }
   }
   
   public void pause()
   {
      pauseAllLooped();
      mState = PAUSED;
   }
   
   public void resume()
   {
      resumeAllLooped();
      mState = NORMAL;
   }
   
   
   public void reset()
   {
      stopAllLooped();
      mState = NORMAL;
   }
   
   /**
    * 2D interpolation for linearizing between points on the "sound distance" curve
    * 
    * @param x
    * @param xa
    * @param xb
    * @param ya
    * @param yb
    * @return
    */
   public float interp(float x, float xa, float xb, float ya, float yb)
   {
      return (ya + (yb-ya)/(xb-xa)*(x - xa)/(xb-xa));
   }
   
   /*
    * Horrible hack to get around sound issue with soundpool!  Play a 'silent' sound every 1.5s to prevent the
    * AudioHardwareQSD from going into standby.
    */
   public void render(float deltaTime)
   {
      mSoundHackTime -= deltaTime;
      if (mSoundHackTime <= 0)
      {
         mSoundHackTime = SOUND_HACK_TIMEOUT;
         if (Assets.mSndSilence != null)
         {
            Assets.mSndSilence.play();
         }
      }
   }
   
   public int getState()
   {
      return mState;
   }
}
